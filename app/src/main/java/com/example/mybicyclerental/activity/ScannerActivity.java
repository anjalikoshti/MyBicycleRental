package com.example.mybicyclerental.activity;

import android.hardware.camera2.CameraDevice;
import android.os.Bundle;
import android.view.SurfaceView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mybicyclerental.R;

import java.security.CodeSource;

public class ScannerActivity extends AppCompatActivity {
    TextView barcodeinfo;
    SurfaceView surfaceView;
    CameraDevice cameraDevice;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        surfaceView=(SurfaceView)findViewById(R.id.camera_view);
        barcodeinfo=(TextView)findViewById(R.id.txtContent);



    }
}
