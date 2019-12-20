package com.paul9834.dynamicexoplayer.androidx.Activities;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.paul9834.dynamicexoplayer.androidx.Adapters.CanalesAdapter;
import com.paul9834.dynamicexoplayer.androidx.ClientRetrofit.RetrofitClient;
import com.paul9834.dynamicexoplayer.androidx.Controllers.CanalesInterface;
import com.paul9834.dynamicexoplayer.androidx.Entities.Canales;
import com.paul9834.dynamicexoplayer.androidx.Entities.PostCanalesID;
import com.paul9834.dynamicexoplayer.androidx.R;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.DynamicConcatenatingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.VideoRendererEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;


/**
 * Reproducción de ExoPlayer a través de microservicio Rest con links dinamicos..
 *
 * @author  Kevin Paul Montealegre Melo
 * @version 1.3
 */

public class MainActivity extends AppCompatActivity implements VideoRendererEventListener {

    private static final String TAG = "MainActivity";
    private PlayerView simpleExoPlayerView;
    private SimpleExoPlayer player;
    private TextView resolutionTextView;
    private RequestQueue mQueue;
    DynamicConcatenatingMediaSource dynamicConcatenatingMediaSource;
    private ArrayList<String> links;
    int lastWindowIndex = 0;
    Button boton;
    private View debugRootView;
    DataSource.Factory dataSourceFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resolutionTextView = new TextView(this);
        resolutionTextView = (TextView) findViewById(R.id.resolution_textView);

        // 1. Se llama al metodo Rest


        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

        // 2. Crea el reproductor

        player = ExoPlayerFactory.newSimpleInstance(this, trackSelector);
        simpleExoPlayerView = new SimpleExoPlayerView(this);
        simpleExoPlayerView = (SimpleExoPlayerView) findViewById(R.id.player_view);

        int h = simpleExoPlayerView.getResources().getConfiguration().screenHeightDp;
        int w = simpleExoPlayerView.getResources().getConfiguration().screenWidthDp;
        Log.v(TAG, "height : " + h + " weight: " + w);

        // 3. ¿Desea tener controladores?

        simpleExoPlayerView.setUseController(true);//set to true or false to see controllers
        simpleExoPlayerView.requestFocus();
        simpleExoPlayerView.setPlayer(player);
        dataSourceFactory = new DefaultDataSourceFactory(this, Util.getUserAgent(this, "exoplayer2example"), bandwidthMeter);


        // 4. Obtenemos el id de los canales //

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        SharedPreferences.Editor editor = prefs.edit();
        String uriCopy = prefs.getString("url", "no id");
        //   Log.e("URL :", uriCopy);


        // 5. Metodo REST para obtener los canales dinamicos //

        links = new ArrayList<>();


        Call<List<Canales>> call = RetrofitClient.getInstance().getLogin().getCanales();
        call.enqueue(new Callback<List<Canales>>() {
            @Override
            public void onResponse(Call<List<Canales>> call, retrofit2.Response<List<Canales>> response) {
                List<Canales> posts = response.body();

                for (Canales post : posts) {
                    links.add(post.getLink());
                }

                ArrayList<MediaSource> sources = new ArrayList<>();
                for (int i = 0; i < links.size(); i++) {
                    MediaSource videoSource = new HlsMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(links.get(i)));
                    sources.add(videoSource);
                }


                Bundle b = new Bundle();
                b = getIntent().getExtras();
                int position = b.getInt("name");

                Log.e("POSITION", Integer.toString(position));

                ConcatenatingMediaSource concatenatedSource = new ConcatenatingMediaSource();
                concatenatedSource.addMediaSources(sources);




                player.prepare(concatenatedSource);
                player.seekTo(position, 0);




            }
            @Override
            public void onFailure(Call<List<Canales>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error." + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("ERROR", t.getMessage());
            }
        });





        player.addListener(new ExoPlayer.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {
            }
            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
                Log.v(TAG, "Listener-onTracksChanged... ");
            }
            @Override
            public void onLoadingChanged(boolean isLoading) {
            }
            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                Log.v(TAG, "Listener-onPlayerStateChanged..." + playbackState+"|||isDrawingCacheEnabled():"+simpleExoPlayerView.isDrawingCacheEnabled());
            }
            @Override
            public void onRepeatModeChanged(int repeatMode) {
            }
            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
            }
            @Override
            public void onPlayerError(ExoPlaybackException error) {
                Log.e(TAG, "Listener-onPlayerError...");
                player.prepare(dynamicConcatenatingMediaSource);
                player.setPlayWhenReady(true);
            }
            @Override
            public void onPositionDiscontinuity(int reason) {
                int latestWindowIndex = player.getCurrentWindowIndex();
                if (latestWindowIndex != lastWindowIndex) {
                     lastWindowIndex = latestWindowIndex;
                    }
            }
            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
            }
            @Override
            public void onSeekProcessed() {
            }
        });
        player.setPlayWhenReady(true); //run file/link when ready to play.
        player.setVideoDebugListener(this);
    }



    @Override
    public void onVideoEnabled(DecoderCounters counters) {
    }
    @Override
    public void onVideoDecoderInitialized(String decoderName, long initializedTimestampMs, long initializationDurationMs) {
    }
    @Override
    public void onVideoInputFormatChanged(Format format) {
    }
    @Override
    public void onDroppedFrames(int count, long elapsedMs) {
    }
    @Override
    public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
        Log.v(TAG, "onVideoSizeChanged [" + " width: " + width + " height: " + height + "]");
        resolutionTextView.setText("RES:(WxH):" + width + "X" + height + "\n           " + height + "p");//shows video info
    }
    @Override
    public void onRenderedFirstFrame(Surface surface) {
    }
    @Override
    public void onVideoDisabled(DecoderCounters counters) {
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.v(TAG, "onStop()...");
    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.v(TAG, "onStart()...");
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.v(TAG, "onResume()...");
    }
    @Override
    protected void onPause() {
        super.onPause();
        Log.v(TAG, "onPause()...");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "onDestroy()...");
        player.release();
    }
}