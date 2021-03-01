package com.example.mybicyclerental.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mybicyclerental.R;
import com.example.mybicyclerental.activity.BookRideActivity;

public class CustomAdapter extends BaseAdapter {
    Context context;
    int[]image;
    int[]id;
    String[]name;

    Button btnsubmit;
    //private Object LayoutInflater;

    public CustomAdapter(Context context, int[]image, String[]name, int[]id) {
        this.context=context;
        this.image=image;
        this.id=id;
        this.name=name;

     Button btnsubmit;
    }

    @Override
    public int getCount() {
        return name.length;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view= LayoutInflater.from(context).inflate(R.layout.custom_item_view,viewGroup,false);
        ImageView imageView=view.findViewById(R.id.image);
        TextView tvName=view.findViewById(R.id.tv_text1);
        imageView.setImageResource(image[i]);
        tvName.setText(name[i]);



        return view;
    }
}
