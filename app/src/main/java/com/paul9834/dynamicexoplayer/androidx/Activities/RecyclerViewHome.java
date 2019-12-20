package com.paul9834.dynamicexoplayer.androidx.Activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

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

import retrofit2.Call;
import retrofit2.Callback;


public class RecyclerViewHome extends AppCompatActivity {

    RecyclerView recyclerView;
    CanalesAdapter adapter;
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview);

        recyclerView = findViewById(R.id.recyclerview2);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(RecyclerViewHome.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout2);
        RetrofitSearch();

    }

    private void RetrofitSearch() {

        Call<List<Canales>> call = RetrofitClient.getInstance().getLogin().getCanales();

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
                Log.e("ERROR", t.getMessage());

            }
        });
    }
}

