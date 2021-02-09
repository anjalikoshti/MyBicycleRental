package com.example.mybicyclerental.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mybicyclerental.R;
import com.example.mybicyclerental.activity.BookRideActivity;
import com.example.mybicyclerental.activity.ChooseBicycleActivity;
import com.example.mybicyclerental.model.BicycleModel;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class ChooseBicycleAdapter extends RecyclerView.Adapter<ChooseBicycleAdapter.BicycleViewHolder> {
    Context context;
    List<BicycleModel> list;
    OnBicycleSelectedListener listener;

    public ChooseBicycleAdapter(Context context, List<BicycleModel> list, OnBicycleSelectedListener listener) {
        this.context=context;
        this.list=list;
        this.listener=listener;
    }

    @NonNull
    @Override
    public BicycleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View Bicycle = LayoutInflater.from(context).inflate(R.layout.custom_item_view, parent, false);
        return new BicycleViewHolder(Bicycle);
    }

    @Override
    public void onBindViewHolder(@NonNull BicycleViewHolder holder, int position) {

        holder.tvname.setText(list.get(position).getBicycleName());
        Glide.with(context).load(list.get(position).getBicycleImage()).into(holder.imageView);

        holder.btnChoose.setOnClickListener(view -> {
            listener.getSelectedBicycle(list.get(position));
        });

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class BicycleViewHolder extends RecyclerView.ViewHolder {
        TextView  tvname;
        ImageView imageView;
        Button btnChoose;

        public BicycleViewHolder(View bicycle) {
            super(bicycle);

            tvname = itemView.findViewById(R.id.tv_text1);
            imageView = itemView.findViewById(R.id.image_item);
            btnChoose=itemView.findViewById(R.id.btn_choose);
        }

    }
}
