package com.example.projektandroid1

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.content.SharedPreferences
import android.hardware.SensorEvent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.projektandroid1.data.ProjektAndroid1Database
import com.google.android.material.button.MaterialButton
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_licznik.*

class MainActivity : AppCompatActivity() {

    private lateinit var userHeader: TextView
    private lateinit var sharedPref: SharedPreferences
    private lateinit var circularProgressBar: CircularProgressBar
    private lateinit var cardKrokiButton: MaterialButton
    private lateinit var progressValueTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userHeader = findViewById<TextView>(R.id.userHeader)
        circularProgressBar = findViewById<CircularProgressBar>(R.id.circularProgressBar)
        cardKrokiButton = findViewById<MaterialButton>(R.id.cardKrokiButton)
        progressValueTextView = findViewById<TextView>(R.id.progressValueTextView)

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

        // Przycisk Do KalorieActivity
        val kalorieActivityButton=findViewById<Button>(R.id.kalorieButton)
        kalorieActivityButton.setOnClickListener {
            val intent = Intent(this, KalorieActivity::class.java)
            startActivity(intent)
        }

        //odnośnik do polityki prywatności
        privacyButton.setOnClickListener {
            val intent = Intent(this, WebViewActivity::class.java)
            startActivity(intent)
        }

        //progressValueTextView.setText("lalaal")
    }

    override fun onResume() {
        super.onResume()

        sharedPref = getSharedPreferences(
            getString(R.string.shared_preferences_file_name),
            MODE_PRIVATE
        )
        val imie_nazwisko_pref = sharedPref.getString("imie_nazwisko", "")
        userHeader.setText("Witaj $imie_nazwisko_pref!")

        val targetFootCount = sharedPref.getInt("cel_kroki", 6000)
        circularProgressBar.progressMax = targetFootCount.toFloat()

        val currentStepCount = Math.round(sharedPref.getFloat("klucz1", 0F))
        progressValueTextView.setText("$currentStepCount")
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