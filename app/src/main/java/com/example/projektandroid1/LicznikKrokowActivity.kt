package com.example.projektandroid1

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast

class LicznikKrokowActivity : AppCompatActivity(),SensorEventListener {


    private var sensorManager:SensorManager?=null

    private var running = false
    private var sumaLiczbaKrokow = 0f
    private var wczesniejszaLiczbaKrokow = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_licznik_krokow)

        wczytajDane()
        resetLiczenia()
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    override fun onResume() {
        super.onResume()
        running = true
        val czujnikKrokow = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        if(czujnikKrokow==null){
            Toast.makeText(this,"Niekompatybilne urządzenie!",Toast.LENGTH_LONG).show()
        }
        else{
            sensorManager?.registerListener(this,czujnikKrokow,SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if(running){
            sumaLiczbaKrokow = event!!.values[0]
            val obecnaLiczbaKrokow = sumaLiczbaKrokow.toInt() - wczesniejszaLiczbaKrokow.toInt()
            val tv_licznik = findViewById<TextView>(R.id.textView4)
            tv_licznik.text = ("$obecnaLiczbaKrokow")
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    fun resetLiczenia(){
        val tvLicznik = findViewById<TextView>(R.id.textView4)
        tvLicznik.setOnClickListener{
            Toast.makeText(this,"Przytrzymaj żeby zresetować!",Toast.LENGTH_LONG).show()
        }
        tvLicznik.setOnClickListener{
            wczesniejszaLiczbaKrokow = sumaLiczbaKrokow
            tvLicznik.text = 0.toString()
            zapiszDane()
        }
    }

    private fun zapiszDane(){
        val sharedPreferences = getSharedPreferences("pref1",Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putFloat("klucz1",wczesniejszaLiczbaKrokow)
        editor.apply()
    }

    private fun wczytajDane(){
        val sharedPreferences = getSharedPreferences("pref1",Context.MODE_PRIVATE)
        val savedNumber = sharedPreferences.getFloat("klucz1",0f)
        Log.d("MainActivity","$savedNumber")
        wczesniejszaLiczbaKrokow = savedNumber
    }
}