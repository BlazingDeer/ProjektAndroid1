package com.example.projektandroid1

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.*
import androidx.core.content.ContentProviderCompat.requireContext
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_informacje_uzytkownika.*

class InformacjeUzytkownikaActivity : AppCompatActivity() {

    private lateinit var zapiszButton: Button
    private lateinit var imie_nazwiskoEditText: TextInputLayout
    private lateinit var wzrostEditText: TextInputLayout
    private lateinit var wagaEditText: TextInputLayout
    private lateinit var plecSpinner: TextInputLayout
    private lateinit var autoCompleteTextView: AutoCompleteTextView
    private lateinit var cel_krokiEditText: TextInputLayout
    private lateinit var cel_kalorieEditText: TextInputLayout
    private lateinit var sharedPref: SharedPreferences

    private lateinit var textView4: TextView
    private lateinit var textView6: TextView
    private lateinit var textView8: TextView
    private lateinit var textView10: TextView
    private lateinit var textView12: TextView
    private lateinit var textView14: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_informacje_uzytkownika)

        this.setTitle("KeepFit - Dane użytkownika")
        val actionBar = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)

        zapiszButton = findViewById<Button>(R.id.zapiszButton)
        imie_nazwiskoEditText = findViewById<TextInputLayout>(R.id.imie_nazwiskoEditText)
        wzrostEditText = findViewById<TextInputLayout>(R.id.wzrostEditText)
        wagaEditText = findViewById<TextInputLayout>(R.id.wagaEditText)
        plecSpinner = findViewById<TextInputLayout>(R.id.plecSpinner)
        autoCompleteTextView = findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView)
        cel_kalorieEditText = findViewById<TextInputLayout>(R.id.cel_kalorieEditText)
        cel_krokiEditText = findViewById<TextInputLayout>(R.id.cel_krokiEditText)
        textView4 = findViewById<TextView>(R.id.textView4)
        textView6 = findViewById<TextView>(R.id.textView6)
        textView8 = findViewById<TextView>(R.id.textView8)
        textView10 = findViewById<TextView>(R.id.textView10)
        textView12 = findViewById<TextView>(R.id.textView12)
        textView14 = findViewById<TextView>(R.id.textView14)

