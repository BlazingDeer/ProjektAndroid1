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
import com.example.projektandroid1.Daty
import com.example.projektandroid1.LicznikKrokowActivity
import com.example.projektandroid1.R
import com.example.projektandroid1.data.Kroki
import kotlinx.android.synthetic.main.fragment_licznik.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


/* Lepszy licznik ale zabrakło czasu żeby prawidłowo wykorzystać
class LicznikFragment : Fragment(), SensorEventListener {

    private var sensorManager:SensorManager?=null
    private lateinit var sharedPref: SharedPreferences

    private var running = false
    private var sumaLiczbaKrokow = 0f
    private var wczesniejszaSumaKrokow = 0f
    private var cel_kroki=0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_licznik, container, false)
        return view
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
        Log.d("czujnikKrokow?", czujnikKrokow.toString())

        if(czujnikKrokow==null){
            Toast.makeText(context,"Niekompatybilne urządzenie!", Toast.LENGTH_LONG).show()
        }
        else{
            sensorManager?.registerListener(this,czujnikKrokow, SensorManager.SENSOR_DELAY_UI)
        }

        sharedPref = requireActivity().getSharedPreferences(
            getString(R.string.shared_preferences_file_name),
            AppCompatActivity.MODE_PRIVATE
        )

        //ustawienie zakresu wskaźnika na ustalony cel liczby kroków
        val cel_kroki_pref = sharedPref.getInt("cel_kroki", 6000)
        Log.d("Ustalony cel kroków","$cel_kroki_pref")
        circularProgressBar.progressMax = cel_kroki_pref.toFloat()
    }

    override fun onSensorChanged(event: SensorEvent?) {
        val tv = activity?.findViewById<TextView>(R.id.tvLicznik)
        if(running){
            //Wartość kroków wykonanych od ostatniego uruchomienia telefonu
            sumaLiczbaKrokow = event!!.values[0]
            Log.d("SumaLiczbaKrokow", sumaLiczbaKrokow.toString())
            val obecnaLiczbaKrokow = sumaLiczbaKrokow.toInt() - wczesniejszaSumaKrokow.toInt()
            if (tv != null) {
                tv.text = "$obecnaLiczbaKrokow/$cel_kroki"
            }
            circularProgressBar?.apply {
                setProgressWithAnimation(obecnaLiczbaKrokow.toFloat())
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    //reset licznika
    fun resetLiczenia(){
        tvLicznik.setOnClickListener{
            Toast.makeText(context,"Przytrzymaj żeby zresetować!", Toast.LENGTH_LONG).show()
        }
        //po przytrzymaniu tekstu resetuje licznik
        tvLicznik.setOnLongClickListener{
            wczesniejszaSumaKrokow = sumaLiczbaKrokow
            tvLicznik.text = 0.toString()
            zapiszDane()
            circularProgressBar.apply {
                setProgressWithAnimation(0F)
            }
            true
        }
    }

    private fun zapiszDane(){
        val sharedPreferences = requireActivity().getSharedPreferences("stepCounter_sharedPref", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putFloat("obecnaLiczbaKrokow",wczesniejszaSumaKrokow)
        editor.apply()
    }

    private fun wczytajDane(){
        val sharedPreferences = requireActivity().getSharedPreferences("stepCounter_sharedPref", Context.MODE_PRIVATE)
        val zapisanaLiczbaKrokow = sharedPreferences.getFloat("wczesniejszaLiczbaKrokow",0f)
        val obecnaLiczbaKrokow = sharedPreferences.getFloat("obecnaLiczbaKrokow",0f)
        Log.d("Wczesniejsza liczba krokow","$zapisanaLiczbaKrokow")
        Log.d("Obecna liczba krokow","$obecnaLiczbaKrokow")
        wczesniejszaSumaKrokow = zapisanaLiczbaKrokow
    }
}*/

class LicznikFragment : Fragment(), SensorEventListener{

    private var sensorManager:SensorManager?=null
    lateinit var myActivity: LicznikKrokowActivity
    private var liczbaKrokow: Int = 0
    private var wczesniejszaLiczbaKrokow: Double = 0.0
    private var cel_kroki = 0
    private var running = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sensorManager =requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val czujnikKrokow = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        if(czujnikKrokow==null){
            Toast.makeText(context,"Niekompatybilne urządzenie!", Toast.LENGTH_LONG).show()
        }
        else{
            sensorManager?.registerListener(this,czujnikKrokow, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_licznik, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        myActivity=(requireActivity() as LicznikKrokowActivity)

        val sharedPreferences = requireActivity().getSharedPreferences(getString(R.string.shared_preferences_file_name), Context.MODE_PRIVATE)

        //dodac ten cel do wyswietlania na liczniku
        cel_kroki=sharedPreferences.getInt("cel_kroki",6000)
        Log.d("Ustalony cel kroków","$cel_kroki")
        circularProgressBar.progressMax = cel_kroki.toFloat()
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if(running){
            val tv = activity?.findViewById<TextView>(R.id.tvLicznik)
            val x_acceleration: Float = event!!.values[0]
            val y_acceleration: Float = event!!.values[1]
            val z_acceleration: Float = event!!.values[2]

            val magnitude: Double = Math.sqrt((x_acceleration*x_acceleration + y_acceleration*y_acceleration + z_acceleration*z_acceleration).toDouble())
            val MagnitudeDelta: Double = magnitude-wczesniejszaLiczbaKrokow
            wczesniejszaLiczbaKrokow = magnitude

            if(MagnitudeDelta > 8){
                liczbaKrokow++
            }
            if (tv != null) {
                tv.text = "$liczbaKrokow/$cel_kroki"
            }
            circularProgressBar?.apply {
                setProgressWithAnimation(liczbaKrokow.toFloat())
            }
        }
    }

    override fun onResume() {
        super.onResume()

        running = true
        val sharedPreferences = requireActivity().getSharedPreferences("stepCounter_sharedPref", Context.MODE_PRIVATE)
        liczbaKrokow = sharedPreferences.getInt("obecnaLiczbaKrokow", 0)
    }

    override fun onPause() {
        super.onPause()

        val sharedPreferences = requireActivity().getSharedPreferences("stepCounter_sharedPref", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("obecnaLiczbaKrokow",liczbaKrokow)
        editor.apply()
    }

    override fun onStop() {
        super.onStop()

        val sharedPreferences = requireActivity().getSharedPreferences("stepCounter_sharedPref", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("obecnaLiczbaKrokow",liczbaKrokow)
        editor.apply()
    }

    override fun onDestroy() {
        super.onDestroy()

        CoroutineScope(Dispatchers.IO).launch {
            zapiszKrokiBD(liczbaKrokow)
        }
    }

    private suspend fun zapiszKrokiBD(krokiDoBD: Int) {
        /*var calendar: Calendar
        calendar = Calendar.getInstance()
        var simpleDateFormat = SimpleDateFormat("yyMMdd")
        val dateTime = simpleDateFormat.format(calendar.time).toString()*/
        val newKroki = Kroki(
            0, krokiDoBD, Daty.getNowDate()
        )
        val inserted_id = myActivity.krokiDao.addKroki(newKroki)
    }
}