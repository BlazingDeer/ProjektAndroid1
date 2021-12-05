package com.example.projektandroid1

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*

class InformacjeUzytkownikaActivity : AppCompatActivity() {

    private lateinit var zapiszButton: Button
    private lateinit var imie_nazwiskoEditText: EditText
    private lateinit var wzrostEditText: EditText
    private lateinit var wagaEditText: EditText
    private lateinit var plecSpinner: Spinner
    private lateinit var cel_krokiEditText: EditText
    private lateinit var cel_kalorieEditText: EditText
    private lateinit var sharedPref: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_informacje_uzytkownika)
        zapiszButton = findViewById<Button>(R.id.zapiszButton)
        imie_nazwiskoEditText = findViewById<EditText>(R.id.imie_nazwiskoEditText)
        wzrostEditText = findViewById<EditText>(R.id.wzrostEditText)
        wagaEditText = findViewById<EditText>(R.id.wagaEditText)
        plecSpinner = findViewById<Spinner>(R.id.plecSpinner)
        cel_kalorieEditText = findViewById<EditText>(R.id.cel_kalorieEditText)
        cel_krokiEditText = findViewById<EditText>(R.id.cel_krokiEditText)

        ArrayAdapter.createFromResource(
            this,
            R.array.plec_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            plecSpinner.adapter = adapter
        }

        sharedPref = getSharedPreferences(
            getString(R.string.shared_preferences_file_name),
            Context.MODE_PRIVATE
        )

        setDefaultFieldValues()


        zapiszButton.setOnClickListener {
            val sharedPref = getSharedPreferences(
                getString(R.string.shared_preferences_file_name),
                Context.MODE_PRIVATE
            ).edit()
            val imie_nazwisko = imie_nazwiskoEditText.text.toString()
            sharedPref.putString("imie_nazwisko", imie_nazwisko)

            if (wagaEditText.text.toString().isNotEmpty()) {
                val waga = wagaEditText.text.toString().toFloat()
                sharedPref.putFloat("waga", waga)
            }

            if (wzrostEditText.text.toString().isNotEmpty()) {
                val wzrost = wzrostEditText.text.toString().toInt()
                sharedPref.putInt("wzrost", wzrost)
            }

            if (cel_kalorieEditText.text.toString().isNotEmpty()) {
                val cel_kalorie = cel_kalorieEditText.text.toString().toInt()
                sharedPref.putInt("cel_kalorie", cel_kalorie)
            }

            if (cel_krokiEditText.text.toString().isNotEmpty()) {
                val cel_kroki = cel_krokiEditText.text.toString().toInt()
                sharedPref.putInt("cel_kroki", cel_kroki)
            }

            val plec=plecSpinner.selectedItemPosition
            sharedPref.putInt("plec",plec)

            sharedPref.apply()
            Toast.makeText(this, "Informacje zostały zaaktualizowane!", Toast.LENGTH_LONG).show()

        }
    }

    fun setDefaultFieldValues() {

        val imie_nazwisko_pref = sharedPref.getString("imie_nazwisko", "")
        imie_nazwiskoEditText.setText(imie_nazwisko_pref)

        val waga_pref = sharedPref.getFloat("waga", 0.0f)
        if (!waga_pref.equals(0.0f)) {
            wagaEditText.setText(waga_pref.toString())
        }
        val wzrost_pref = sharedPref.getInt("wzrost", 0)
        if (wzrost_pref != 0) {
            wzrostEditText.setText(wzrost_pref.toString())
        }

        val plec_pref = sharedPref.getInt("plec", 0)
        plecSpinner.setSelection(plec_pref)


        val cel_kalorie_pref = sharedPref.getInt("cel_kalorie", 2300)
        cel_kalorieEditText.setText(cel_kalorie_pref.toString())

        val cel_kroki_pref = sharedPref.getInt("cel_kroki", 6000)
        cel_krokiEditText.setText(cel_kroki_pref.toString())

    }


}