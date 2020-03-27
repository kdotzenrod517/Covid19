package com.kdotz.covid19

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.anychart.anychart.AnyChart
import com.anychart.anychart.AnyChartView
import com.anychart.anychart.DataEntry
import com.anychart.anychart.ValueDataEntry
import com.kdotz.covid19.model.Model

class AllStatesActivity : AppCompatActivity() {

    val data = ArrayList<DataEntry>()

    private var pie = AnyChart.pie()

    private lateinit var anyChartView: AnyChartView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_states)

        val countryResponse : ArrayList<Model.CountryResponse> = intent.extras.getParcelableArrayList("data")

        pie = AnyChart.pie()

        countryResponse.forEach{
                data.add(ValueDataEntry(it.provinceState, it.active))
        }

        pie.setData(data)

        anyChartView = findViewById<AnyChartView>(R.id.any_chart_view)
        anyChartView.setChart(pie)

        anyChartView.setChart(pie)

    }



}
