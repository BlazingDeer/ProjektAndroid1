package com.example.projektandroid1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projektandroid1.data.Kalorie

class KalorieRecyclerViewAdapter(kalorieList: ArrayList<Kalorie>) :
    RecyclerView.Adapter<KalorieRecyclerViewAdapter.ExampleViewHolder>() {


    var mKalorieList: ArrayList<Kalorie> = kalorieList

    class ExampleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {

        val mIloscKaloriiTextView: TextView=itemView.findViewById(R.id.example_itemTextView1)
        val mPosilekTextView: TextView=itemView.findViewById(R.id.example_itemTextView2)

    }

    fun setListData(data: ArrayList<Kalorie>)
    {
        mKalorieList=data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExampleViewHolder {
        val v: View=LayoutInflater.from(parent.context).inflate(R.layout.example_item,
            parent,false)
        val evh: ExampleViewHolder= ExampleViewHolder(v)
        return evh
    }

    override fun onBindViewHolder(holder: ExampleViewHolder, position: Int) {
        var currentItem: Kalorie = mKalorieList[position]
        holder.mIloscKaloriiTextView.setText(currentItem.ilosc_kalorii.toString())
        holder.mPosilekTextView.setText(currentItem.posilek)

    }

    override fun getItemCount(): Int {
        return mKalorieList.size
    }
}