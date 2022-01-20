package com.example.projektandroid1

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.projektandroid1.data.*
import com.google.android.material.button.MaterialButton
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_dodaj_posilek.*
import kotlinx.android.synthetic.main.fragment_licznik.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var userHeader: TextView
    private lateinit var sharedPref: SharedPreferences
    private lateinit var circularProgressBar: CircularProgressBar
    private lateinit var cardKrokiButton: MaterialButton
    private lateinit var cardKalorieButton: MaterialButton
    private lateinit var progressValueTextView: TextView
    private lateinit var progressValueTextView2: TextView
    private lateinit var stepsCardTextView: TextView
    private lateinit var kalorieCardTextView: TextView
    public lateinit var kalorieDao: KalorieDao
    private var targetCaloriesCount=0
    private var mKalorieList: ArrayList<Kalorie>? =null

    //coś Kotlinowego w celu uzyskania funkcjonalności static Javy? ogarnąć jak działa
    companion object{
        val otwarcieKalorieActivity = 0
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //zapisanie daty otwarcia aplikacji
        val sharedPreferences = getSharedPreferences("stepCounter_sharedPref", Context.MODE_PRIVATE)
        //val date = Date(System.currentTimeMillis())
        var calendar: Calendar
        calendar = Calendar.getInstance()
        var simpleDateFormat = SimpleDateFormat("yyMMdd")
        val dateTime = simpleDateFormat.format(calendar.time).toString()
        val editor = sharedPreferences.edit()
        //sprawdzam czy zapisana data ostatniego uruchomienia jest datą dzisiejszą
        if(sharedPreferences.getString("aktualnaData", "").equals(dateTime)){

        }
        else{
            editor.putString("aktualnaData",dateTime)
            editor.putInt("obecnaLiczbaKrokow",0)
            editor.apply()
        }

        kalorieDao= ProjektAndroid1Database.get(this@MainActivity.application).getKalorieDao()

        CoroutineScope(Dispatchers.IO).launch {
            kalorieDao= ProjektAndroid1Database.get(this@MainActivity.application).getKalorieDao()
        }

        userHeader = findViewById<TextView>(R.id.userHeader)
        circularProgressBar = findViewById<CircularProgressBar>(R.id.circularProgressBar)
        cardKrokiButton = findViewById<MaterialButton>(R.id.cardKrokiButton)
        cardKalorieButton = findViewById<MaterialButton>(R.id.cardKalorieButton)
        progressValueTextView = findViewById<TextView>(R.id.progressValueTextView)
        progressValueTextView2 = findViewById<TextView>(R.id.progressValueTextView2)
        stepsCardTextView = findViewById<TextView>(R.id.stepsCardTextView)
        kalorieCardTextView = findViewById<TextView>(R.id.kalorieCardTextView)

        //wymaganie uprawnienia do aktywności
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q){
            if (ContextCompat.checkSelfPermission(this@MainActivity,
                    Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(this@MainActivity,
                        Manifest.permission.ACTIVITY_RECOGNITION)) {
                    ActivityCompat.requestPermissions(this@MainActivity,
                        arrayOf(Manifest.permission.ACTIVITY_RECOGNITION), 1)
                } else {
                    ActivityCompat.requestPermissions(this@MainActivity,
                        arrayOf(Manifest.permission.ACTIVITY_RECOGNITION), 1)
                }

            }
        }

        //Przycisk Do Informacji Uzytkownika
        val informacjeUzytkownikaActivityButton=findViewById<Button>(R.id.informacjeButton)
        informacjeUzytkownikaActivityButton.setOnClickListener {
            val intent = Intent(this, InformacjeUzytkownikaActivity::class.java)
            startActivity(intent)
        }

        // Przycisk do kroków na widgecie
        cardKrokiButton.setOnClickListener {
            val intent = Intent(this, LicznikKrokowActivity::class.java)
            startActivity(intent)
        }

        cardKalorieButton.setOnClickListener {
            val intent = Intent(this, KalorieActivity::class.java)
            otwarcieKalorieActivity+1
            startActivity(intent)
        }

        //odnośnik do polityki prywatności
        privacyButton.setOnClickListener {
            val intent = Intent(this, WebViewActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()

        sharedPref = getSharedPreferences(
            getString(R.string.shared_preferences_file_name),
            MODE_PRIVATE
        )

        val krokiSharedPreferences = getSharedPreferences("stepCounter_sharedPref", Context.MODE_PRIVATE)

        val imie_nazwisko_pref = sharedPref.getString("imie_nazwisko", "")
        userHeader.text = "Witaj $imie_nazwisko_pref!"

        val targetFootCount = sharedPref.getInt("cel_kroki", 6000)
        stepsCardTextView.text = "Twój cel to $targetFootCount kroków"

        /*val currentStepCount =
            krokiSharedPreferences.getFloat("obecnaLiczbaKrokow", 0F).roundToInt()*/
        val currentStepCount =
            krokiSharedPreferences.getInt("obecnaLiczbaKrokow", 0)
        val progressPercent = (currentStepCount.toDouble()/targetFootCount*100).toInt()
        progressValueTextView.text = "$progressPercent%"
        circularProgressBar.apply{
            progressMax = targetFootCount.toFloat()
            progress = currentStepCount.toFloat()
        }

        targetCaloriesCount = sharedPref.getInt("cel_kalorie", 2300)
        kalorieCardTextView.text = "Twój cel to $targetCaloriesCount kalorii"
        circularProgressBarKalorie.progressMax = targetCaloriesCount.toFloat()

        CoroutineScope(Dispatchers.IO).launch {
            initListaPosilkow()
        }
    }

    private suspend fun pobierzDzisiejszeKalorie():Int {
        val fromDate = Daty.getNowDateWithoutTime()
        val toDate=Daty.getNowDateWithoutTime()
        val kalorie = kalorieDao.getKalorieSumByDate(fromDate, toDate)?: 0
        return kalorie
    }

    private suspend fun initListaPosilkow() {
        //pobierz dzisiejsze posilki z db
        mKalorieList = ArrayList<Kalorie>(
            kalorieDao.getKalorieByDate(Daty.getNowDateWithoutTime())
        )
        updateCurrentCalorieTextView()
    }

    private suspend fun updateCurrentCalorieTextView() {
        var sum: Int = 0
        if (mKalorieList != null) {
            for (k in mKalorieList!!) {
                sum += k.ilosc_kalorii
            }
        }

        var calorieTarget: Int = targetCaloriesCount

        var s: String = "$sum/$calorieTarget"

        withContext(Dispatchers.Main)
        {
            progressValueTextView2.text = s
            circularProgressBarKalorie.apply {
                progressMax = targetCaloriesCount.toFloat()
                progress = sum.toFloat()
            }
        }
    }

    private var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Press BACK again to exit", Toast.LENGTH_SHORT).show()

        Handler(Looper.getMainLooper()).postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED) {
                    if ((ContextCompat.checkSelfPermission(this@MainActivity,
                            Manifest.permission.ACTIVITY_RECOGNITION) ==
                                PackageManager.PERMISSION_GRANTED)) {
                        Toast.makeText(this, "Uprawnienia przyznane", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Brak uprawnień", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }
}