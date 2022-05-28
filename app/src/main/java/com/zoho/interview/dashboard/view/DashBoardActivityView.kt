package com.zoho.interview.dashboard.view

import android.Manifest
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.zoho.interview.R
import com.zoho.interview.dashboard.modelview.DashBoardModelView
import com.zoho.interview.database.DatabaseHelper
import com.zoho.interview.databinding.ActivityDashBoardBinding


class DashBoardActivityView : AppCompatActivity(), TextWatcher, LocationListener {

    private lateinit var dashBoardBinding: ActivityDashBoardBinding
    private lateinit var dashBoardModelView: DashBoardModelView
    private lateinit var adapter: DashBoardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dashBoardBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_dash_board
        )
        dashBoardModelView = DashBoardModelView(this)
        dashBoardBinding.rvUserList.setItemViewCacheSize(50)
        dashBoardBinding.etSearch.addTextChangedListener(this)
        dashBoardBinding.rvUserList.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter = DashBoardAdapter(
            this, ArrayList()
        )
        dashBoardBinding.rvUserList.adapter = adapter
        dashBoardModelView.getUserDetails(20, 1)
        getLocationPermission()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101 && resultCode == RESULT_OK) {
            getLocationPermission()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1001 && grantResults[0] == PackageManager.PERMISSION_GRANTED && permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION) {
            getLocationPermission()
        }
    }

    fun onUserDetailsUpdated() {
        val userList = DatabaseHelper().getCurrentUserDetails(this)
        adapter.updateData(userList)
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        val userList = DatabaseHelper().getCurrentUserSearchedDetails(this, p0.toString())
        adapter.updateSearchedData(userList)

        if (p0?.length == 0) {
            val allUserList = DatabaseHelper().getCurrentUserDetails(this)
            adapter.updateData(allUserList)
        }
        Log.d("TYPING_TEXT::", userList?.size.toString())
    }

    override fun afterTextChanged(p0: Editable?) {

    }


    private fun getLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient()
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                    ),
                    1001
                )
            }
        }
    }


    private fun buildGoogleApiClient() {
        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)

        val task = LocationServices.getSettingsClient(this)
            .checkLocationSettings(builder.build())

        task.addOnSuccessListener { response ->
            val states = response.locationSettingsStates
            if (states?.isLocationPresent == true) {
                val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    val location: Location? =
                        locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                    if (location != null) {
                        Log.d(
                            "CURRENT_LOCATION::",
                            location.latitude.toString() + "::" + location.longitude
                        )
                    } else {
                        locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            0,
                            0f,
                            this@DashBoardActivityView
                        )
                    }
                }
            }
        }
        task.addOnFailureListener { e ->
            if (e is ResolvableApiException) {
                try {
                    e.startResolutionForResult(
                        this,
                        101
                    )
                } catch (sendEx: IntentSender.SendIntentException) {
                }
            }
        }
    }

    override fun onLocationChanged(location: Location) {
        Log.d("CURRENT_LOCATION::", location.latitude.toString() + "::" + location.longitude)

    }
}