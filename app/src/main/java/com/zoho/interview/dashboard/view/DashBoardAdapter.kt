package com.zoho.interview.dashboard.view

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zoho.interview.R
import com.zoho.interview.database.entity.User

class DashBoardAdapter(
    private val activity: Activity, private val data: ArrayList<User>
) : RecyclerView.Adapter<DashBoardHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashBoardHolder {
        return DashBoardHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.adapter_dashboard, parent, false)
        )
    }

    override fun onBindViewHolder(holder: DashBoardHolder, position: Int) {
        holder.bind(activity, data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun updateData(userList: List<User>?) {
        userList?.let { data.addAll(it) }
        notifyDataSetChanged()
    }

    fun updateSearchedData(userList: List<User>?) {
        data.clear()
        userList?.let { data.addAll(it) }
        notifyDataSetChanged()
    }
}