//        ArrayAdapter.createFromResource(
//            this,
//            R.array.plec_array,
//            android.R.layout.simple_spinner_item
//        ).also { adapter ->
//            // Specify the layout to use when the list of choices appears
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//            // Apply the adapter to the spinner
//            plecSpinner.adapter = adapter
//        }

        sharedPref = getSharedPreferences(
            getString(R.string.shared_preferences_file_name),
            MODE_PRIVATE
        )

        val imie_nazwisko_pref = sharedPref.getString("imie_nazwisko", "")
        textView4.setText("$imie_nazwisko_pref")
        val wzrost_pref = sharedPref.getInt("wzrost", 0)
        textView6.setText("$wzrost_pref")
        val waga_pref = sharedPref.getFloat("waga", 0.0f)
        textView8.setText("$waga_pref")
        val plec_pref = sharedPref.getString("plec", "Inny")
        textView10.setText("$plec_pref")
        val cel_kalorie_pref = sharedPref.getInt("cel_kalorie", 2300)
        textView12.setText("$cel_kalorie_pref")
        val cel_kroki_pref = sharedPref.getInt("cel_kroki", 6000)
        textView14.setText("$cel_kroki_pref")

        val plec_array = resources.getStringArray(R.array.plec_array)
        val arrayAdapter = ArrayAdapter(this, R.layout.list_item, plec_array)
        autoCompleteTextView.setAdapter(arrayAdapter)

        setDefaultFieldValues()
        val caloriesBurntValue = findViewById<TextView>(R.id.caloriesBurntValue)
        caloriesBurntValue.text = obliczSpaloneKalorie().plus(" kcal")

        zapiszButton.setOnClickListener {
            val sharedPref = getSharedPreferences(
                getString(R.string.shared_preferences_file_name),
                MODE_PRIVATE
            ).edit()
            if(imie_nazwiskoEditText.editText?.text.toString().isNotEmpty()){
                val imie_nazwisko = imie_nazwiskoEditText.editText?.text.toString()
                sharedPref.putString("imie_nazwisko", imie_nazwisko)
            }

            if (wagaEditText.editText?.text.toString().isNotEmpty()) {
                val waga = wagaEditText.editText?.text.toString().toFloat()
                sharedPref.putFloat("waga", waga)
            }

            if (wzrostEditText.editText?.text.toString().isNotEmpty()) {
                val wzrost = wzrostEditText.editText?.text.toString().toInt()
                sharedPref.putInt("wzrost", wzrost)
            }

            if (cel_kalorieEditText.editText?.text.toString().isNotEmpty()) {
                val cel_kalorie = cel_kalorieEditText.editText?.text.toString().toInt()
                sharedPref.putInt("cel_kalorie", cel_kalorie)
            }

            if (cel_krokiEditText.editText?.text.toString().isNotEmpty()) {
                val cel_kroki = cel_krokiEditText.editText?.text.toString().toInt()
                sharedPref.putInt("cel_kroki", cel_kroki)
            }

            if(plecSpinner.editText?.text.toString().isNotEmpty()){
                val plec=plecSpinner.editText?.text.toString()
                sharedPref.putString("plec",plec)
            }

            sharedPref.apply()
            Toast.makeText(this, "Informacje zostały zaktualizowane!", Toast.LENGTH_LONG).show()

            finish();
            overridePendingTransition(0, 0);
            startActivity(getIntent());
            overridePendingTransition(0, 0);
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        startActivity(Intent(this, MainActivity::class.java))
        return super.onOptionsItemSelected(item)
    }

    fun setDefaultFieldValues() {

        val imie_nazwisko_pref = sharedPref.getString("imie_nazwisko", "")
        imie_nazwiskoEditText.setHelperText(imie_nazwisko_pref)

        val waga_pref = sharedPref.getFloat("waga", 0.0f)
        if (!waga_pref.equals(0.0f)) {
            wagaEditText.setHelperText("Obecnie $waga_pref")
        }
        val wzrost_pref = sharedPref.getInt("wzrost", 0)
        if (wzrost_pref != 0) {
            wzrostEditText.setHelperText("Obecnie $wzrost_pref")
        }

        val plec_pref = sharedPref.getString("plec", "Inny")

        val cel_kalorie_pref = sharedPref.getInt("cel_kalorie", 2300)
        cel_kalorieEditText.setHint("Cel kalorii")
        cel_kalorieEditText.setHelperText("Obecnie $cel_kalorie_pref")
        //cel_kalorieEditText.setText(cel_kalorie_pref.toString())

        val cel_kroki_pref = sharedPref.getInt("cel_kroki", 6000)
        cel_krokiEditText.setHelperText("Obecnie $cel_kroki_pref")

    }

    //współczynnik TEE(total energy expenditure)
    private fun obliczSpaloneKalorie(): String{
        val stepsSharedPref = getSharedPreferences("stepCounter_sharedPref", Context.MODE_PRIVATE)
        val kroki = stepsSharedPref.getInt("obecnaLiczbaKrokow",0)
        val bmr: Double
        val wynik: String
        val plec_pref = sharedPref.getString("plec", "Inny")
        val waga_pref = sharedPref.getFloat("waga", 0.0f)
        val wzrost_pref = sharedPref.getInt("wzrost", 0)
        if(plec_pref.equals("Mężczyzna")){
            bmr = 66+6.2*waga_pref*0.45+12.7*wzrost_pref*2.54-6.27*24
        }
        else{
            bmr = 655+4.35*waga_pref*0.45+4.7*wzrost_pref*2.54-4.7*24
        }
        val tee = (bmr*1.4)/200*kroki
        wynik = "%.2f".format(tee)
        return wynik
    }


}