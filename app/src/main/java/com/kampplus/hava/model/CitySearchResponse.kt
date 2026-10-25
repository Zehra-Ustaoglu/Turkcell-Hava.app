package com.kampplus.hava.model

import com.google.gson.annotations.SerializedName

data class CitySearchResponse(
    val results: List<ApiCity>?
)

data class ApiCity(
    val id: Long,
    val name: String,
    val latitude: Double,
    val longitude: Double,

    @SerializedName("country_code")  // json country_code ve kotlin countryCode alanı bağlanır
    val countryCode: String?,

    val country: String?,
    val admin1: String?
) {
    val subtitle: String
        get() = listOfNotNull(admin1, country)
            .distinct()
            .joinToString(", ")
}
