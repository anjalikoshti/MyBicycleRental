package com.example.mybicyclerental

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.mybicyclerental.activity.MapActivity
import com.example.mybicyclerental.utils.Consts
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import java.lang.UnsupportedOperationException

typealias polyline = MutableList<LatLng>
typealias polylines = MutableList<polyline>

class TrackerService : LifecycleService() {

    private val isFirstRun = true

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(resut: LocationResult?) {
            super.onLocationResult(resut)
            if (isTracking.value!!)
                if (resut != null && resut.lastLocation != null) {
                    for (location in resut.locations) {
                        addPathPoints(location)
                        Log.d(
                                TAG,
                                "onLocationResult: lat-${location.latitude} lng-${location.longitude}"
                        )
                    }

                }
        }
    }

    private fun postInitialValues() {
        isTracking.postValue(false)
        pathPoints.postValue(mutableListOf())
    }

    private fun addEmptyPolylines() = pathPoints.value?.apply {
        add(mutableListOf())
        pathPoints.postValue(this)
    } ?: pathPoints.postValue(mutableListOf(mutableListOf()))

    private fun addPathPoints(location: Location?) {
        location?.let {
            val pos = LatLng(location.latitude, location.longitude)
            pathPoints.value?.apply {
                last().add(pos)
                pathPoints.postValue(this)
            }
        }
    }

    private fun updateLocationTracking(isTracking: Boolean) {
        if (isTracking) {
            val locationRequest = LocationRequest().apply {
                interval = 5000
                fastestInterval = 2000
                priority = PRIORITY_HIGH_ACCURACY

            }

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }else {
                LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(
                        locationRequest, locationCallback,
                        Looper.getMainLooper()
                )
            }

        }
    }

    override fun onCreate() {
        super.onCreate()
        postInitialValues()

        isTracking.observe(this, Observer {
            updateLocationTracking(it)
        })
    }

    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
        throw UnsupportedOperationException("Not yet implemented")
    }

    private fun startLocationService() {

        addEmptyPolylines()
        isTracking.postValue(true)
        val channelId = "location_channel"
        val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val pendingIntent = PendingIntent.getActivity(
                this,
                0,
                Intent(this, MapActivity::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(this, "location_channel")
                .setContentTitle("Location Service")
                .setContentText("Running")
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setPriority(NotificationCompat.PRIORITY_HIGH)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                    channelId,
                    "Location Channel",
                    NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
//
//        val locationRequest = LocationRequest()
//        locationRequest.interval = 4000
//        locationRequest.fastestInterval = 2000
//        locationRequest.priority = PRIORITY_HIGH_ACCURACY
//
//        if (ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return
//        } else {
//            LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(
//                locationRequest, locationCallback,
//                Looper.getMainLooper()
//            )
//        }
        startForeground(150, notification.build())

    }

    private fun stopLocationService() {
        LocationServices.getFusedLocationProviderClient(this).removeLocationUpdates(
                locationCallback
        )
        stopForeground(true)
        stopSelf()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (it.action) {
                Consts.ACTION_START_OR_RESUME_SERVICE -> {
                    startLocationService()
                    Log.d(Companion.TAG, "Service started or resumed")
                }
                Consts.ACTION_PAUSE_SERVICE -> {
                    Log.d(Companion.TAG, "Service paused")
                }
                Consts.ACTION_STOP_SERVICE -> {
                    stopLocationService()
                }
                else -> return super.onStartCommand(intent, flags, startId)
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    companion object {
        private const val TAG = "TrackerService"
        private val isTracking = MutableLiveData<Boolean>()
        val pathPoints = MutableLiveData<polylines>()
    }
}