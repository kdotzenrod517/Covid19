package com.kdotz.covid19

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.*
import com.kdotz.covid19.model.Model
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.NumberFormat
import java.util.*


class StateActivity : AppCompatActivity() {

    var countryResponse: ArrayList<Model.CountryResponse> = arrayListOf()

    var states: ArrayList<String> = arrayListOf()

    private var activeCountyMap: HashMap<String, Int> = hashMapOf()

    lateinit var spinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_state)

        states = intent.getSerializableExtra("states") as ArrayList<String>
        states.add(0, "Select a State")
        spinner = findViewById(R.id.spinner)
        val arrayAdapter = object: ArrayAdapter<String>(this@StateActivity, android.R.layout.simple_spinner_dropdown_item, states){
            override fun isEnabled(position: Int): Boolean{
                return position != 0
            }
        }
        spinner.adapter = arrayAdapter

        generateCountryMaps()

        spinner.setSelection(0, false)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedItemText = parent.getItemAtPosition(position) as String
                val activeTextView = findViewById<TextView>(R.id.active)

                if (activeCountyMap.isNotEmpty()) {
                    activeCountyMap[selectedItemText]?.let {
                        activeTextView.text = NumberFormat.getNumberInstance(Locale.US).format(it)
                    } ?: run {
                        if (position > 0) {
                            Toast.makeText(
                                applicationContext,
                                " $selectedItemText does not have any data. Please try again.",
                                Toast.LENGTH_LONG
                            )
                                .show()
                        } else {
                            activeTextView.text = "Select a State"
                        }
                    }
                } else {
                    Toast.makeText(applicationContext, "Loading...", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
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

                    countryResponse.forEach {
                        activeCountyMap.merge(it.provinceState, it.active, Integer::sum)
                    }
                }
            }

            override fun onFailure(call: Call<ArrayList<Model.CountryResponse>>, t: Throwable) {
                println("Fail $t")
            }
        })
    }

}
