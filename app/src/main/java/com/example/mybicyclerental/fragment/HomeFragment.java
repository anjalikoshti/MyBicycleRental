package com.example.mybicyclerental.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.mybicyclerental.R;
import com.example.mybicyclerental.activity.BookRideActivity;
import com.example.mybicyclerental.activity.ChooseBicycleActivity;
import com.example.mybicyclerental.activity.CurrentRideActivity;

public class HomeFragment extends Fragment {

    CardView BookRide;
    CardView CurrentRide;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_home,container,false);
        BookRide=view.findViewById(R.id.cv_book_ride);
        CurrentRide=view.findViewById(R.id.cv_current_ride1);
        BookRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), BookRideActivity.class);
                startActivity(intent);
            }
        });
        CurrentRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), CurrentRideActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

}
