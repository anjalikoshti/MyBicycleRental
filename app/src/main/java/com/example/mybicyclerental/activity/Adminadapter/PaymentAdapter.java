package com.example.mybicyclerental.activity.Adminadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybicyclerental.R;
import com.example.mybicyclerental.activity.Model.PaymentModel;
import com.example.mybicyclerental.model.BookingModel;

import java.util.List;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.PaymentViewHolder> {
    Context context;
    List<PaymentModel> list;
    List<BookingModel> list1;

    public PaymentAdapter(Context context, List<BookingModel> list) {
        this.context = context;
        this.list1=list;
    }
    @NonNull
    @Override
    public PaymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View payment= LayoutInflater.from(context).inflate(R.layout.payment_item_view,parent,false);
        return new PaymentAdapter.PaymentViewHolder(payment);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentViewHolder holder, int position) {
     holder.txnid.setText(list.get(position).getTxnid());
    // holder.amount.setText(list1.get(position).getTotal());
     //holder.tfname.setText(list.get(position).getna);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class PaymentViewHolder extends RecyclerView.ViewHolder {
        TextView txnid,tfname,amount;
        public PaymentViewHolder(@NonNull View itemView) {
            super(itemView);
            txnid=itemView.findViewById(R.id.pay_txid);
            tfname=itemView.findViewById(R.id.pay_fname);
            amount=itemView.findViewById(R.id.pay_amount);
        }
    }
}
