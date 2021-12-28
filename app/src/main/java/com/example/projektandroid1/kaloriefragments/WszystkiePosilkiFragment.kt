package com.example.projektandroid1.kaloriefragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projektandroid1.KalorieActivity
import com.example.projektandroid1.KalorieRecyclerViewAdapter
import com.example.projektandroid1.R
import com.example.projektandroid1.data.Kalorie
import com.example.projektandroid1.data.ProjektAndroid1Database
import kotlinx.android.synthetic.main.fragment_wszystkie_posilki.*


class WszystkiePosilkiFragment : Fragment() {

    private lateinit var  mRecyclerViewAdapter: KalorieRecyclerViewAdapter
    private lateinit var mLayoutManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wszystkie_posilki, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mLayoutManager= LinearLayoutManager(context)
        mRecyclerViewAdapter = KalorieRecyclerViewAdapter(ArrayList<Kalorie>())

        listaWszystkichPosilkowRecyclerview.layoutManager = mLayoutManager
        listaWszystkichPosilkowRecyclerview.adapter = mRecyclerViewAdapter

        val myActivity=(requireActivity() as KalorieActivity)

        myActivity.kalorieDao.getAllLiveData().observe(viewLifecycleOwner, Observer{
            mRecyclerViewAdapter.setListData(ArrayList(it))
            mRecyclerViewAdapter.notifyDataSetChanged()
            Toast.makeText(context,"Update",Toast.LENGTH_SHORT).show()
        })
    }




}