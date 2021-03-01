package com.example.mybicyclerental.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.example.mybicyclerental.R;
import com.example.mybicyclerental.model.BookingModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.FirebaseFirestore;

public class BookingDetailActivity extends AppCompatActivity {
    BookingModel bookingModel;
    TextView tvBN,tvhh,tvdy,tvdd,tvprice;
    ImageView imageView;
    Button btnbook;
    CardView cvdetail;
    SharedPreferences preferences;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_detail);
        cvdetail=findViewById(R.id.cv_BN);
        tvBN=findViewById(R.id.tv_BN);
        tvprice=findViewById(R.id.txt_price);
        tvdd=findViewById(R.id.tv_date);
        tvhh=findViewById(R.id.tv_hh);
        tvdy=findViewById(R.id.tv_dy);
        btnbook=findViewById(R.id.btn_bookbicycle);
        imageView=findViewById(R.id.image_BD);
        preferences=getSharedPreferences("booking" ,Context.MODE_PRIVATE);

       btnbook.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
                      Intent intent=new Intent(BookingDetailActivity.this,PaymentActivity.class);
               intent.putExtra("bookingModel",bookingModel);
               startActivity(intent);
           }

       });

        bookingModel= (BookingModel) getIntent().getSerializableExtra("bookingModel");

        tvBN.setText(bookingModel.getBicycleModel().getBicycleName());
        tvdd.setText(bookingModel.getDate());
        tvdy.setText(bookingModel.getDays());
        tvhh.setText(bookingModel.getHour());
        tvprice.setText(bookingModel.getTotal()+"RS");
        Glide.with(BookingDetailActivity.this).load(bookingModel.getBicycleModel().getBicycleImage()).into(imageView);
    }


}

