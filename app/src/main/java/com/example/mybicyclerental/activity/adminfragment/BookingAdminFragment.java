package com.example.mybicyclerental.activity.adminfragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.mybicyclerental.R;
import com.example.mybicyclerental.activity.Adminadapter.ActiveAdapter;
import com.example.mybicyclerental.activity.HistoryActivity;
import com.example.mybicyclerental.activity.UpcomingActivity;
import com.example.mybicyclerental.adapter.HistoryAdapter;
import com.example.mybicyclerental.adapter.UpcomingAdapter;
import com.example.mybicyclerental.model.BookingModel;

import java.util.ArrayList;
import java.util.List;

public class BookingAdminFragment extends Fragment {
    CardView cvhistory,cvupcoming;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booking_admin, container, false);
        cvhistory=view.findViewById(R.id.cv_Ahistory);
        cvupcoming=view.findViewById(R.id.cv_Aupcoming);

        cvhistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), HistoryActivity.class);
                startActivity(intent);

            }
        });
        cvupcoming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), UpcomingActivity.class);
                startActivity(intent);

            }
        });


        return view;

    }
}