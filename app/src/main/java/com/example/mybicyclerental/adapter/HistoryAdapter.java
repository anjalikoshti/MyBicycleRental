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
import com.example.mybicyclerental.model.BicycleModel;
import com.example.mybicyclerental.model.BookingModel;
import com.google.api.ResourceDescriptor;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {
    Context context;
    List<BookingModel> list;
    OnBicycleSelectedListener listener;

    public HistoryAdapter(Context context, List<BookingModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View History = LayoutInflater.from(context).inflate(R.layout.upcoming_item_view, parent, false);
        return new HistoryViewHolder(History);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        holder.tname.setText(list.get(position).getBicycleModel().getBicycleName());
        holder.thour.setText(list.get(position).getHour());
        holder.tdays.setText(list.get(position).getDays());
        holder.tdate.setText(list.get(position).getDate());
        holder.tamount.setText(list.get(position).getTotal());
        Glide.with(context).load(list.get(position).getBicycleModel().getBicycleImage()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {

        return list.size();
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView tname,thour,tdate,tdays,tamount;
        ImageView imageView;

        public HistoryViewHolder(View History) {
            super(History);

            tname = itemView.findViewById(R.id.tv_Uname);
            thour=itemView.findViewById(R.id.tv_Uhour);
            tdate=itemView.findViewById(R.id.tv_Udate);
            tdays=itemView.findViewById(R.id.tv_Udays);
            tamount=itemView.findViewById(R.id.tv_Uamount);
            imageView=itemView.findViewById(R.id.u_image);
        }
    }
}

