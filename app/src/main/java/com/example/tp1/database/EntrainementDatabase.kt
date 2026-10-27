package com.example.tp1.database

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.tp1.database.dao.EntrainementDAO
import com.example.tp1.database.dao.ExerciceSeanceDAO
import com.example.tp1.database.entity.EntrainementEntity
import com.example.tp1.database.entity.ExerciceSeanceEntity

@Database(
    entities = [EntrainementEntity::class, ExerciceSeanceEntity::class],
    version = 2,
    autoMigrations = [
        AutoMigration(from = 1, to = 2)
    ],
    exportSchema = true
)
abstract class EntrainementDatabase : RoomDatabase() {

    abstract fun entrainementDao() : EntrainementDAO
    abstract fun exerciceSeanceDao() : ExerciceSeanceDAO

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
