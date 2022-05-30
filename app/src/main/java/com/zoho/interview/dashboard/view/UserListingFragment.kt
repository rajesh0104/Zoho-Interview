package com.zoho.interview.dashboard.view

import android.content.Context
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
import com.zoho.interview.dashboard.view.DashBoardActivityView.Companion.currentUserAddressFromLocation
import com.zoho.interview.database.DatabaseHelper
import com.zoho.interview.database.entity.User
import com.zoho.interview.databinding.FragmentUserListBinding
import com.zoho.interview.helper.AppHelper
import com.zoho.interview.rest.pogo.ApiData

class UserListingFragment : Fragment(), TextWatcher, View.OnClickListener {

    private lateinit var fragmentUserListBinding: FragmentUserListBinding
    private lateinit var dashBoardModelView: DashBoardModelView
    private lateinit var adapter: DashBoardAdapter
    private lateinit var locationTextView: TextView
    private var onClickFragment: AppHelper.OnClickFragment? = null
    private var userLocation: String = ""


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentUserListBinding = FragmentUserListBinding.inflate(inflater, container, false)
        return fragmentUserListBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dashBoardModelView = DashBoardModelView(this)
        fragmentUserListBinding.rvUserList.setItemViewCacheSize(50)
        fragmentUserListBinding.rvUserList.isNestedScrollingEnabled = false
        fragmentUserListBinding.etSearch.addTextChangedListener(this)
        fragmentUserListBinding.clLocationSection.setOnClickListener(this)
        fragmentUserListBinding.ivSearch.setOnClickListener(this)
        fragmentUserListBinding.rvUserList.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        adapter = DashBoardAdapter(
            requireActivity(), ArrayList(), onClickFragment
        )
        fragmentUserListBinding.rvUserList.adapter = adapter
        DatabaseHelper().deleteUserDetails(requireActivity())
        val userDetails: User = User(
            "rajesh0104",
            "Male",
            "Rajesh Kumar",
            10,
            "rajeshkumar.a2218@gmail.com",
            "8667378900",
            "600126",
            "",
            "",
            "Tamil Nadu",
            "Chennai",
            "12.8782909",
            "80.1688473", 0
        )
        DatabaseHelper().storeUserDetails(requireActivity(), userDetails)
        dashBoardModelView.getUserDetails(10, 1)
    }

    override fun onClick(view: View?) {
        if (view?.id == R.id.cl_location_section) {
            onClickFragment?.onUserDetailPageClicked(userLocation, "rajesh0104")
        } else if (view?.id == R.id.iv_search) {
            fragmentUserListBinding.etSearch.setText("")
        }
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        val userList =
            DatabaseHelper().getCurrentUserSearchedDetails(requireActivity(), p0.toString())
        adapter.updateSearchedData(userList)

        if (p0?.length == 0) {
            val allUserList = DatabaseHelper().getCurrentUserDetails(requireActivity())
            adapter.updateData(allUserList)
            fragmentUserListBinding.ivSearch.setImageDrawable(resources.getDrawable(android.R.drawable.ic_menu_search))
        } else {
            fragmentUserListBinding.ivSearch.setImageDrawable(resources.getDrawable(android.R.drawable.ic_menu_close_clear_cancel))
        }
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        // Not yet handled
    }

    override fun afterTextChanged(p0: Editable?) {
        // Not yet handled
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            onClickFragment = activity as AppHelper.OnClickFragment
        } catch (castException: Exception) {

        }

    }


    fun onUserDetailsUpdated() {
        val userList = DatabaseHelper().getCurrentUserDetails(requireActivity())
        adapter.updateData(userList)
    }

    fun getUserWeatherDetails(location: String, tvUserLocation: TextView) {
        locationTextView = tvUserLocation
        userLocation = location
        dashBoardModelView.getUserWeatherDetails(location)
    }

    fun onUserWeatherDetailsUpdated(
        results: ApiData.ApiResults.Location?,
        current: ApiData.CurrentWeatherData?
    ) {

        try {
            fragmentUserListBinding.tvWeatherCelcius.text =
                current?.tempInCelsius + resources.getString(R.string.degree)

            fragmentUserListBinding.tvDateTimeUpdated.text =
                "Last Updated on: " + AppHelper().getConvertedSimpleDateFormat(current?.lastUpdated)
            fragmentUserListBinding.tvWeatherText.text = current?.climateCondition?.text
            locationTextView.text = results?.name + ", " + results?.region
            currentUserAddressFromLocation = results?.name + ", " + results?.region
            val climateImageUrl = "https://" + current?.climateCondition?.icon?.replace("//", "")
            Glide.with(this)
                .asBitmap()
                .load(climateImageUrl)
                .thumbnail(0.1f)
                .into(fragmentUserListBinding.ivWeatherStatus)
            fragmentUserListBinding.clLocationSection.visibility = View.VISIBLE

            fragmentUserListBinding.tvDateTime.text =
                "Date: ${AppHelper().getConvertedSimpleDateFormat(results?.localtime)}"
        } catch (e: Exception) {

        }
    }
}