package com.zoho.interview.dashboard.view

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.zoho.interview.R
import com.zoho.interview.database.entity.User

class UserListingHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private var mProfilePicture: ImageView? = null
    private var mUserName: TextView? = null
    private var mUserMailId: TextView? = null
    private var mUserPhoneNumber: TextView? = null
    private var mUserLocation: TextView? = null

    init {
        mProfilePicture = itemView.findViewById(R.id.iv_profile_picture)
        mUserName = itemView.findViewById(R.id.tv_title)
        mUserMailId = itemView.findViewById(R.id.tv_mail_id)
        mUserPhoneNumber = itemView.findViewById(R.id.tv_phone_no)
        mUserLocation = itemView.findViewById(R.id.tv_state_country)
    }

    fun bind(activity: Activity, user: User) {
        mUserName?.text =user.userName
        mUserMailId?.text = user.mailId
        mUserPhoneNumber?.text = user.mobileNumber
        mUserLocation?.text = user.state + ", " + user.country
        glideLoadCircularImage(activity, user.medium)
    }

    fun glideLoadCircularImage(
        context: Activity,
        imageUrl: String?
    ) {
        mProfilePicture?.let {
            Glide.with(context)
                .load(imageUrl)
                .apply(RequestOptions.circleCropTransform())
                .thumbnail(0.1f)
                .placeholder(R.drawable.ic_profile_pic)
                .error(R.drawable.ic_profile_pic)
                .into(it)
        }
    }
}