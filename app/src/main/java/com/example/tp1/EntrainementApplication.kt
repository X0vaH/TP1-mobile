package com.example.tp1

import android.app.Application
import com.example.tp1.database.EntrainementDatabase
import com.example.tp1.network.ExerciceApiService
import com.example.tp1.network.RetrofitInstance
import com.example.tp1.repository.RoomEntrainementRepository

class EntrainementApplication : Application() {
    val database by lazy { EntrainementDatabase.getInstance(this) }
    val exerciceApiService by lazy { RetrofitInstance.api }
    val repository by lazy { RoomEntrainementRepository(
        database,
        database.entrainementDao(),
        database.exerciceSeanceDao(),
        exerciceApiService
    )}
}