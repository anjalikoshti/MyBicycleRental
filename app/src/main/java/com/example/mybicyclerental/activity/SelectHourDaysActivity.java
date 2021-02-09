package com.example.mybicyclerental.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.example.mybicyclerental.R;

public class SelectHourDaysActivity extends AppCompatActivity {


    AutoCompleteTextView autoCompleteTextView,autoCompleteTextView1;
    String[]hour={"1 hour","2 hour","3 hour","4 hour","5 hour"};
    String[]days={"1 day","2 day","3 day","4 day","5 day",};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_hour);
        autoCompleteTextView=findViewById(R.id.hour_dropdown);
        autoCompleteTextView1=findViewById(R.id.day_dropdown);

        ArrayAdapter<String> adapter=new ArrayAdapter<>(SelectHourDaysActivity.this,R.layout.single_item_view,hour);
        autoCompleteTextView.setAdapter(adapter);

        ArrayAdapter<String> adapter1=new ArrayAdapter<>(SelectHourDaysActivity.this,R.layout.single_item_view,days);
        autoCompleteTextView1.setAdapter(adapter1);




    }
}