package com.zoho.interview.helper

import java.text.SimpleDateFormat
import java.util.*

class AppHelper {

    fun getConvertedSimpleDateFormat(
        currentDate: String?
    ): String {
        var currentDateFormat = "yyyy-MM-dd HH:mm"
        var requiredDateFormat = "MMM dd, yyyy HH:mm"
        val inputFormat = SimpleDateFormat(currentDateFormat, Locale.getDefault())
        val outputFormat = SimpleDateFormat(requiredDateFormat, Locale.getDefault())
        val date: Date? = inputFormat.parse(currentDate.toString())
        return outputFormat.format(date)
    }
}