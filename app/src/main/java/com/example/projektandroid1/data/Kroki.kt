package com.example.projektandroid1.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "kroki_table")
data class Kroki (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val ilosc_krokow: Int,
    val date: Date
        )