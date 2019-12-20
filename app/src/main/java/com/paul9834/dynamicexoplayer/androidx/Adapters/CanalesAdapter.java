package com.paul9834.dynamicexoplayer.androidx.Adapters;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.paul9834.dynamicexoplayer.androidx.Activities.MainActivity;
import com.paul9834.dynamicexoplayer.androidx.Activities.RecyclerViewHome;
import com.paul9834.dynamicexoplayer.androidx.Entities.Canales;
import com.paul9834.dynamicexoplayer.androidx.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CanalesAdapter extends RecyclerView.Adapter<CanalesAdapter.MyViewHolder> {

    private List<Canales> publications;

    public CanalesAdapter(List<Canales> publications) {
        this.publications = publications;
    }

    @NonNull
    @Override
    public CanalesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CanalesAdapter.MyViewHolder holder, final int position) {

        Canales publications2 = publications.get(position);
        holder.textView.setText(publications2.getName());
        Picasso.get().load(publications2.getLogo()).into(holder.imagen);


        holder.test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), MainActivity.class);
                i.putExtra("name", position);
                v.getContext().startActivity(i);
            }
        });

    }
    @Override
    public int getItemCount() {
        return publications.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{


        TextView textView;
        TextView textView2;
        TextView textView3;
        ImageView imagen;
        Button test;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.txt);
            textView2 = itemView.findViewById(R.id.txt2);
            textView3 = itemView.findViewById(R.id.txt3);
            imagen = itemView.findViewById(R.id.imageView);
            test = itemView.findViewById(R.id.button2);


        }
    }
}