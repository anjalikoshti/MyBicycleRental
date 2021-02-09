package com.example.mybicyclerental.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.example.mybicyclerental.adapter.CustomAdapter;
import com.example.mybicyclerental.fragment.BookingFragment;
import com.example.mybicyclerental.fragment.HomeFragment;
import com.example.mybicyclerental.fragment.PaymentFragment;
import com.example.mybicyclerental.R;
import com.example.mybicyclerental.fragment.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomNavActivity extends AppCompatActivity {

    FrameLayout container;
    BottomNavigationView bottomNav;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_nav);

        container=findViewById(R.id.container);

        bottomNav=findViewById(R.id.bottom_nav);

        bottomNav.setSelectedItemId(R.id.home);



        getSupportFragmentManager().beginTransaction().replace(R.id.container,new HomeFragment()).commit();

        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId()==R.id.home){
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,new HomeFragment()).commit();
                }
                if (item.getItemId()==R.id.book){
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,new BookingFragment()).commit();
                }
                if (item.getItemId()==R.id.pay){
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,new PaymentFragment()).commit();
                }
                if  (item.getItemId()==R.id.prof){
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,new ProfileFragment()).commit();
                }
                return true;
            }
        });

    }
}