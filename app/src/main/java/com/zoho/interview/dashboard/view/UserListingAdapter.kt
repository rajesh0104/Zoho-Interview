package com.zoho.interview.dashboard.view

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zoho.interview.IPagination
import com.zoho.interview.R
import com.zoho.interview.database.entity.User
import com.zoho.interview.helper.AppHelper

class UserListingAdapter(
    private val activity: Activity,
    private val data: ArrayList<User>,
    private val onClickFragment: AppHelper.OnClickFragment?,
    private val onPagination: IPagination
) : RecyclerView.Adapter<UserListingHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserListingHolder {
        return UserListingHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.adapter_dashboard, parent, false)
        )
    }

    override fun onBindViewHolder(holder: UserListingHolder, position: Int) {
        holder.bind(activity, data[position])
        holder.itemView.setOnClickListener {
            onClickFragment?.onUserDetailPageClicked(
                data[position].latitude + "," + data[position].longitude,
                data[position].userId
            )
        }

        if (position == data.size - 1) {
            onPagination.onPaginate()
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun updateData(userList: List<User>?) {
        userList?.let { data.addAll(it) }
        userList?.size?.let { notifyItemRangeChanged(data.size, it) }
    }

    fun updateSearchedData(userList: List<User>?) {
        data.clear()
        userList?.let { data.addAll(it) }
        notifyDataSetChanged()
    }
}