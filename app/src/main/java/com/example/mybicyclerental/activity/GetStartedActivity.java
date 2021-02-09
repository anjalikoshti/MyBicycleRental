package com.example.mybicyclerental.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mybicyclerental.R;

public class GetStartedActivity extends AppCompatActivity {
    Button btnget;
    SharedPreferences preferences;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);
        btnget=findViewById(R.id.btn_get);
        preferences=getSharedPreferences("user",MODE_PRIVATE);
        if (preferences.contains("email")){
            startActivity(new Intent(GetStartedActivity.this,BottomNavActivity.class));
            finish();
        }
        btnget.setOnClickListener(view -> {
            startActivity(new Intent(GetStartedActivity.this,LoginActivity.class));
            finish();
        });
    }
}
