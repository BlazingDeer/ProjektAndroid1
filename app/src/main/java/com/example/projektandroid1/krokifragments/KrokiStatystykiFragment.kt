package com.example.projektandroid1.krokifragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import com.example.projektandroid1.Daty
import com.example.projektandroid1.LicznikKrokowActivity
import com.example.projektandroid1.R
import com.example.projektandroid1.data.ProjektAndroid1Database
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import kotlinx.android.synthetic.main.fragment_kroki_statystyki.*
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class KrokiStatystykiFragment : Fragment() {

    lateinit var myActivity: LicznikKrokowActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_kroki_statystyki, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        myActivity=(requireActivity() as LicznikKrokowActivity)

        CoroutineScope(Dispatchers.IO).launch {
            setBarChartValues()
            updateStatystykiTextFields()
        }
    }

    //utworzenie danych dla wykresu
    suspend fun setBarChartValues() {
        var fromDates = ArrayList<Date>()
        var toDates = ArrayList<Date>()
        var krokiList = ArrayList<Int>()
        var barList= ArrayList<BarEntry>()
        for( i in 7 downTo 1)
        {
            val fromDate = Daty.getDaysAgoDateWithoutTime(i)
            val toDate= Daty.getDaysAgoDateWithoutTime(i-1)
            fromDates.add(fromDate)
            toDates.add(toDate)

            val kroki = ProjektAndroid1Database.get(requireActivity().application).getKrokiDao()
                .getKrokiMaxByDate(fromDate, toDate)?: 0
            krokiList.add(kroki!!)

        }

        for(i in 0..6)
        {
            barList.add(BarEntry(i.toFloat(), krokiList[i].toFloat()))
        }

        class xdateFormatter: IndexAxisValueFormatter(){
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


            kroki_week_bar_chart.data = barData

            kroki_week_bar_chart.axisRight.isEnabled=false
            kroki_week_bar_chart.axisLeft.isEnabled=false
            kroki_week_bar_chart.axisLeft.setDrawGridLines(false)
            kroki_week_bar_chart.axisLeft.setDrawAxisLine(false)
            kroki_week_bar_chart.axisLeft.textSize=12f
            kroki_week_bar_chart.axisLeft.textColor=textColor
            kroki_week_bar_chart.axisLeft.zeroLineWidth=0f
            kroki_week_bar_chart.axisLeft.axisMinimum=0f
            kroki_week_bar_chart.xAxis.valueFormatter=xdateFormatter()
            kroki_week_bar_chart.xAxis.setDrawGridLines(false)
            kroki_week_bar_chart.xAxis.position= XAxis.XAxisPosition.BOTTOM
            kroki_week_bar_chart.xAxis.textColor=textColor
            kroki_week_bar_chart.legend.isEnabled=false
            kroki_week_bar_chart.xAxis.textSize=12f
            kroki_week_bar_chart.description.isEnabled=false
            kroki_week_bar_chart.animateY(1500)
            kroki_week_bar_chart.description.textColor=textColor
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

    suspend fun setSredniaIloscKrokowDziennie(okresCzasu: String) {
        var suma_krokow: Int=0
        var dzielnik: Int=0
        var srednia: Float=0f
        when(okresCzasu)
        {
            "tydzien" -> {
                dzielnik=6
            }

            "miesiac" -> {
                dzielnik=29
            }
        }

        for( i in dzielnik downTo 0) {
            val fromDate = Daty.getDaysAgoDateWithoutTime(i)
            val toDate=Daty.getDaysAgoDateWithoutTime(i-1)

            suma_krokow += myActivity.krokiDao.getKrokiMaxByDate(fromDate,toDate)?: 0
        }

        srednia=suma_krokow.toFloat()/dzielnik.toFloat()

        when(okresCzasu)
        {
            "tydzien" -> {
                withContext(Dispatchers.Main) {
                    srednia_krokow_tydzien.text = "$srednia"
                }
                Log.d("Srednia tygodniowa z BD:", srednia.toString())
            }

            "miesiac" -> {
                withContext(Dispatchers.Main) {
                    srednia_krokow_miesiac.text="$srednia"
                }
                Log.d("Srednia miesieczna z BD:", srednia.toString())
            }
        }
    }

    suspend fun setMaxKroki(okresCzasu: String){
        var indeks = 0
        var max_krokow:Int = 0
        when(okresCzasu)
        {
            "tydzien" -> {
                indeks=6
            }

            "miesiac" -> {
                indeks=29
            }
        }
        val fromDate = Daty.getDaysAgoDateWithoutTime(indeks)
        val toDate=Daty.getDaysAgoDateWithoutTime(-1)
        max_krokow = myActivity.krokiDao.getKrokiMaxByDate(fromDate,toDate)?: 0

        when(okresCzasu)
        {
            "tydzien" -> {
                withContext(Dispatchers.Main) {
                    max_krokow_tydzien.text = "$max_krokow"
                }
                Log.d("Maksymalna ilość krokow tygodniowa z BD:", max_krokow.toString())
            }

            "miesiac" -> {
                withContext(Dispatchers.Main) {
                    max_krokow_miesiac.text="$max_krokow"
                }
                Log.d("Maksymalna ilość krokow miesieczna z BD:", max_krokow.toString())
            }
        }
    }

    suspend fun setSumKroki(okresCzasu: String){
        var indeks: Int = 0
        var suma_krokow:Int
        when(okresCzasu)
        {
            "tydzien" -> {
                indeks=6
            }

            "miesiac" -> {
                indeks=29
            }
        }
        val fromDate = Daty.getDaysAgoDateWithoutTime(indeks)
        val toDate=Daty.getDaysAgoDateWithoutTime(-1)
        suma_krokow=myActivity.krokiDao.getKrokiSumByDate(fromDate,toDate)?: 0
        when(okresCzasu)
        {
            "tydzien" -> {
                withContext(Dispatchers.Main) {
                    suma_krokow_tydzien.text = "$suma_krokow"
                }
                Log.d("Maksymalna ilość krokow tygodniowa z BD:", suma_krokow.toString())
            }

            "miesiac" -> {
                withContext(Dispatchers.Main) {
                    suma_krokow_miesiac.text="$suma_krokow"
                }
                Log.d("Maksymalna ilość krokow miesieczna z BD:", suma_krokow.toString())
            }
        }
    }

    suspend fun updateStatystykiTextFields() {
        //tydzien
        setSredniaIloscKrokowDziennie("tydzien")
        setMaxKroki("tydzien")
        setSumKroki("tydzien")

        //miesiac
        setSredniaIloscKrokowDziennie("miesiac")
        setMaxKroki("miesiac")
        setSumKroki("miesiac")
    }
}