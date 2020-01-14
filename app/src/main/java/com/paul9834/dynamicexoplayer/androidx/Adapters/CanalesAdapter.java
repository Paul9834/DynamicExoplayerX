package com.paul9834.dynamicexoplayer.androidx.Adapters;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
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

import static com.android.volley.VolleyLog.TAG;

public class CanalesAdapter extends RecyclerView.Adapter<CanalesAdapter.MyViewHolder> {

    private List<Canales> publications;
    private final static int FADE_DURATION = 1000;

    private boolean on_attach = true;
    long DURATION = 500;


    public CanalesAdapter(List<Canales> publications) {
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

        setAnimation(holder.itemView, position);

    }

    private void setAnimation(View itemView, int i) {
        if(!on_attach){
            i = -1;
        }
        boolean not_first_item = i == -1;
        i = i + 1;
        itemView.setTranslationX(-400f);
        itemView.setAlpha(0.f);
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator animatorTranslateY = ObjectAnimator.ofFloat(itemView, "translationX", -400f, 0);
        ObjectAnimator animatorAlpha = ObjectAnimator.ofFloat(itemView, "alpha", 1.f);
        ObjectAnimator.ofFloat(itemView, "alpha", 0.f).start();
        animatorTranslateY.setStartDelay(not_first_item ? DURATION : (i * DURATION));
        animatorTranslateY.setDuration((not_first_item ? 2 : 1) * DURATION);
        animatorSet.playTogether(animatorTranslateY, animatorAlpha);
        animatorSet.start();
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
            imagen = itemView.findViewById(R.id.imageView);
            test = itemView.findViewById(R.id.button2);


        }
    }
}