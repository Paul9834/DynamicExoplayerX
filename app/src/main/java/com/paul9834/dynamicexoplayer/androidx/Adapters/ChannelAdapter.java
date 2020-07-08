package com.paul9834.dynamicexoplayer.androidx.Adapters;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.paul9834.dynamicexoplayer.androidx.Activities.PlayerActivity;
import com.paul9834.dynamicexoplayer.androidx.Entities.Channel_info;
import com.paul9834.dynamicexoplayer.androidx.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.android.volley.VolleyLog.TAG;

public class ChannelAdapter extends RecyclerView.Adapter<ChannelAdapter.MyViewHolder> {

    private List<Channel_info> publications;
    private final static int FADE_DURATION = 1000;

    private boolean on_attach = true;
    private long DURATION = 500;


    public ChannelAdapter(List<Channel_info> publications) {
        this.publications = publications;
    }


    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                Log.d(TAG, "onScrollStateChanged: Called " + newState);
                on_attach = false;
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        super.onAttachedToRecyclerView(recyclerView);
    }

    @NonNull
    @Override
    public ChannelAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChannelAdapter.MyViewHolder holder, final int position) {

        Channel_info publications2 = publications.get(position);
        holder.textView.setText(publications2.getName());
        Picasso.get().load(publications2.getLogo()).into(holder.imagen);



        holder.test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), PlayerActivity.class);
                i.putExtra("name", position);
                v.getContext().startActivity(i);
            }
        });


    }



    @Override
    public int getItemCount() {
        return publications.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{


        TextView textView;
        TextView textView2;
        TextView textView3;
        ImageView imagen;
        Button test;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.txt);
            imagen = itemView.findViewById(R.id.imageView);
            test = itemView.findViewById(R.id.button2);


        }
    }
}