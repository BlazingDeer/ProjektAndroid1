package com.example.projektandroid1.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.projektandroid1.data.kalorie_statystyki.NajczestszyPosilek
import java.util.Date

@Dao
interface KalorieDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertKalorieList(kalorie: List<Kalorie>): List<Long>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertKalorie(kalorie: Kalorie): Long

    @Query("SELECT * FROM kalorie_table ORDER BY id ASC")
    suspend fun getAllData(): List<Kalorie>

    @Query("SELECT * FROM kalorie_table ORDER BY date DESC")
    fun getAllLiveData(): LiveData<List<Kalorie>>

    @Query("SELECT * FROM kalorie_table WHERE date >= :targetDate ORDER BY date DESC")
    suspend fun getKalorieByDate(targetDate: Date): List<Kalorie>

    @Query("SELECT * FROM kalorie_table WHERE date > :targetDate ORDER BY date DESC")
    fun getKalorieBeforeDate(targetDate: Date): LiveData<List<Kalorie>>


    @Query("SELECT * FROM kalorie_table WHERE id=:target_id")
    suspend fun getKalorieById(target_id: Int): Kalorie



    //###############################
    // STATYSTYKI


    //ilosc posilkow
    @Query("SELECT count(*) as ilosc_posilkow FROM kalorie_table" +
            " WHERE date >=:fromDate AND date < :toDate")
    suspend fun getPosilkiCountByDate(fromDate: Date, toDate: Date): Int?

    //najczestszy posilek
    @Query("SELECT max(t.ilosc) as ilosc, t.posilek as posilek from (SELECT count(k.posilek) as ilosc, k.posilek FROM kalorie_table k WHERE date >=:fromDate AND date < :toDate group by k.posilek) t")
    suspend fun getNajczestszyPosilekByDate(fromDate: Date, toDate: Date): NajczestszyPosilek

    // suma spozytych kalorii
    @Query("SELECT sum(ilosc_kalorii) FROM kalorie_table WHERE date >=:fromDate AND date < :toDate")
    suspend fun getKalorieSumByDate(fromDate: Date, toDate: Date): Int?

}