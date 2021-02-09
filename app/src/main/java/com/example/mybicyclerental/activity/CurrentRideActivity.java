package com.example.mybicyclerental.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.mybicyclerental.R;

public class CurrentRideActivity extends AppCompatActivity {
    Button btnscan;
    Button btnunlocked;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_ride);
        btnscan=findViewById(R.id.btn_scan);
        btnunlocked=findViewById(R.id.btn_unlocked);

        btnscan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(CurrentRideActivity.this,ScannerActivity.class);
                startActivity(intent);
            }
        });
    }


}
