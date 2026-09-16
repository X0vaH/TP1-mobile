package com.example.tp1.repository

import com.example.tp1.data.Entrainement
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.milliseconds

class FakeEntrainementRepository : IEntrainementRepository {
    override suspend fun chargerEntrainements() : List<Entrainement> = withContext(Dispatchers.IO) {
        delay(3000.milliseconds)
        FakeListEntrainement.liste
    }

    override fun observerEntrainements(): Flow<List<Entrainement>> = flow {
        emit(FakeListEntrainement.liste)
        delay(2000.milliseconds)
        emit(FakeListEntrainement.liste)
    }.flowOn(Dispatchers.IO)
}

object FakeListEntrainement {
    val liste = listOf(
        Entrainement("Course a l'exterieur", "Course", "Piste de course",
            true, 8, "Apporter équipements pour la course"),
        Entrainement("Vélo a l'exterieur", "Vélo", "Piste cyclabe",
            true, 7, "Apporter un casque"),
        Entrainement("Muscu", "Musculation", "Gym",
            false, 9, "Étirer vous avant"),
    )
}