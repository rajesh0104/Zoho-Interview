package com.zoho.interview.rest.pogo

import com.google.gson.annotations.SerializedName
import java.util.*

data class ApiData(
    var results: List<ApiResults>?,
    var location: ApiResults.Location,
    var current: CurrentWeatherData,
) {
    data class ApiResults(
        val name: Name?,
        val location: Location?,
        val email: String?,
        val login: Login?,
        val dob: Dob?,
        val phone: String?,
        val picture: Picture?,
    ) {

        data class Name(
            var title: String?,
            var first: String?,
            var last: String?
        )

        data class Location(
            var name: String?,
            var localtime: String?,
            var region: String?,
            var city: String?,
            var state: String?,
            var country: String?,
            var postcode: Any?,
            var coordinates: Coordinates?,
        ) {
            data class Coordinates(
                var latitude: String?,
                var longitude: String?
            )
        }

        data class Login(
            var uuid: String,
            var username: String
        )

        data class Dob(
            var date: String?,
            var age: Int?,
        )


        data class Picture(
            var medium: String?,
            var large: String?
        )
    }

    data class CurrentWeatherData(
        @SerializedName("temp_c")
        var tempInCelsius: String,

        @SerializedName("last_updated")
        var lastUpdated: String,

        @SerializedName("wind_kph")
        var windSpeedInKmp: String,

        @SerializedName("wind_dir")
        var windDirection: String,

        @SerializedName("condition")
        var climateCondition: Condition
    ) {
        data class Condition(
            var text: String,
            var icon: String
        )
    }
}
