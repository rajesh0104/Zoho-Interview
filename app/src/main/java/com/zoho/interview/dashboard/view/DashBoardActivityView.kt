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
import android.util.Log
import android.view.Gravity
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.transition.Slide
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.zoho.interview.R
import com.zoho.interview.database.DatabaseHelper
import com.zoho.interview.databinding.ActivityDashBoardBinding
import com.zoho.interview.helper.AppHelper
import com.zoho.interview.userdetails.view.UserDetailPageFragment


class DashBoardActivityView : AppCompatActivity(), View.OnClickListener,
    LocationListener, AppHelper.OnClickFragment {

    private lateinit var dashBoardBinding: ActivityDashBoardBinding
    private lateinit var userListingFragment: UserListingFragment

    companion object {
        var currentUserAddressFromLocation = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dashBoardBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_dash_board
        )

        dashBoardBinding.clLocationUpdation.setOnClickListener(this)
        dashBoardBinding.ivProfilePicture.setOnClickListener(this)
        userListingFragment = UserListingFragment()
        addChildFragment(userListingFragment)
        getLocationPermission()

        Glide.with(this)
            .load(R.drawable.ic_my_self)
            .apply(RequestOptions.circleCropTransform())
            .thumbnail(0.1f)
            .into(dashBoardBinding.ivProfilePicture)
    }


    override fun onClick(view: View?) {
        if (view?.id == R.id.cl_location_updation) {
            getLocationPermission()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101 && resultCode == RESULT_OK) {
            getLocationPermission()
        } else if (requestCode == 101) {
            dashBoardBinding.tvUserLocation.text = "Click to update weather"
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fl_child_fragment)

        if (currentFragment is UserListingFragment) {
            dashBoardBinding.tvUserLocation.text =
                currentUserAddressFromLocation
            Glide.with(this)
                .load(R.drawable.ic_my_self)
                .apply(RequestOptions.circleCropTransform())
                .thumbnail(0.1f)
                .into(dashBoardBinding.ivProfilePicture)
        } else {
            finish()
        }
    }

    private fun addChildFragment(fragment: Fragment) {
        val manager: FragmentManager = supportFragmentManager
        val transaction: FragmentTransaction = manager.beginTransaction()
        val animFragment: Fragment
        if (fragment is UserListingFragment) {
            // No need any animation because this is the launcher fragment
            animFragment = fragment.apply {

            }
        } else {
            animFragment = fragment.apply {
                enterTransition = Slide(Gravity.END)
                exitTransition = Slide(Gravity.START)
            }
        }

        transaction.add(R.id.fl_child_fragment, animFragment, fragment::class.java.simpleName)
        transaction.addToBackStack(fragment::class.java.simpleName)
        transaction.commitAllowingStateLoss()
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
        dashBoardBinding.tvUserLocation.text = "Updating location details"
        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)

        val task = LocationServices.getSettingsClient(this)
            .checkLocationSettings(builder.build())

        task.addOnSuccessListener { response ->
            val states = response.locationSettingsStates
            if (states?.isLocationPresent == true) {
                val locationManager =
                    getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        0,
                        0f,
                        this@DashBoardActivityView
                    )
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
        userListingFragment.getUserWeatherDetails(
            location.latitude.toString() + "," + location.longitude,
            dashBoardBinding.tvUserLocation
        )
    }

    override fun onUserDetailPageClicked(userLocation: String, userId: String) {
        UserDetailPageFragment.newInstance(userLocation, userId)?.let { addChildFragment(it) }
        val selectedUserDetails = DatabaseHelper().getSelectedUserDetails(this, userId)
        dashBoardBinding.tvUserLocation.text =
            selectedUserDetails.state + "," + selectedUserDetails.country

        if (selectedUserDetails.medium?.isEmpty() == true) {
            Glide.with(this)
                .load(R.drawable.ic_my_self)
                .apply(RequestOptions.circleCropTransform())
                .thumbnail(0.1f)
                .into(dashBoardBinding.ivProfilePicture)
        } else {
            Glide.with(this)
                .load(selectedUserDetails.medium)
                .apply(RequestOptions.circleCropTransform())
                .thumbnail(0.1f)
                .into(dashBoardBinding.ivProfilePicture)
        }
    }
}