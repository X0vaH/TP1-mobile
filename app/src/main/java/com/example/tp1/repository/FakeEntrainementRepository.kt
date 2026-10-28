package com.example.tp1.repository

import com.example.tp1.data.Entrainement
import com.example.tp1.network.ExerciceDTO
import com.example.tp1.data.TypeActivite
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.milliseconds

class FakeEntrainementRepository : IEntrainementRepository {

    private val entrainements = MutableStateFlow(FakeListEntrainement.liste)

    override suspend fun chargerEntrainements(): List<Entrainement> = withContext(Dispatchers.IO) {
        delay(1000.milliseconds)
        entrainements.value
    }

    override fun observerEntrainements(): Flow<List<Entrainement>> = entrainements.asStateFlow()

    override suspend fun obtenirParId(id: Int): Entrainement? = entrainements.value.find { it.id == id }

    override suspend fun obtenirAvecExercices(id: Int): Entrainement? = obtenirParId(id)

    override fun observerAvecExercices(id: Int): Flow<Entrainement?> =
        entrainements.map { liste -> liste.find { it.id == id } }

    override suspend fun modifier(entrainement: Entrainement) {
        require(entrainements.value.any { it.id == entrainement.id }) { "Cette séance n'existe plus." }
        entrainements.update { liste ->
            liste.map { actuel ->
                if (actuel.id == entrainement.id) entrainement.copy(
                    estComplete = actuel.estComplete,
                    estFavori = actuel.estFavori
                ) else actuel
            }
        }
    }
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

    override suspend fun rechercherExercicesDepuisApi(
        motCle: String,
        apiKey: String
    ): Result<List<ExerciceDTO>> {
        return Result.failure(UnsupportedOperationException("Utilisez le dépôt Room pour rechercher des exercices."))
    }

    override suspend fun obtenirConsignesExercice(idExterne: String, apiKey: String): Result<ExerciceDTO> {
        return Result.failure(UnsupportedOperationException("Utilisez le dépôt Room pour charger les consignes."))
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
        )
    )
}
