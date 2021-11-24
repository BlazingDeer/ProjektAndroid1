package com.example.projektandroid1.data

import android.app.Application
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities=[Kroki::class, Kalorie::class], version=1, exportSchema=false)
@TypeConverters(DateConverters::class)
abstract class ProjektAndroid1Database: RoomDatabase() {
    abstract fun getKrokiDao(): KrokiDao
    abstract fun getKalorieDao(): KalorieDao

    companion object {
        fun get(application: Application): ProjektAndroid1Database{
            return Room.databaseBuilder(application,ProjektAndroid1Database::class.java,
                "ProjektAndroid1Database").build()
        }
    }

}