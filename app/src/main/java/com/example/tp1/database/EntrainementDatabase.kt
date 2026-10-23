package com.example.tp1.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

abstract class EntrainementDatabase : RoomDatabase() {

    abstract fun entrainementDao() : EntrainementDAO

    companion object {
        @Volatile
        private var INSTANCE: EntrainementDatabase? = null

        fun getInstance(context: Context) : EntrainementDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    EntrainementDatabase::class.java,
                    "entrainements_database.db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}