package com.example.mybicyclerental.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.mybicyclerental.R;
import com.example.mybicyclerental.adapter.ChooseBicycleAdapter;
import com.example.mybicyclerental.adapter.HistoryAdapter;
import com.example.mybicyclerental.adapter.OnBicycleSelectedListener;
import com.example.mybicyclerental.model.BicycleModel;
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

public class HistoryActivity extends AppCompatActivity {

    RecyclerView rvHistory;
    HistoryAdapter historyAdapter;
    ImageView imageView;
    List<BookingModel> list = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        rvHistory = findViewById(R.id.rv_history);
        imageView=findViewById(R.id.image_item);

        FirebaseFirestore.getInstance().collection("BOOKINGS").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (int i = 0; i < value.size(); i++) {
                    try {

                        String bookingDate = value.getDocuments().get(i).get("date").toString();
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        Date strDate = sdf.parse(bookingDate);
                        if (new Date().after(strDate)) {
                            BookingModel model = value.getDocuments().get(i).toObject(BookingModel.class);
                            list.add(model);

                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                historyAdapter = new HistoryAdapter(HistoryActivity.this, list);
                rvHistory.setAdapter(historyAdapter);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(HistoryActivity.this);
                rvHistory.setLayoutManager(linearLayoutManager);
            }
        });

    }
}