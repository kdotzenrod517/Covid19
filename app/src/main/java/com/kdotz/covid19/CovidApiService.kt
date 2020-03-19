package com.kdotz.covid19

import com.kdotz.covid19.model.Model
import retrofit2.Call
import retrofit2.http.GET

interface CovidApiService {

    @GET("countries/USA/confirmed")
    fun getUSAConfirmed(): Call<ArrayList<Model.CountryResponse>>
}