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
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.paul9834.dynamicexoplayer.androidx.Adapters.CanalesAdapter;
import com.paul9834.dynamicexoplayer.androidx.ClientRetrofit.RetrofitClient;
import com.paul9834.dynamicexoplayer.androidx.Entities.Canales;
import com.paul9834.dynamicexoplayer.androidx.R;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;


/**
 * The type Recycler view home.
 */
public class RecyclerViewHome extends AppCompatActivity {

    /**
     * The Recycler view.
     */
    RecyclerView recyclerView;
    /**
     * The Adapter.
     */
    CanalesAdapter adapter;
    /**
     * The M swipe refresh layout.
     */
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview);


        ActionBar actionBar = getSupportActionBar(); // or getActionBar();
        Objects.requireNonNull(getSupportActionBar()).setTitle("Lista de Canales");

        recyclerView = findViewById(R.id.recyclerview2);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(RecyclerViewHome.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout2);
        RetrofitSearch();


    }

    private void RetrofitSearch() {


        SharedPreferences sp = getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
        int idUser = sp.getInt("your_int_key", -1);

        Call<List<Canales>> call = RetrofitClient.getInstance().getLogin().getCanales(idUser);

        call.enqueue(new Callback<List<Canales>>() {

            @Override
            public void onResponse(Call<List<Canales>> call, retrofit2.Response<List<Canales>> response) {

                adapter = new CanalesAdapter(response.body());

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
            public void onFailure(Call<List<Canales>> call, Throwable t) {
                Toast.makeText(RecyclerViewHome.this, "Error." + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("ERROR", Objects.requireNonNull(t.getMessage()));

            }
        });
    }
}

