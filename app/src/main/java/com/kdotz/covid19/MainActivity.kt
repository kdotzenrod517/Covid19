package com.kdotz.covid19

import android.app.AlertDialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.anychart.anychart.AnyChart
import com.anychart.anychart.AnyChartView
import com.anychart.anychart.DataEntry
import com.anychart.anychart.ValueDataEntry
import com.kdotz.covid19.model.Model
import io.reactivex.disposables.Disposable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private var disposable: Disposable? = null

    lateinit var covidApiService: CovidApiService

    var countryResponse: ArrayList<Model.CountryResponse> = arrayListOf()

    lateinit var alertDialog: AlertDialog

    val data = ArrayList<DataEntry>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getCurrentUSAConfirmed()

        // Set up progress before call
        //AlertDialog
        // show it

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
                        println("State ${it.provinceState}")
                    }

                    populateCountryResponseChart(countryResponse)
                }
            }

            override fun onFailure(call: Call<ArrayList<Model.CountryResponse>>, t: Throwable) {
                println("Fail $t")
            }
        })
    }

    fun populateCountryResponseChart(countryResponse: ArrayList<Model.CountryResponse>){

        val pie = AnyChart.pie()

        countryResponse.forEach{
            data.add(ValueDataEntry(it.provinceState, it.active))
        }
//        data.add(ValueDataEntry("John", 10000))
//        data.add(ValueDataEntry("Jake", 12000))
//        data.add(ValueDataEntry("Peter", 18000))
        pie.setData(data)

        val anyChartView = findViewById<AnyChartView>(R.id.any_chart_view)
        anyChartView.setChart(pie)
    }

    companion object {
        var BaseUrl = "https://covid19.mathdro.id/api/"
    }

}
