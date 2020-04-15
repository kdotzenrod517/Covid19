package com.kdotz.covid19.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

object Model {

    @Parcelize
    data class CountryResponse(
        @SerializedName("provinceState") val provinceState : String,
        @SerializedName("countryRegion") val countryRegion : String?,
        @SerializedName("lastUpdate") val lastUpdate : Number?,
        @SerializedName("lat") val lat : Double?,
        @SerializedName("long") val long : Double?,
        @SerializedName("confirmed") val confirmed : Int?,
        @SerializedName("recovered") val recovered : Int?,
        @SerializedName("deaths") val deaths : Int?,
        @SerializedName("active") val active : Int,
        @SerializedName("admin2") val admin2 : String?,
        @SerializedName("fips") val fips : String?,
        @SerializedName("combinedKey") val combinedKey : String?,
        @SerializedName("iso2") val iso2 : String?,
        @SerializedName("iso3") val iso3 : String?
    ) : Parcelable
}