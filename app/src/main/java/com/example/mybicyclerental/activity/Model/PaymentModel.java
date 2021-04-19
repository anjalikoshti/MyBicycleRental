package com.example.mybicyclerental.activity.Model;

import com.example.mybicyclerental.model.BicycleModel;
import com.example.mybicyclerental.model.BookingModel;
import com.example.mybicyclerental.model.UserModel;

import java.io.Serializable;

public class PaymentModel implements Serializable {

    private String txnid;
    private String firstname;
    private  String amount,total;
    private  BookingModel bookingModel;
    private UserModel userModel;
    public String getTxnid() {
        return txnid;
    }

    public void setTxnid(String txnid) {
        this.txnid = txnid;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public BookingModel getBookingModel() {
        return bookingModel;
    }

    public UserModel getUserModel(){
        return userModel;
    }


    public void setBookingModel(BookingModel bookingModel) {
        this.bookingModel = bookingModel;
    }

    public void setUserModel(UserModel userModel){
        this.userModel = userModel;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
