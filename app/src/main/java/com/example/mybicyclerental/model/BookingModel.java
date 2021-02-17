package com.example.mybicyclerental.model;

import java.io.Serializable;

public class BookingModel implements Serializable {
    private String userEmail;
    private String hour;
    private String days;
    private String date;
    private String time;
    private String price;


    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    private String total;

    public BookingModel() {
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    private BicycleModel bicycleModel;

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public BicycleModel getBicycleModel() {
        return bicycleModel;
    }

    public void setBicycleModel(BicycleModel bicycleModel) {
        this.bicycleModel = bicycleModel;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }


}


