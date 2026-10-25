package com.example.tp1

import android.app.Application
import com.example.tp1.database.EntrainementDatabase
import com.example.tp1.repository.RoomEntrainementRepository

class EntrainementApplication : Application() {
    val database by lazy { EntrainementDatabase.getInstance(this) }
    val repository by lazy { RoomEntrainementRepository(database.entrainementDao()) }
}