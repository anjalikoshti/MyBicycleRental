package com.example.mybicyclerental.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.mybicyclerental.R;
import com.example.mybicyclerental.adapter.UpcomingAdapter;
import com.example.mybicyclerental.model.BookingModel;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UpcomingActivity extends AppCompatActivity {

    RecyclerView rvUpcoming;
    UpcomingAdapter upcomingAdapter;
    List<BookingModel>list =new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming);
        rvUpcoming=findViewById(R.id.rv_upcoming);

        FirebaseFirestore.getInstance().collection("BOOKINGS").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (int i=0; i<value.size(); i++){
                    try {
                        String bookingDate=value.getDocuments().get(i).get("date").toString();
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        Date strDate = sdf.parse(bookingDate);
                        if (new Date().before(strDate)) {
                            BookingModel model = value.getDocuments().get(i).toObject(BookingModel.class);
                            list.add(model);

                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                upcomingAdapter = new UpcomingAdapter(UpcomingActivity.this,list);
                rvUpcoming.setAdapter(upcomingAdapter);
                LinearLayoutManager linearLayoutManager=new LinearLayoutManager(UpcomingActivity.this);
                rvUpcoming.setLayoutManager(linearLayoutManager);
            }
        });
    }
}