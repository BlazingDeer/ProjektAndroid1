package com.example.projektandroid1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val info_uzytButton=findViewById<Button>(R.id.informacje_button)
        info_uzytButton.setOnClickListener {
            val intent = Intent(this, InformacjeUzytkownikaActivity::class.java)
            startActivity(intent)
        }


        val kalorieButton=findViewById<Button>(R.id.kalorie_button)

    }
}