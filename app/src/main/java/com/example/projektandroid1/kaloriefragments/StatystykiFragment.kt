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
import com.example.projektandroid1.KalorieActivity
import com.example.projektandroid1.R
import com.example.projektandroid1.data.Kalorie
import com.example.projektandroid1.data.ProjektAndroid1Database
import com.example.projektandroid1.data.kalorie_statystyki.NajczestszyPosilek
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import kotlinx.android.synthetic.main.fragment_statystyki.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class StatystykiFragment : Fragment() {

    lateinit var myActivity: KalorieActivity


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

        myActivity=(requireActivity() as KalorieActivity)

        CoroutineScope(Dispatchers.IO).launch {
            setBarChartValues()
            updateStatystykiTextFields()
        }

        myActivity.kalorieDao.getAllLiveData().observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            CoroutineScope(Dispatchers.IO).launch {
                updateStatystykiTextFields()
            }
        })
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


    //#####################################
    // funkcje do statystyk

    suspend fun setSredniaIloscKaloriiDzien(okresCzasu: String)
    {
        var suma_kalorii: Int=0
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

        for( i in dzielnik downTo 0)
        {
            val fromDate = JEBANEDATY.getDaysAgoDateWithoutTime(i)
            val toDate=JEBANEDATY.getDaysAgoDateWithoutTime(i-1)

            suma_kalorii += myActivity.kalorieDao.getKalorieSumByDate(fromDate, toDate)?: 0
        }

        srednia=suma_kalorii.toFloat()/dzielnik.toFloat()

        when(okresCzasu)
        {
            "tydzien" -> {
                kalorie_tydzien_srednia_kalorie_dzien.text="$srednia"
            }

            "miesiac" -> {
                kalorie_miesiac_srednia_kalorie_dzien.text="$srednia"
            }
        }
    }

    suspend fun setSredniaIloscPosilkowDzien(okresCzasu: String)
    {
        var suma_ilosci_posilkow: Int=0
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

        for( i in dzielnik downTo 0)
        {
            val fromDate = JEBANEDATY.getDaysAgoDateWithoutTime(i)
            val toDate=JEBANEDATY.getDaysAgoDateWithoutTime(i-1)

            suma_ilosci_posilkow += myActivity.kalorieDao.getPosilkiCountByDate(fromDate, toDate)?: 0
        }

        srednia=suma_ilosci_posilkow.toFloat()/dzielnik.toFloat()

        when(okresCzasu)
        {
            "tydzien" -> {
                kalorie_tydzien_srednia_posilki_dzien.text="$srednia"
            }

            "miesiac" -> {
                kalorie_miesiac_srednia_posilki_dzien.text="$srednia"
            }
        }
    }

    suspend fun setSredniaIloscKaloriiNaPosilek(okresCzasu: String)
    {
        var ilosc_posilkow: Int=0
        var suma_kalorii: Int=0
        var dni: Int=0
        var srednia: Float=0f
        when(okresCzasu)
        {
            "tydzien" -> {
                dni=6
            }

            "miesiac" -> {
                dni=29
            }
        }

        val fromDate = JEBANEDATY.getDaysAgoDateWithoutTime(dni)
        val toDate=JEBANEDATY.getDaysAgoDateWithoutTime(-1)

        ilosc_posilkow = myActivity.kalorieDao.getPosilkiCountByDate(fromDate, toDate)?: 1
        suma_kalorii=myActivity.kalorieDao.getKalorieSumByDate(fromDate,toDate)?: 0

        srednia=suma_kalorii.toFloat()/ilosc_posilkow.toFloat()
        when(okresCzasu)
        {
            "tydzien" -> {
                kalorie_tydzien_srednia_kalorie_posilek.text="$srednia"
            }

            "miesiac" -> {
                kalorie_miesiac_srednia_kalorie_posilek.text="$srednia"
            }
        }
    }

    suspend fun setNajczestszyPosilek(okresCzasu: String)
    {
        var posilek: NajczestszyPosilek?

        var dni: Int=0
        when(okresCzasu)
        {
            "tydzien" -> {
                dni=6
            }

            "miesiac" -> {
                dni=29
            }
        }

        val fromDate = JEBANEDATY.getDaysAgoDateWithoutTime(dni)
        val toDate=JEBANEDATY.getDaysAgoDateWithoutTime(-1)
        posilek=myActivity.kalorieDao.getNajczestszyPosilekByDate(fromDate,toDate)?: NajczestszyPosilek()

        when(okresCzasu)
        {
            "tydzien" -> {
                kalorie_tydzien_najczesciej_posilek.text="${posilek.posilek} | ${posilek.ilosc}"
            }

            "miesiac" -> {
                kalorie_miesiac_najczesciej_posilek.text="${posilek.posilek} | ${posilek.ilosc}"
            }
        }

    }


    suspend fun setIloscSpozytychPosilkow(okresCzasu: String)
    {
        var ilosc_posilkow: Int=0
        var dni: Int=0
        when(okresCzasu)
        {
            "tydzien" -> {
                dni=6
            }

            "miesiac" -> {
                dni=29
            }
        }
        val fromDate = JEBANEDATY.getDaysAgoDateWithoutTime(dni)
        val toDate=JEBANEDATY.getDaysAgoDateWithoutTime(-1)

        ilosc_posilkow = myActivity.kalorieDao.getPosilkiCountByDate(fromDate, toDate)?: 0

        when(okresCzasu)
        {
            "tydzien" -> {
                kalorie_tydzien_ilosc_posilki_tydzien.text="$ilosc_posilkow"
            }

            "miesiac" -> {
                kalorie_miesiac_ilosc_posilki_miesiac.text="$ilosc_posilkow"
            }
        }

    }

    suspend fun setIloscSpozytychKalorii(okresCzasu: String)
    {

        var suma_kalorii: Int=0
        var dni: Int=0
        when(okresCzasu)
        {
            "tydzien" -> {
                dni=6
            }

            "miesiac" -> {
                dni=29
            }
        }

        val fromDate = JEBANEDATY.getDaysAgoDateWithoutTime(dni)
        val toDate=JEBANEDATY.getDaysAgoDateWithoutTime(-1)
        suma_kalorii=myActivity.kalorieDao.getKalorieSumByDate(fromDate,toDate)?: 0

        when(okresCzasu)
        {
            "tydzien" -> {
                kalorie_tydzien_suma_kalorie.text="$suma_kalorii"
            }

            "miesiac" -> {
                kalorie_miesiac_suma_kalorie.text="$suma_kalorii"
            }
        }
    }

    suspend fun updateStatystykiTextFields()
    {
        //tydzien
        setSredniaIloscKaloriiDzien("tydzien")
        setSredniaIloscPosilkowDzien("tydzien")
        setSredniaIloscKaloriiNaPosilek("tydzien")
        setNajczestszyPosilek("tydzien")
        setIloscSpozytychPosilkow("tydzien")
        setIloscSpozytychKalorii("tydzien")


        //miesiac

        setSredniaIloscKaloriiDzien("miesiac")
        setSredniaIloscPosilkowDzien("miesiac")
        setSredniaIloscKaloriiNaPosilek("miesiac")
        setNajczestszyPosilek("miesiac")
        setIloscSpozytychPosilkow("miesiac")
        setIloscSpozytychKalorii("miesiac")
    }

}