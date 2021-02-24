package com.example.mybicyclerental.activity

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.mybicyclerental.R
import com.example.mybicyclerental.TrackerService
import com.example.mybicyclerental.databinding.ActivityMapBinding
import com.example.mybicyclerental.polyline
import com.example.mybicyclerental.utils.Consts
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint

class MapActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var viewBinding: ActivityMapBinding
    private var pathPoints = mutableListOf<polyline>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        checkLocationPermission()
    }

    private fun checkLocationPermission() {
        if (isPermissionGranted()) {
            initMap()
        } else {
            requestPermissions()
        }
    }

    private fun requestPermissions() {
        if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION) || shouldShowRequestPermissionRationale(
                        ACCESS_COARSE_LOCATION
                )
        ) {
            AlertDialog.Builder(this)
                    .setMessage("Grant Location Permission to access Map")
                    .setPositiveButton("Okay") { dialog, which ->
                        ActivityCompat.requestPermissions(
                                this,
                                arrayOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION),
                                PERMISSION_REQUEST_CODE
                        )
                    }
                    .create().show()
        } else
            ActivityCompat.requestPermissions(
                    this,
                    arrayOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION),
                    PERMISSION_REQUEST_CODE
            )
    }

    private fun isPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
                applicationContext,
                ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(
                applicationContext,
                ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }


    private fun initMap() {
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // sendCommandToService(Consts.ACTION_START_OR_RESUME_SERVICE)




        viewBinding.btnStart1.setOnClickListener {
            startService(
                    Intent(
                            applicationContext,
                            TrackerService::class.java
                    ).setAction(Consts.ACTION_START_OR_RESUME_SERVICE)
            )
            Toast.makeText(this, "Location Service Started", Toast.LENGTH_SHORT).show()
        }

        viewBinding.btnStop.setOnClickListener {

            startService(
                    Intent(
                            applicationContext,
                            TrackerService::class.java
                    ).setAction(Consts.ACTION_STOP_SERVICE)
            )
            Toast.makeText(this, "Location Service Stopped", Toast.LENGTH_SHORT).show()
        }
    }


    private fun sendCommandToService(action: String) {
        startService(Intent(this, TrackerService::class.java).setAction(action))
    }

    private fun addLatestPolyline() {
        if (pathPoints.isNotEmpty() && pathPoints.last().size > 1) {
            val preLastLatLng = pathPoints.last()[pathPoints.last().size - 2]
            val lastLatLng = pathPoints.last().last()
            val polylineOptions = PolylineOptions()
                    .color(getColor(R.color.blue))
                    .width(10f)
                    .add(preLastLatLng)
                    .add(lastLatLng)
            map.addPolyline(polylineOptions)
        }
    }

    private fun addAllPolylines() {
        for (polyline in pathPoints) {
            val polylineOptions = PolylineOptions()
                    .color(getColor(R.color.blue))
                    .width(5f)
                    .addAll(polyline)
            map.addPolyline(polylineOptions)
        }
    }

    private fun moveCameraToUser() {
        if (pathPoints.isNotEmpty() && pathPoints.last().isNotEmpty()) {
            map.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                            pathPoints.last().last(),
                            DEFAULT_ZOOM
                    )
            )
        }
    }


    private fun geoLocate() {
        val geocoder = Geocoder(this)
        var addressList = ArrayList<Address>()

        if (addressList.isNotEmpty()) {
            val address = addressList[0]
            Log.d(TAG, "geoLocate:${address}")
            moveCamera(
                    LatLng(address.latitude, address.longitude),
                    DEFAULT_ZOOM, address.locality
            )
        }
    }

    private fun getCurrentLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                        this,
                        ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions()
        } else {
            val location = fusedLocationClient.lastLocation
            location.addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d(TAG, "onComplete: found location!")
                    val currentLocation =
                            it.result as Location

                    moveCamera(
                            LatLng(currentLocation.latitude, currentLocation.longitude),
                            DEFAULT_ZOOM, "My Location"
                    )


                }
            }
        }

    }



    private fun moveCamera(latLng: LatLng, zoom: Float, title: String) {
        Log.d(
                TAG,
                "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude
        )
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom))

        val markerOptions = MarkerOptions().position(latLng).title(title)
        map.addMarker(markerOptions)
    }

    override fun onMapReady(googleMap: GoogleMap?) {

        if (googleMap != null) {
            map = googleMap
            // getCurrentLocation()
            TrackerService.pathPoints.observe(this, Observer {
                pathPoints = it
                addLatestPolyline()
                addAllPolylines()
                moveCameraToUser()
            })
        }

        if (ActivityCompat.checkSelfPermission(
                        this,
                        ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        map.isMyLocationEnabled = true
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty()) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permissions Granted", Toast.LENGTH_SHORT).show()
            } else {
                requestPermissions()
            }
        }
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 200
        private const val TAG = "MapActivity"
        private const val DEFAULT_ZOOM = 15f

    }
}