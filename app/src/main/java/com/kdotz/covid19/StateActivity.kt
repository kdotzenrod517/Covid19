package com.kdotz.covid19

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.kdotz.covid19.model.Model
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class StateActivity : AppCompatActivity() {

    var countryResponse: ArrayList<Model.CountryResponse> = arrayListOf()

    var states: ArrayList<String> = arrayListOf()

    val finalCountryResponse = arrayListOf<Model.CountryResponse>()

    private var activeCountyMap: HashMap<String, Int> = hashMapOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_state)
        generateCountryMaps()
    }

    fun onClick(view: View) {
        val editText = findViewById<EditText>(R.id.editText)
        val activeTextView = findViewById<TextView>(R.id.active)

        if (activeCountyMap.isNotEmpty()) {
            activeCountyMap[editText.text.toString()]?.let {
                activeTextView.text = it.toString()
            } ?: run {
                Toast.makeText(this, " ${editText.text} does not have any data. Please try again.", Toast.LENGTH_LONG)
                    .show()
            }
        } else {
            Toast.makeText(this, "Loading...", Toast.LENGTH_LONG).show()
        }
    }

    private fun generateCountryMaps() {
        val retrofit = Retrofit.Builder()
            .baseUrl(MainActivity.BaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(CovidApiService::class.java)
        val call = service.getUSAConfirmed()

        call.enqueue(object : Callback<ArrayList<Model.CountryResponse>> {
            override fun onResponse(
                call: Call<ArrayList<Model.CountryResponse>>,
                response: Response<ArrayList<Model.CountryResponse>>
            ) {

                if (response.code() == 200) {
                    countryResponse = response.body()!!

//                    countryResponse.forEach { it ->
//                        if (!finalCountryResponse.stream().map { it.provinceState }.collect(Collectors.toList()).contains(
//                                it.provinceState
//                            )
//                        ) {
//                            finalCountryResponse.add(it)
//                        }
//                    }

                    countryResponse.forEach {
                        activeCountyMap.merge(it.provinceState, it.active, Integer::sum)
                    }

                    countryResponse.forEach {
                        println("State ${it.provinceState}")
                        if (!states.contains(it.provinceState)) {
                            it.provinceState?.let { it1 -> states.add(it1) }
                        }
                    }

                }
            }

            override fun onFailure(call: Call<ArrayList<Model.CountryResponse>>, t: Throwable) {
                println("Fail $t")
            }
        })
    }

}
