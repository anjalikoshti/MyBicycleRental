package com.example.mybicyclerental.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mybicyclerental.R;
import com.example.mybicyclerental.model.BookingModel;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CurrentRideActivity extends AppCompatActivity {
    BookingModel model;
    TextView tvCBN, tvCHH, tvCDY, tvCDD, tvTotal;
    ImageView icimage;
    CardView cvcdetail;
    Button btnstart;
    SharedPreferences preferences;
    List<BookingModel> list = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_ride);
        btnstart = findViewById(R.id.btn_start);
        cvcdetail = findViewById(R.id.cv_CN);
        tvCBN = findViewById(R.id.tv_CBN);
        tvCHH = findViewById(R.id.tv_Chh);
        tvCDY = findViewById(R.id.tv_Cdy);
        tvCDD = findViewById(R.id.tv_Cdate);
        tvTotal = findViewById(R.id.txt_Tprice);
        icimage = findViewById(R.id.image_CImage);
        preferences = getSharedPreferences("bookings", Context.MODE_PRIVATE);

        String currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

        FirebaseFirestore.getInstance().collection("BOOKINGS").whereEqualTo("date",currentDate)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (value != null && !value.isEmpty()) {
                            model = value.getDocuments().get(0).toObject(BookingModel.class);
                                tvCBN.setText(model.getBicycleModel().getBicycleName());
                                tvCDD.setText(model.getDate());
                                tvCDY.setText(model.getDays());
                                tvCHH.setText(model.getHour());
                                tvTotal.setText(model.getTotal()+"RS");
                            Picasso.with(CurrentRideActivity.this).load(model.getBicycleModel().getBicycleImage()).into(icimage);
                                //Glide.with(CurrentRideActivity.this).load(model.getBicycleModel().getBicycleImage()).into(icimage);
                        }
                        if (error!=null){
                            Toast.makeText(CurrentRideActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                     }
                });


        btnstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new IntentIntegrator(CurrentRideActivity.this).initiateScan();
  //            Intent intent  = new Intent(CurrentRideActivity.this,MapActivity.class);
  //              startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                String code = result.getContents();
                if (model.getBicycleModel().getBicycleID().equals(code)) {
                    Intent intent = new Intent(CurrentRideActivity.this, MapActivity.class);
                    startActivity(intent);

                    Toast.makeText(CurrentRideActivity.this, "Start Ride", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(CurrentRideActivity.this, "Invalid code, Retry!", Toast.LENGTH_SHORT).show();
            }}

        else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }




}
