package com.example.mybicyclerental.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.INotificationSideChannel;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.mybicyclerental.R;
import com.example.mybicyclerental.activity.HistoryActivity;
import com.example.mybicyclerental.activity.SelectHourDaysActivity;
import com.example.mybicyclerental.activity.UpcomingActivity;
import com.example.mybicyclerental.adapter.HistoryAdapter;
import com.example.mybicyclerental.model.BicycleModel;
import com.example.mybicyclerental.model.BookingModel;
import com.google.api.ResourceDescriptor;

public class BookingFragment extends Fragment{

    CardView History;
    CardView Upcoming;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_booking,container,false);
        History=view.findViewById(R.id.cv_history);
        Upcoming=view.findViewById(R.id.cv_upcoming);
        History.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), HistoryActivity.class);
                startActivity(intent);

            }
        });
        Upcoming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), UpcomingActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
}



