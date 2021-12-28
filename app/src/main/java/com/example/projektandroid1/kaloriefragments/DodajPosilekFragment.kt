package com.example.projektandroid1.kaloriefragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projektandroid1.JEBANEDATY
import com.example.projektandroid1.KalorieActivity
import com.example.projektandroid1.KalorieRecyclerViewAdapter
import com.example.projektandroid1.R
import com.example.projektandroid1.data.Kalorie
import com.example.projektandroid1.data.ProjektAndroid1Database
import kotlinx.android.synthetic.main.fragment_dodaj_posilek.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class DodajPosilekFragment : Fragment() {
    private var mRecyclerViewAdapter: RecyclerView.Adapter<KalorieRecyclerViewAdapter.ExampleViewHolder>? = null
    private var mLayoutManager: RecyclerView.LayoutManager?=null
    private var mKalorieList: ArrayList<Kalorie>? =null
    private var cel_kalorie=0
    lateinit var myActivity: KalorieActivity


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dodaj_posilek, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        myActivity=(requireActivity() as KalorieActivity)


        val sharedPref = requireActivity().getSharedPreferences(
            getString(R.string.shared_preferences_file_name),
            Context.MODE_PRIVATE
        )
        cel_kalorie=sharedPref.getInt("cel_kalorie",2300)

        mLayoutManager= LinearLayoutManager(context)


        CoroutineScope(Dispatchers.IO).launch {
            initListaPosilkow()
        }

        dodajKalorieButton.setOnClickListener {
            //zapytania do bazy danych musza byc async bo inaczej wypierdala wyjatek
            CoroutineScope(Dispatchers.IO).launch {
                addNewKalorieItem()
            }

        }

    }


    private suspend fun initListaPosilkow() {
        //pobierz dzisiejsze posilki z db
        mKalorieList = ArrayList<Kalorie>(
            myActivity.kalorieDao.getKalorieByDate(JEBANEDATY.getNowDateWithoutTime())
        )

        // dodanie przedmiotow z bazy danych do RecyclerView
        withContext(Dispatchers.Main) {
            mRecyclerViewAdapter = KalorieRecyclerViewAdapter(mKalorieList!!)
            listaPosilkowRecyclerview.layoutManager = mLayoutManager
            listaPosilkowRecyclerview.adapter = mRecyclerViewAdapter
        }
        updateCurrentCalorieTextView()
    }

    private suspend fun addNewKalorieItem() {


        if (posilekEditText.text.isNotEmpty() == true || iloscKaloriiEditText.text.isNotEmpty() == true) {
            val newKalorie = Kalorie(
                0, iloscKaloriiEditText.text.toString().toInt(), posilekEditText.text.toString(),
                JEBANEDATY.getNowDate()
            )
            val inserted_id =
                myActivity.kalorieDao.insertKalorie(newKalorie)
            mKalorieList?.add(0, newKalorie)


            // kurwa okazuje sie ze toast nie mozna zrobic na innym niz main thread
            withContext(Dispatchers.Main) {
                mRecyclerViewAdapter?.notifyItemInserted(0)
                listaPosilkowRecyclerview.smoothScrollToPosition(0)
                Toast.makeText(activity, "Zapisano posiłek!", Toast.LENGTH_LONG).show()
            }
            updateCurrentCalorieTextView()

        } else {
            withContext(Dispatchers.Main) {
                Toast.makeText(activity, "Wprowadź poprawne dane!", Toast.LENGTH_LONG)
                    .show()
            }
        }

    }

    private suspend fun updateCurrentCalorieTextView() {
        var sum: Int = 0
        if (mKalorieList != null) {
            for (k in mKalorieList!!) {
                sum += k.ilosc_kalorii
            }
        }

        var calorieTarget: Int = cel_kalorie

        var s: String = "$sum/$calorieTarget"

        withContext(Dispatchers.Main)
        {
            currentCalorieTextView.text = s
        }
    }

}