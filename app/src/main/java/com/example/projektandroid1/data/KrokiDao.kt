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

    @Query("SELECT * FROM kroki_table ORDER BY id ASC")
    suspend fun getAllData(): List<Kroki>

    @Query("SELECT * FROM kroki_table WHERE date >= :targetDate ORDER BY date DESC")
    suspend fun getKrokiByDate(targetDate: Date): List<Kroki>

}