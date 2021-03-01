package com.example.mybicyclerental.activity.Adminadapter;

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
import com.example.mybicyclerental.model.BookingModel;

import java.util.List;

public class ActiveAdapter extends RecyclerView.Adapter<ActiveAdapter.ActiveViewHolder> {
    Context context;
    List<BookingModel> list;
    public ActiveAdapter(Context context, List<BookingModel> list) {
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public ActiveAdapter.ActiveViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View Active=LayoutInflater.from(context).inflate(R.layout.active_item_view,parent,false);
        return new ActiveViewHolder(Active);
    }

    @Override
    public void onBindViewHolder(@NonNull ActiveAdapter.ActiveViewHolder holder, int position) {
        holder.tname.setText(list.get(position).getBicycleModel().getBicycleName());
        holder.tamount.setText(list.get(position).getTotal());
        Glide.with(context).load(list.get(position).getBicycleModel().getBicycleImage()).into(holder.imageView);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ActiveViewHolder extends RecyclerView.ViewHolder {
        TextView tname,tamount;
        Button btnEnd;
        ImageView imageView;
        public ActiveViewHolder(@NonNull View itemView) {
            super(itemView);
            tname=itemView.findViewById(R.id.tv_admin);
            tamount=itemView.findViewById(R.id.tv_adamount);
            imageView=itemView.findViewById(R.id.active_image);

        }
    }
}

