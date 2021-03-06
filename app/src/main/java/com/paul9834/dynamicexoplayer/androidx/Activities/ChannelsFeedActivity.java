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
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.paul9834.dynamicexoplayer.androidx.Adapters.ChannelAdapter;
import com.paul9834.dynamicexoplayer.androidx.ClientRetrofit.Retrofit_Base;
import com.paul9834.dynamicexoplayer.androidx.Entities.Channel;
import com.paul9834.dynamicexoplayer.androidx.Entities.Channel_info;
import com.paul9834.dynamicexoplayer.androidx.R;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;


/**
 * The type Recycler view home.
 */
public class ChannelsFeedActivity extends AppCompatActivity implements View.OnClickListener{

    /**
     * The Recycler view.
     */
    RecyclerView recyclerView;
    /**
     * The Adapter.
     */
    ChannelAdapter adapter;
    /**
     * The M swipe refresh layout.
     */
    SwipeRefreshLayout mSwipeRefreshLayout;

    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview);


        button = findViewById(R.id.epg);
        button.setOnClickListener(this);

        ActionBar actionBar = getSupportActionBar(); // or getActionBar();
        Objects.requireNonNull(getSupportActionBar()).setTitle("Lista de Canales");

        recyclerView = findViewById(R.id.recyclerview2);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ChannelsFeedActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout2);
        RetrofitSearch();


    }

    private void RetrofitSearch() {

        SharedPreferences sp = getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
        int idUser = sp.getInt("your_int_key", -1);

        Call<List<Channel_info>> call = Retrofit_Base.getInstance().getLogin().getCanales(idUser);

        call.enqueue(new Callback<List<Channel_info>>() {

            @Override
            public void onResponse(Call<List<Channel_info>> call, retrofit2.Response<List<Channel_info>> response) {

                adapter = new ChannelAdapter(response.body());

                Log.e("Holaaa", "xd" + adapter.getItemCount());

                recyclerView.setAdapter(adapter);

                mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

                    @Override
                    public void onRefresh() {
                        RetrofitSearch();
                        mSwipeRefreshLayout.setRefreshing(false); }

                });

            }

            @Override
            public void onFailure(Call<List<Channel_info>> call, Throwable t) {
                Toast.makeText(ChannelsFeedActivity.this, "Error." + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("ERROR", Objects.requireNonNull(t.getMessage()));

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.epg:
                startActivity(new Intent(ChannelsFeedActivity.this, EPGActivity.class));
                break;
        }
    }
}

