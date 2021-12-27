package com.example.projektandroid1.kaloriefragments

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import com.example.projektandroid1.JEBANEDATY
import com.example.projektandroid1.KalorieRecyclerViewAdapter
import com.example.projektandroid1.R
import com.example.projektandroid1.data.Kalorie
import com.example.projektandroid1.data.ProjektAndroid1Database
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.IValueFormatter
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import kotlinx.android.synthetic.main.fragment_dodaj_posilek.*
import kotlinx.android.synthetic.main.fragment_statystyki.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class StatystykiFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_statystyki, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        CoroutineScope(Dispatchers.IO).launch {
            setBarChartValues()
        }
    }

    //utworzenie danych dla wykresu
    suspend fun setBarChartValues()
    {
        var fromDates = ArrayList<Date>()
        var toDates = ArrayList<Date>()
        var kalorieList = ArrayList<Int>()
        var barList= ArrayList<BarEntry>()
        for( i in 7 downTo 1)
        {
            val fromDate = JEBANEDATY.getDaysAgoDateWithoutTime(i)
            val toDate=JEBANEDATY.getDaysAgoDateWithoutTime(i-1)
            fromDates.add(fromDate)
            toDates.add(toDate)

            val kalorie = ProjektAndroid1Database.get(requireActivity().application).getKalorieDao()
                    .getKalorieSumByDate(fromDate, toDate)?: 0
            kalorieList.add(kalorie!!)

        }

        for(i in 0..6)
        {
            barList.add(BarEntry(i.toFloat(), kalorieList[i].toFloat()))
        }

        class xdateFormatter:IndexAxisValueFormatter(){
            val dateFormat = SimpleDateFormat("dd-MM")
            override fun getFormattedValue(value: Float): String {
                return dateFormat.format(fromDates[value.toInt()]).toString()
            }
        }

        withContext(Dispatchers.Main) {
            val textColor=requireContext().resolveColorAttr(android.R.attr.textColorPrimary)
            val barDataSet = BarDataSet(barList, "Ostatni tydzień")
            barDataSet.valueTextSize=12f
            barDataSet.valueTextColor=textColor
            val barData = BarData(barDataSet)


            kalorie_week_bar_chart.data = barData

            kalorie_week_bar_chart.axisRight.isEnabled=false
            kalorie_week_bar_chart.axisLeft.isEnabled=false
            kalorie_week_bar_chart.axisLeft.setDrawGridLines(false)
            kalorie_week_bar_chart.axisLeft.setDrawAxisLine(false)
            kalorie_week_bar_chart.axisLeft.textSize=12f
            kalorie_week_bar_chart.axisLeft.textColor=textColor
            kalorie_week_bar_chart.axisLeft.zeroLineWidth=0f
            kalorie_week_bar_chart.axisLeft.axisMinimum=0f
            kalorie_week_bar_chart.xAxis.valueFormatter=xdateFormatter()
            kalorie_week_bar_chart.xAxis.setDrawGridLines(false)
            kalorie_week_bar_chart.xAxis.position=XAxis.XAxisPosition.BOTTOM
            kalorie_week_bar_chart.xAxis.textColor=textColor
            kalorie_week_bar_chart.legend.isEnabled=false
            kalorie_week_bar_chart.xAxis.textSize=12f
            kalorie_week_bar_chart.description.isEnabled=false
            kalorie_week_bar_chart.animateY(1500)
            kalorie_week_bar_chart.description.textColor=textColor

        }


    }


    //Funkcje do znalezienia koloru z motywu
    @ColorInt
    fun Context.resolveColorAttr(@AttrRes colorAttr: Int): Int {
        val resolvedAttr = resolveThemeAttr(colorAttr)
        // resourceId is used if it's a ColorStateList, and data if it's a color reference or a hex color
        val colorRes = if (resolvedAttr.resourceId != 0) resolvedAttr.resourceId else resolvedAttr.data
        return ContextCompat.getColor(this, colorRes)
    }

    fun Context.resolveThemeAttr(@AttrRes attrRes: Int): TypedValue {
        val typedValue = TypedValue()
        theme.resolveAttribute(attrRes, typedValue, true)
        return typedValue
    }

}