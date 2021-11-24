package com.example.projektandroid1.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "kalorie_table")
data class Kalorie(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val ilosc_kalorii: Int,
    val posilek: String,
    val date: Date
)