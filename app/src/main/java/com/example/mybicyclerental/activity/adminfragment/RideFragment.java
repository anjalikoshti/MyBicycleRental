package com.example.mybicyclerental.activity.adminfragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybicyclerental.R;
import com.example.mybicyclerental.activity.Adminadapter.ActiveAdapter;
import com.example.mybicyclerental.model.BookingModel;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RideFragment extends Fragment {
    RecyclerView recyclerView;
    ImageView imageView;
    Button btnEnd;
    ActiveAdapter activeAdapter;
    List<BookingModel> list = new ArrayList<>();
    SharedPreferences preferences;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ride_admin, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView=view.findViewById(R.id.rv_active);
        imageView=view.findViewById(R.id.active_image);
        btnEnd=view.findViewById(R.id.btn_endride);
        preferences=getActivity().getSharedPreferences("booking", Context.MODE_PRIVATE);
        String currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());


        FirebaseFirestore.getInstance().collection("BOOKINGS").whereEqualTo("date",currentDate)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (value != null && !value.isEmpty()) {
                            BookingModel model = value.getDocuments().get(0).toObject(BookingModel.class);
                            list.add(model);
                            Log.e("list>>>>>>",list+"");
                        }
                        if (error != null) {
                            Toast.makeText(getActivity(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }


                        activeAdapter = new ActiveAdapter(view.getContext(),list);
                        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setAdapter(new ActiveAdapter(view.getContext(),list));


                    }

                });

    }
}
