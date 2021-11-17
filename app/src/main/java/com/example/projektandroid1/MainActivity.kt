package com.example.projektandroid1

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
        val info_uzytButton=findViewById<Button>(R.id.informacje_button)
        info_uzytButton.setOnClickListener {
            val intent = Intent(this, InformacjeUzytkownikaActivity::class.java)
            startActivity(intent)
        }

        // Przycisk Do LicznikKrokowActivity
        val licznikKrokowActivityButton=findViewById<Button>(R.id.kroki_button)
        licznikKrokowActivityButton.setOnClickListener {
                val intent = Intent(this, LicznikKrokowActivity::class.java)
                startActivity(intent)
        }



        val kalorieButton=findViewById<Button>(R.id.kalorie_button)

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
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }
}