package com.example.projektandroid1.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import java.util.Date

@Dao
interface KrokiDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertKroki(kroki:Kroki)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addKroki(kroki:Kroki): Long

    @Query("SELECT * FROM kroki_table ORDER BY id ASC")
    suspend fun getAllData(): List<Kroki>

    @Query("SELECT MAX(ilosc_krokow) FROM kroki_table WHERE date = :targetDate")
    suspend fun getKrokiByDate(targetDate: Date): Int

    @Query("SELECT MAX(ilosc_krokow) FROM kroki_table WHERE date >=:fromDate AND date < :toDate")
    suspend fun getKrokiMaxByDate(fromDate: Date, toDate: Date): Int?

    @Query("SELECT sum(ilosc_krokow) FROM kroki_table WHERE date >=:fromDate AND date < :toDate")
    suspend fun getKrokiSumByDate(fromDate: Date, toDate: Date): Int?
}