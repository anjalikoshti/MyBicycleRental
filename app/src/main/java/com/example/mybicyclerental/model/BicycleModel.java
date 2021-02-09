package com.example.mybicyclerental.model;

import android.app.Activity;
import android.view.View;

import java.io.Serializable;

public class BicycleModel implements Serializable {
    private String bicycleID, bicycleName, bicycleImage;

    public String getBicycleID() {
        return bicycleID;
    }

    public void setBicycleID(String bicycleID) {
        this.bicycleID = bicycleID;
    }

    public String getBicycleName() {
        return bicycleName;
    }

    public void setBicycleName(String bicycleName) {
        this.bicycleName = bicycleName;
    }

    public String getBicycleImage() {
        return bicycleImage;
    }

    public void setBicycleImage(String bicycleImage) {
        this.bicycleImage = bicycleImage;
    }


}
