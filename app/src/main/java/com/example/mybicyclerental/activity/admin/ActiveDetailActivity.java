package com.example.mybicyclerental.activity.admin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.mybicyclerental.R;
import com.example.mybicyclerental.activity.Adminadapter.ActiveAdapter;
import com.example.mybicyclerental.activity.CurrentRideActivity;
import com.example.mybicyclerental.activity.HistoryActivity;
import com.example.mybicyclerental.model.BookingModel;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ActiveDetailActivity extends AppCompatActivity {
    BookingModel model;
    RecyclerView rvactive;
    ActiveAdapter activeAdapter;
    ImageView imageView;
    List<BookingModel> list = new ArrayList<>();
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_detail);
        rvactive = findViewById(R.id.rv_active);
        imageView = findViewById(R.id.active_image);
        preferences =getSharedPreferences("bookings", Context.MODE_PRIVATE);
        String currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        FirebaseFirestore.getInstance().collection("BOOKINGS").whereEqualTo("date", currentDate)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (value != null && !value.isEmpty()) {
                            model = value.getDocuments().get(0).toObject(BookingModel.class);
                            Picasso.with(ActiveDetailActivity.this).load(model.getBicycleModel().getBicycleImage()).into(imageView);

                        }
                        if (error != null) {
                            Toast.makeText(ActiveDetailActivity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }


                        activeAdapter = new ActiveAdapter(ActiveDetailActivity.this, list);
                        rvactive.setAdapter(activeAdapter);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ActiveDetailActivity.this);
                        rvactive.setLayoutManager(linearLayoutManager);
                    }

                });


    }
}
