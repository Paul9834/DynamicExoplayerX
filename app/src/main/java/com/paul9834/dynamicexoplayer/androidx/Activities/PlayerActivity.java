/*
 *
 *  * Copyright (c) 2020. [Kevin Paul Montealegre Melo]
 *  *
 *  * Permission is hereby granted, free of charge, to any person obtaining a copy
 *  * of this software and associated documentation files (the "Software"), to deal
 *  * in the Software without restriction, including without limitation the rights
 *  * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  * copies of the Software, and to permit persons to whom the Software is
 *  * furnished to do so, subject to the following conditions:
 *  *
 *  * The above copyright notice and this permission notice shall be included in
 *  * all copies or substantial portions of the Software.
 *
 */

package com.paul9834.dynamicexoplayer.androidx.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PictureInPictureParams;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Rational;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exoplayer2.*;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.EventLogger;
import com.google.android.material.card.MaterialCardView;
import com.paul9834.dynamicexoplayer.androidx.ClientRetrofit.Retrofit_Base;
import com.paul9834.dynamicexoplayer.androidx.Entities.Channel_info;
import com.paul9834.dynamicexoplayer.androidx.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Reproducción de ExoPlayer a través de microservicio Rest con links dinamicos..
 *
 * @author Kevin Paul Montealegre Melo
 * @version 1.0
 */
public class PlayerActivity extends AppCompatActivity  implements Player.EventListener, Runnable  {

    ConcatenatingMediaSource concatenatedSource;
    TextView canal;
    ImageView image;
    MaterialCardView cardView;

    private ArrayList<String> links;
    private ArrayList<String> names;
    private ArrayList<String> logos;

    private PlayerView simpleExoPlayerView;
    private SimpleExoPlayer player;
    private boolean playWhenReady = true;

    private int currentWindow = 0;
    private long playbackPosition = 0;

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    private int idUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sp = getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
        idUser = sp.getInt("your_int_key", -1);


        scheduler.scheduleWithFixedDelay(this, 10,30, TimeUnit.SECONDS);

        simpleExoPlayerView = findViewById(R.id.player_view);

        canal = new TextView(this);
        canal = findViewById(R.id.texto);
        image = findViewById(R.id.imageView2);
        cardView = findViewById(R.id.material);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                cardView.setVisibility(View.INVISIBLE); }
        }, 3000);


        links = new ArrayList<>();
        names = new ArrayList<>();
        logos = new ArrayList<>();

        requestMediaAPI();

    }

    private MediaSource buildMediaSource(Uri uri) {
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this, "exoplayer-codelab");
        return new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
    }

    private void initializePlayer(ConcatenatingMediaSource concatenatingMediaSource) {
        if (player == null) {
            DefaultTrackSelector trackSelector = new DefaultTrackSelector();
            trackSelector.setParameters(
                    trackSelector.buildUponParameters().setMaxVideoSizeSd());
            player = ExoPlayerFactory.newSimpleInstance(this, trackSelector);
            player.addAnalyticsListener(new EventLogger(trackSelector));

        }

        simpleExoPlayerView.setPlayer(player);
        player.setPlayWhenReady(playWhenReady);
        player.seekTo(currentWindow, playbackPosition);
        player.prepare(concatenatingMediaSource, false, false);
        player.addListener(this);
    }

    void requestMediaAPI() {

        Call<List<Channel_info>> call = Retrofit_Base.getInstance().getLogin().getCanales(idUser);
        call.enqueue(new Callback<List<Channel_info>>() {
            @Override
            public void onResponse(Call<List<Channel_info>> call, retrofit2.Response<List<Channel_info>> response) {
                List<Channel_info> posts = response.body();

                for (Channel_info post : posts) {
                    links.add(post.getLink());
                    names.add(post.getName());
                    logos.add(post.getLogo());
                }

                ArrayList<MediaSource> sources = new ArrayList<>();

                for (String link : links) {
                    MediaSource mediaSource = buildMediaSource(Uri.parse(link));
                    sources.add(mediaSource);
                }


                Bundle b = new Bundle();
                b = getIntent().getExtras();
                int position = b.getInt("name");

                Log.e("POSITION", Integer.toString(position));

                concatenatedSource = new ConcatenatingMediaSource();
                concatenatedSource.addMediaSources(sources);

                initializePlayer(concatenatedSource);

                canal.setText(names.get(position));
                Picasso.get().load(logos.get(position)).into(image);

                player.seekTo(position, 0);
            }

            @Override
            public void onFailure(Call<List<Channel_info>> call, Throwable t) {
                Toast.makeText(PlayerActivity.this, "Error." + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("ERROR", t.getMessage());
            }
        });

    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            enterPictureInPictureMode(
                    new PictureInPictureParams.Builder()
                            .setAspectRatio(new Rational(350, 200))
                            .setSourceRectHint(new Rect(
                                    simpleExoPlayerView.getLeft(), simpleExoPlayerView.getTop(),
                                    simpleExoPlayerView.getRight(), simpleExoPlayerView.getBottom())).build());

        }
    }

    @Override
    public void run() {

        Log.e("Status:", "Runnable Ok");

        Call<Boolean> call = Retrofit_Base.getInstance().getLogin().checkStatusAccount(idUser);

        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {

                Log.e("Response API:", response.body() + "");
                if (response.isSuccessful()) {
                    boolean result = response.body();
                    if (!result) {
                        alerDialog();
                    }
                }
            }
            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {

            }
        });
    }
    void alerDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(PlayerActivity.this).create();
        alertDialog.setTitle("Alerta");
        alertDialog.setMessage("Su usuario se encuentra suspendido, por favor comuniquese con nosotros.");
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        scheduler.shutdown();

    }
}
