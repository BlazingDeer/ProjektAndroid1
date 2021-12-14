package com.example.projektandroid1.krokifragments

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.example.projektandroid1.R
import kotlinx.android.synthetic.main.fragment_licznik.*


class LicznikFragment : Fragment(), SensorEventListener {

    private var sensorManager:SensorManager?=null

    private var running = false
    private var sumaLiczbaKrokow = 0f
    private var wczesniejszaLiczbaKrokow = 0f
    private var cel_kroki=0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_licznik, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPref = requireActivity().getSharedPreferences(
            getString(R.string.shared_preferences_file_name),
            Context.MODE_PRIVATE
        )

        //dodac ten cel do wyswietlania na liczniku
        cel_kroki=sharedPref.getInt("cel_kroki",6000)

        wczytajDane()
        resetLiczenia()
        sensorManager =requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    override fun onResume() {
        super.onResume()
        running = true
        val czujnikKrokow = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        if(czujnikKrokow==null){
            Toast.makeText(context,"Niekompatybilne urządzenie!", Toast.LENGTH_LONG).show()
        }
        else{
            sensorManager?.registerListener(this,czujnikKrokow, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if(running){
            sumaLiczbaKrokow = event!!.values[0]
            val obecnaLiczbaKrokow = sumaLiczbaKrokow.toInt() - wczesniejszaLiczbaKrokow.toInt()
            tvLicznik.text = ("$obecnaLiczbaKrokow")
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    fun resetLiczenia(){

        //zeby to dzialalo to do przytrzymywania jest onTouch czy cos takiego
        tvLicznik.setOnClickListener{
            Toast.makeText(context,"Przytrzymaj żeby zresetować!", Toast.LENGTH_LONG).show()
        }
        tvLicznik.setOnClickListener{
            wczesniejszaLiczbaKrokow = sumaLiczbaKrokow
            tvLicznik.text = 0.toString()
            zapiszDane()
        }
    }

    private fun zapiszDane(){
        val sharedPreferences = requireActivity().getSharedPreferences("pref1", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putFloat("klucz1",wczesniejszaLiczbaKrokow)
        editor.apply()
    }

    private fun wczytajDane(){
        val sharedPreferences = requireActivity().getSharedPreferences("pref1", Context.MODE_PRIVATE)
        val savedNumber = sharedPreferences.getFloat("klucz1",0f)
        Log.d("MainActivity","$savedNumber")
        wczesniejszaLiczbaKrokow = savedNumber
    }

}