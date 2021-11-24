package com.example.projektandroid1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projektandroid1.data.Kalorie
import com.example.projektandroid1.data.ProjektAndroid1Database
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class KalorieActivity : AppCompatActivity() {

    private lateinit var posilekEditText: EditText
    private lateinit var iloscKaloriiEditText: EditText
    private lateinit var dodajKalorieButton: Button
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mCurrentCalorieTextView: TextView
    private var mRecyclerViewAdapter: RecyclerView.Adapter<KalorieRecyclerViewAdapter.ExampleViewHolder>? = null
    private var mLayoutManager:RecyclerView.LayoutManager?=null
    private var mKalorieList: ArrayList<Kalorie>? =null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kalorie)

        posilekEditText=findViewById<EditText>(R.id.posilekEditText)
        iloscKaloriiEditText=findViewById<EditText>(R.id.iloscKaloriiEditText)
        dodajKalorieButton=findViewById<Button>(R.id.dodajKalorieButton)
        mCurrentCalorieTextView=findViewById(R.id.currentCalorieTextView)

        mRecyclerView=findViewById(R.id.listaPosilkowRecyclerview)
        mLayoutManager=LinearLayoutManager(this)

        CoroutineScope(IO).launch {
            initListaPosilkow()
        }



        dodajKalorieButton.setOnClickListener {
            //zapytania do bazy danych musza byc async bo inaczej wypierdala wyjatek
            CoroutineScope(IO).launch {
                addNewKalorieItem()
            }

        }
    }

    private suspend fun initListaPosilkow()
    {
        //pobierz dzisiejsze posilki z db
        mKalorieList=ArrayList<Kalorie>(ProjektAndroid1Database.get(application).getKalorieDao()
            .getKalorieByDate(JEBANEDATY.getNowDateWithoutTime()))

        // dodanie przedmiotow z bazy danych do RecyclerView
        withContext(Dispatchers.Main) {
            mRecyclerViewAdapter = KalorieRecyclerViewAdapter(mKalorieList!!)
            mRecyclerView.layoutManager = mLayoutManager
            mRecyclerView.adapter = mRecyclerViewAdapter
        }
        updateCurrentCalorieTextView()
    }

    private suspend fun addNewKalorieItem()
    {


        if(posilekEditText.text.isNotEmpty() == true || iloscKaloriiEditText.text.isNotEmpty() == true)
        {
            val newKalorie=Kalorie(0,iloscKaloriiEditText.text.toString().toInt(),posilekEditText.text.toString(),JEBANEDATY.getNowDate())
            val inserted_id=ProjektAndroid1Database.get(application).getKalorieDao().insertKalorie(newKalorie)
            mKalorieList?.add(0,newKalorie)


            // kurwa okazuje sie ze toast nie mozna zrobic na innym niz main thread
            withContext(Dispatchers.Main){
                mRecyclerViewAdapter?.notifyItemInserted(0)
                mRecyclerView.smoothScrollToPosition(0)
                Toast.makeText(this@KalorieActivity,"Zapisano posiłek!",Toast.LENGTH_LONG).show()
            }
            updateCurrentCalorieTextView()

        }
        else
        {
            withContext(Dispatchers.Main) {
                Toast.makeText(this@KalorieActivity, "Wprowadź poprawne dane!", Toast.LENGTH_LONG).show()
            }
        }

    }

    private suspend fun updateCurrentCalorieTextView()
    {
        var sum: Int=0
        if(mKalorieList!=null)
        {
            for(k in mKalorieList!!)
            {
                sum+=k.ilosc_kalorii
            }
        }

        var calorieTarget: Int=2500

        var s: String = "$sum/$calorieTarget"

        withContext(Dispatchers.Main)
        {
            mCurrentCalorieTextView.text = s
        }
    }

}