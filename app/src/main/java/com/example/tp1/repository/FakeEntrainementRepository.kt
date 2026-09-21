package com.example.tp1.repository

import com.example.tp1.data.Entrainement
import com.example.tp1.data.TypeActivite
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.milliseconds

class FakeEntrainementRepository : IEntrainementRepository {

    private val entrainements = MutableStateFlow(FakeListEntrainement.liste)

    override suspend fun chargerEntrainements(): List<Entrainement> = withContext(Dispatchers.IO) {
        delay(1000.milliseconds)
        entrainements.value
    }

<<<<<<< HEAD
    override fun observerEntrainements(): Flow<List<Entrainement>> = entrainements.asStateFlow()

    override suspend fun ajouter(entrainement: Entrainement) = withContext(Dispatchers.IO) {
        delay(300.milliseconds)
        entrainements.update { liste ->
            val nouvelId = (liste.maxOfOrNull { it.id } ?: 0) + 1
            liste + entrainement.copy(id = nouvelId)
        }
    }

    override suspend fun basculerComplete(id: Int) = withContext(Dispatchers.IO) {
        entrainements.update { liste ->
            liste.map { if (it.id == id) it.copy(estComplete = !it.estComplete) else it }
        }
    }

    override suspend fun basculerFavori(id: Int) = withContext(Dispatchers.IO) {
        entrainements.update { liste ->
            liste.map { if (it.id == id) it.copy(estFavori = !it.estFavori) else it }
        }
    }

    override suspend fun supprimer(id: Int) = withContext(Dispatchers.IO) {
        entrainements.update { liste -> liste.filter { it.id != id } }
    }
}

object FakeListEntrainement {
    val liste = listOf(
        Entrainement(
            id = 1, titre = "Course à l'extérieur", activite = TypeActivite.COURSE,
            lieu = "Piste de course", exterieur = true, intensite = 8,
            notes = "Apporter équipements pour la course"
        ),
        Entrainement(
            id = 2, titre = "Vélo à l'extérieur", activite = TypeActivite.VELO,
            lieu = "Piste cyclable", exterieur = true, intensite = 7,
            notes = "Apporter un casque"
        ),
        Entrainement(
            id = 3, titre = "Muscu", activite = TypeActivite.MUSCULATION,
            lieu = "Gym", exterieur = false, intensite = 9,
            notes = "Étirez-vous avant"
        ),
=======
    override fun observerEntrainements(): Flow<List<Entrainement>> = flow {
        emit(FakeListEntrainement.liste)
        delay(2000.milliseconds)
        emit(FakeListEntrainement.liste)
    }.flowOn(Dispatchers.IO)

    override suspend fun ajouterEntrainement(entrainement: Entrainement) {
        withContext(Dispatchers.IO) {
            delay(500.milliseconds)

            FakeListEntrainement.liste.add(entrainement)
        }
    }
}

object FakeListEntrainement {
    val liste = mutableListOf(
        Entrainement("Course a l'exterieur", "Course", "Piste de course",
            true, 8, "Apporter équipements pour la course"),
        Entrainement("Vélo a l'exterieur", "Vélo", "Piste cyclabe",
            true, 7, "Apporter un casque"),
        Entrainement("Muscu", "Musculation", "Gym",
            false, 9, "Étirer vous avant"),
>>>>>>> origin/1-feature---ui--training-creation-form
    )
}