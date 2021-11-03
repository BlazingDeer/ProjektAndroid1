package com.example.projektandroid1

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class InformacjeUzytkownikaActivity : AppCompatActivity() {

    private lateinit var zapiszButton: Button
    private lateinit var imie_nazwiskoEditText: EditText
    private lateinit var wzrostEditText: EditText
    private lateinit var wagaEditText: EditText


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_informacje_uzytkownika)
        zapiszButton=findViewById<Button>(R.id.zapiszButton)
        imie_nazwiskoEditText=findViewById<EditText>(R.id.imie_nazwiskoEditText)
        wzrostEditText=findViewById<EditText>(R.id.wzrostEditText)
        wagaEditText=findViewById<EditText>(R.id.wagaEditText)

        val sharedPref = getSharedPreferences( getString(R.string.shared_preferences_file_name), Context.MODE_PRIVATE)


        val imie_nazwisko_pref=sharedPref.getString("imie_nazwisko","")
        imie_nazwiskoEditText.setText(imie_nazwisko_pref)

        val waga_pref=sharedPref.getFloat("waga",0.0f)
        if(!waga_pref.equals(0.0f)){
            wagaEditText.setText(waga_pref.toString())
        }
        val wzrost_pref=sharedPref.getInt("wzrost",0)
        if(wzrost_pref!=0){
            wzrostEditText.setText(wzrost_pref.toString())
        }





        zapiszButton.setOnClickListener {
            val sharedPref = getSharedPreferences( getString(R.string.shared_preferences_file_name), Context.MODE_PRIVATE).edit()
            val imie_nazwisko=imie_nazwiskoEditText.text.toString()
            sharedPref.putString("imie_nazwisko",imie_nazwisko)
            var waga=0.0f
            var wzrost=0
            if(wagaEditText.text.toString().isNotEmpty()){
                waga=wagaEditText.text.toString().toFloat()
                sharedPref.putFloat("waga",waga)
            }

            if(wzrostEditText.text.toString().isNotEmpty()){
                wzrost=wzrostEditText.text.toString().toInt()
                sharedPref.putInt("wzrost",wzrost)
            }

            sharedPref.apply()
            Toast.makeText(this,"Informacje zostały zaaktualizowane!",Toast.LENGTH_LONG).show()

        }
    }
}