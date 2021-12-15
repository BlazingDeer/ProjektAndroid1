package com.example.projektandroid1.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import java.util.Date

@Dao
interface KalorieDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertKalorieList(kalorie: List<Kalorie>): List<Long>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertKalorie(kalorie: Kalorie): Long

    @Query("SELECT * FROM kalorie_table ORDER BY id ASC")
    suspend fun getAllData(): List<Kalorie>

    @Query("SELECT * FROM kalorie_table WHERE date >= :targetDate ORDER BY date DESC")
    suspend fun getKalorieByDate(targetDate: Date): List<Kalorie>


    @Query("SELECT * FROM kalorie_table WHERE id=:target_id")
    suspend fun getKalorieById(target_id: Int): Kalorie

    @Query("SELECT sum(ilosc_kalorii) FROM kalorie_table WHERE date >=:fromDate AND date < :toDate")
    suspend fun getKalorieSumByDate(fromDate: Date, toDate: Date): Int?
}