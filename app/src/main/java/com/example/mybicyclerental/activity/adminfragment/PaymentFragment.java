package com.example.mybicyclerental.activity.adminfragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.mybicyclerental.R;
import com.example.mybicyclerental.activity.Adminadapter.ActiveAdapter;
import com.example.mybicyclerental.activity.Adminadapter.PaymentAdapter;
import com.example.mybicyclerental.model.BookingModel;

import java.util.ArrayList;
import java.util.List;

public class PaymentFragment extends Fragment {
    RecyclerView rvpayemnt;
    PaymentAdapter paymentAdapter;
    List<BookingModel>list =new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment_admin, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvpayemnt=view.findViewById(R.id.rv_pay);

        paymentAdapter= new PaymentAdapter(view.getContext(),list);
        rvpayemnt.setLayoutManager(new LinearLayoutManager(view.getContext()));
        rvpayemnt.setHasFixedSize(true);
        rvpayemnt.setAdapter(new PaymentAdapter(view.getContext(),list));


    }
}
