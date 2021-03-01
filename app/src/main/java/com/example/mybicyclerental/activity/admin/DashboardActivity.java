package com.example.mybicyclerental.activity.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.mybicyclerental.R;
import com.example.mybicyclerental.activity.adminfragment.BookingAdminFragment;
import com.example.mybicyclerental.activity.adminfragment.PaymentFragment;
import com.example.mybicyclerental.activity.adminfragment.RideFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DashboardActivity extends AppCompatActivity {

    FrameLayout container1;
    BottomNavigationView bottomNav1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        container1=findViewById(R.id.container1);

        bottomNav1=findViewById(R.id.bottom_nav1);

        bottomNav1.setSelectedItemId(R.id.activeride);



        getSupportFragmentManager().beginTransaction().replace(R.id.container1,new RideFragment()).commit();

        bottomNav1.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId()==R.id.activeride){
                    getSupportFragmentManager().beginTransaction().replace(R.id.container1,new RideFragment ()).commit();
                }
                if (item.getItemId()==R.id.bookadmin){
                    getSupportFragmentManager().beginTransaction().replace(R.id.container1,new BookingAdminFragment()).commit();
                }

                if  (item.getItemId()==R.id.pay){
                    getSupportFragmentManager().beginTransaction().replace(R.id.container1,new PaymentFragment()).commit();
                }
                return true;
            }
        });

    }
}

