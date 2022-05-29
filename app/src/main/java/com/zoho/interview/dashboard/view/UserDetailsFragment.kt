package com.zoho.interview.dashboard.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.zoho.interview.R
import com.zoho.interview.dashboard.modelview.DashBoardModelView
import com.zoho.interview.database.DatabaseHelper
import com.zoho.interview.databinding.FragmentUserDetailsBinding
import com.zoho.interview.helper.AppHelper
import com.zoho.interview.rest.pogo.ApiData
import org.w3c.dom.Text

class UserDetailsFragment : Fragment(), TextWatcher {

    private lateinit var fragmentUserDetailsBinding: FragmentUserDetailsBinding
    private lateinit var dashBoardModelView: DashBoardModelView
    private lateinit var adapter: DashBoardAdapter
    private lateinit var locationTextView: TextView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentUserDetailsBinding = FragmentUserDetailsBinding.inflate(inflater, container, false)
        return fragmentUserDetailsBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dashBoardModelView = DashBoardModelView(this)
        fragmentUserDetailsBinding.rvUserList.setItemViewCacheSize(50)
        fragmentUserDetailsBinding.rvUserList.isNestedScrollingEnabled = false
        fragmentUserDetailsBinding.etSearch.addTextChangedListener(this)

        fragmentUserDetailsBinding.rvUserList.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        adapter = DashBoardAdapter(
            requireActivity(), ArrayList()
        )
        fragmentUserDetailsBinding.rvUserList.adapter = adapter
        DatabaseHelper().deleteUserDetails(requireActivity())
        dashBoardModelView.getUserDetails(10, 1)
    }


    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        val userList =
            DatabaseHelper().getCurrentUserSearchedDetails(requireActivity(), p0.toString())
        adapter.updateSearchedData(userList)

        if (p0?.length == 0) {
            val allUserList = DatabaseHelper().getCurrentUserDetails(requireActivity())
            adapter.updateData(allUserList)
        }
        Log.d("TYPING_TEXT::", userList?.size.toString())
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        // Not yet handled
    }

    override fun afterTextChanged(p0: Editable?) {
        // Not yet handled
    }


    fun onUserDetailsUpdated() {
        val userList = DatabaseHelper().getCurrentUserDetails(requireActivity())
        adapter.updateData(userList)
    }

    fun getUserWeatherDetails(location: String, tvUserLocation: TextView) {
        locationTextView = tvUserLocation
        dashBoardModelView.getUserWeatherDetails(location)
    }

    fun onUserWeatherDetailsUpdated(
        results: ApiData.ApiResults.Location?,
        current: ApiData.CurrentWeatherData?
    ) {

        fragmentUserDetailsBinding.tvDateTime.text =
            "Date: ${AppHelper().getConvertedSimpleDateFormat(results?.localtime)}"

        fragmentUserDetailsBinding.tvWeatherCelcius.text =
            current?.tempInCelsius + resources.getString(R.string.degree)

        fragmentUserDetailsBinding.tvDateTimeUpdated.text =
            "Last Updated on: " + AppHelper().getConvertedSimpleDateFormat(current?.lastUpdated)
        fragmentUserDetailsBinding.tvWeatherText.text = current?.climateCondition?.text
        locationTextView.text = results?.name + ", " + results?.region
        val climateImageUrl = "https://" + current?.climateCondition?.icon?.replace("//", "")
        Glide.with(this)
            .asBitmap()
            .load(climateImageUrl)
            .thumbnail(0.1f)
            .into(fragmentUserDetailsBinding.ivWeatherStatus)
        fragmentUserDetailsBinding.clLocationSection.visibility = View.VISIBLE
    }
}