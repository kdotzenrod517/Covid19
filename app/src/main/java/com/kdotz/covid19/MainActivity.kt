package com.kdotz.covid19

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import com.kdotz.covid19.model.Model
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    var countryResponse: ArrayList<Model.CountryResponse> = arrayListOf()

    var states: ArrayList<String> = arrayListOf()

    private val finalCountryResponse = arrayListOf<Model.CountryResponse>()

    private var countryMap: HashMap<String, Int> = hashMapOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getCurrentUSAConfirmed()

        val options = resources.getStringArray(R.array.Options)
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, options)

        listView.adapter = arrayAdapter

        listView.setOnItemClickListener { _, _, position: Int, id: Long ->

            if (countryMap.isNotEmpty()) {
                when (position) {
                    0 -> {
                        val intent = Intent(this, AllStatesActivity::class.java)
                        intent.putParcelableArrayListExtra("data", finalCountryResponse) // Be sure con is not null here
                        intent.putExtra("dataMap", countryMap)
                        startActivity(intent)
                    }
                    1 -> {
                        val intent = Intent(this, StateActivity::class.java)
                        states.sort()
                        intent.putExtra("states", states) // Be sure con is not null here
                        startActivity(intent)
                    }
                    else -> Toast.makeText(this, options[position].toString(), LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(this, "Loading...", LENGTH_LONG).show()
            }
        }
    }

    private fun getCurrentUSAConfirmed() {
        val retrofit = Retrofit.Builder()
            .baseUrl(BaseUrl)
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
                        countryMap.merge(it.provinceState, it.active, Integer::sum)
                    }

                    countryResponse.forEach {
                        if (it.provinceState !in states) {
                            states.add(it.provinceState)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<ArrayList<Model.CountryResponse>>, t: Throwable) {
                println("Fail $t")
            }
        })
    }

    companion object {
        var BaseUrl = "https://covid19.mathdro.id/api/"
    }
}
