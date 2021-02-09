package com.example.mybicyclerental.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import com.example.mybicyclerental.R;
import com.example.mybicyclerental.adapter.ChooseBicycleAdapter;
import com.example.mybicyclerental.adapter.OnBicycleSelectedListener;
import com.example.mybicyclerental.model.BicycleModel;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ChooseBicycleActivity<android> extends AppCompatActivity {

    BicycleModel bicycleModel;
    List<BicycleModel> list = new ArrayList<>();
    RecyclerView recyclerView;
    int image[] = {R.mipmap.bicycle, R.mipmap.bicycle12, R.mipmap.bicycle123, R.mipmap.bicycle1234, R.mipmap.bicycle12345, R.mipmap.bicycle123456, R.mipmap.bicycle1234567, R.mipmap.bicycle12345678};
    String name[] = {"Cyclocross", "Randonneur", "Gravel", "Road", "Fatbike", "Trekking", "Mountainbike"};
    int id[] = {1, 2, 3, 4, 5, 6, 7};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_bicycle);
        recyclerView = findViewById(R.id.rv);


        FirebaseFirestore.getInstance().collection("BICYCLE").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null && !value.isEmpty()) {
                    for (int i = 0; i < value.size(); i++) {
                        bicycleModel = value.getDocuments().get(i).toObject(BicycleModel.class);
                        list.add(bicycleModel);
                    }

                    ChooseBicycleAdapter recyclerAdapter = new ChooseBicycleAdapter(ChooseBicycleActivity.this, list, new OnBicycleSelectedListener() {
                        @Override
                        public void getSelectedBicycle(BicycleModel s) {
                            Intent intent = new Intent();
                            intent.putExtra("bicycleName",  s.getBicycleName());
                            intent.putExtra("bicycleImage",s.getBicycleImage());
                            intent.putExtra("bicycleID",s.getBicycleID());
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    });
                    recyclerView.setAdapter(recyclerAdapter);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ChooseBicycleActivity.this);
                    recyclerView.setLayoutManager(linearLayoutManager);

                }
                if (error != null) {
                }
            }
        });


    }

}