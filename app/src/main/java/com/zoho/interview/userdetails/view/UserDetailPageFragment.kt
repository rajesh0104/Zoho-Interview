package com.zoho.interview.userdetails.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.zoho.interview.R
import com.zoho.interview.database.DatabaseHelper
import com.zoho.interview.database.entity.User
import com.zoho.interview.databinding.FragmentUserDetailPageBinding
import com.zoho.interview.helper.AppHelper
import com.zoho.interview.rest.pogo.ApiData
import com.zoho.interview.userdetails.viewmodel.UserDetailPageModelView

class UserDetailPageFragment : Fragment() {
    private lateinit var fragmentUserDetailPageBinding: FragmentUserDetailPageBinding
    private var userLocation: String = ""
    private var userId: String = ""
    private lateinit var userDetailPageModelView: UserDetailPageModelView

    companion object {
        @JvmStatic
        fun newInstance(userLocation: String, userId: String): Fragment? {
            val fragment = UserDetailPageFragment()
            val arguments = Bundle().apply {
                putString("LOCATION", userLocation)
                putString("USER_ID", userId)
            }
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentUserDetailPageBinding =
            FragmentUserDetailPageBinding.inflate(inflater, container, false)
        return fragmentUserDetailPageBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments.let {
            userLocation = it?.getString("LOCATION").toString()
            userId = it?.getString("USER_ID").toString()
        }
        userDetailPageModelView = UserDetailPageModelView(this)
        userDetailPageModelView.getUserWeatherDetails(userLocation)

        val userDetails: User = DatabaseHelper().getSelectedUserDetails(requireActivity(), userId)

        fragmentUserDetailPageBinding.tvAboutUser.text =
            "About " + " \"" + userDetails.userName + "\""
        fragmentUserDetailPageBinding.tvUserGender.text = userDetails.gender
        fragmentUserDetailPageBinding.tvUserLocation.text = userDetails.state
        fragmentUserDetailPageBinding.tvUserCountry.text = userDetails.country
        fragmentUserDetailPageBinding.tvUserPostCode.text = userDetails.postalCode
        fragmentUserDetailPageBinding.tvUserGps.text =
            userDetails.latitude + "," + userDetails.longitude
    }

    fun onUserWeatherDetailsUpdated(
        results: ApiData.ApiResults.Location?,
        current: ApiData.CurrentWeatherData?
    ) {
        try {
            fragmentUserDetailPageBinding.tvWeatherCelcius.text =
                current?.tempInCelsius + resources.getString(R.string.degree)

            fragmentUserDetailPageBinding.tvWeatherText.text = current?.climateCondition?.text

            val climateImageUrl = "https://" + current?.climateCondition?.icon?.replace("//", "")

            Glide.with(this)
                .asBitmap()
                .load(climateImageUrl)
                .thumbnail(0.1f)
                .into(fragmentUserDetailPageBinding.ivWeatherStatus)



            fragmentUserDetailPageBinding.tvHumidity.text = current?.humidity
            fragmentUserDetailPageBinding.tvAirPressure.text = current?.windPressure + " mb"
            fragmentUserDetailPageBinding.tvWindSpeed.text = current?.windSpeed + " mph"
            fragmentUserDetailPageBinding.tvAirCloud.text = current?.cloud.toString() + " oktas"
            fragmentUserDetailPageBinding.tvDateTime.text =
                "Date: ${AppHelper().getConvertedSimpleDateFormat(results?.localtime)}"
        } catch (e: Exception) {

        }
    }
}