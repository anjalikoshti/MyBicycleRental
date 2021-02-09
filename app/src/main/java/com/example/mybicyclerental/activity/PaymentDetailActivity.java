package com.example.mybicyclerental.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.example.mybicyclerental.R;
import com.example.mybicyclerental.model.BicycleModel;
import com.example.mybicyclerental.model.BookingModel;
import com.example.mybicyclerental.model.UserModel;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

public class PaymentDetailActivity extends AppCompatActivity {
    AutoCompleteTextView autoCompleteTextView;
    String[]payment={"Debit Card","Google Pay","Phone Pay"};
    BookingModel bookingModel;
    TextView tvBN,tvhh,tvdy,tvdd;
    ImageView imageView;
    CardView cvdetail;
    SharedPreferences preferences;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_detail);
        autoCompleteTextView=findViewById(R.id.selectpayment);
        cvdetail=findViewById(R.id.cv_BN);
        tvBN=findViewById(R.id.tv_BN);
        tvdd=findViewById(R.id.tv_date);
        tvhh=findViewById(R.id.tv_hh);
        tvdy=findViewById(R.id.tv_dy);
        imageView=findViewById(R.id.image_BD);
        preferences=getSharedPreferences("booking" ,Context.MODE_PRIVATE);

        ArrayAdapter<String> adapter=new ArrayAdapter<>(PaymentDetailActivity.this,R.layout.single_item_view,payment);
        autoCompleteTextView.setAdapter(adapter);


        bookingModel= (BookingModel) getIntent().getSerializableExtra("bookingModel");

        tvBN.setText(bookingModel.getBicycleModel().getBicycleName());
        tvdd.setText(bookingModel.getDate());
        tvdy.setText(bookingModel.getDays());
        tvhh.setText(bookingModel.getHour());
        Glide.with(PaymentDetailActivity.this).load(bookingModel.getBicycleModel().getBicycleImage()).into(imageView);
    }
}

