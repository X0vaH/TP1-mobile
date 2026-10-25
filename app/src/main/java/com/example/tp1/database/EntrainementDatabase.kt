package com.example.tp1.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [EntrainementEntity::class], version = 1, exportSchema = false)
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