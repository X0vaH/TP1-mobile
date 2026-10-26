package com.example.tp1.repository

import retrofit2.HttpException
import com.example.tp1.data.Entrainement
import com.example.tp1.data.TypeActivite
import com.example.tp1.database.EntrainementDAO
import com.example.tp1.database.toEntity
import com.example.tp1.database.toModel
import com.example.tp1.network.ExerciceApiService
import com.example.tp1.network.ExerciceDTO
import com.example.tp1.network.RetrofitInstance
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.IOException

class RoomEntrainementRepository(
    private val entrainementDao: EntrainementDAO,
    private val apiService: ExerciceApiService = RetrofitInstance.api
) : IEntrainementRepository {
    override suspend fun chargerEntrainements(): List<Entrainement> {
        return entrainementDao.chargerTousLesEntrainements().map { entity ->
            entity.toModel()
        }
    }

    override fun observerEntrainements(): Flow<List<Entrainement>> {
        return entrainementDao.obtenirTousLesEntrainements().map { entities ->
            entities.map { entity -> entity.toModel() }
        }
    }


    override suspend fun obtenirParId(id: Int): Entrainement? {
        return entrainementDao.trouverParId(id)?.toModel()
    }

    override suspend fun modifier(entrainement: Entrainement) {
        val actuel = obtenirParId(entrainement.id)
            ?: throw IllegalArgumentException("Cette séance n'existe plus.")
        require(entrainement.titre.trim().length >= 3) { "Le titre doit contenir au moins 3 caractères." }
        require(entrainement.lieu.isNotBlank()) { "Veuillez saisir un lieu." }
        require(entrainement.intensite in 1..10) { "L'intensité doit être comprise entre 1 et 10." }
        entrainementDao.modifierEntrainement(
            actuel.copy(
                titre = entrainement.titre.trim(),
                activite = entrainement.activite,
                lieu = entrainement.lieu.trim(),
                exterieur = entrainement.exterieur,
                intensite = entrainement.intensite,
                notes = entrainement.notes
            ).toEntity()
        )
    }
    override suspend fun ajouter(entrainement: Entrainement) {
        entrainementDao.insererEntrainement(entrainement.toEntity().copy(id = 0))
    }

    override suspend fun basculerComplete(id: Int) {
        var entrainement = entrainementDao.trouverParId(id)?.toModel()
            ?: throw IllegalArgumentException("Cannot find entrainement with Id $id")
        entrainement = entrainement.copy(estComplete = !entrainement.estComplete)
        entrainementDao.modifierEntrainement(entrainement.toEntity())
    }

    override suspend fun basculerFavori(id: Int) {
        var entrainement = entrainementDao.trouverParId(id)?.toModel()
            ?: throw IllegalArgumentException("Cannot find entrainement with Id $id")
        entrainement = entrainement.copy(estFavori = !entrainement.estFavori)
        entrainementDao.modifierEntrainement(entrainement.toEntity())
    }

    override suspend fun supprimer(id: Int) {
        entrainementDao.supprimerParId(id)
    }

    override suspend fun rechercherExercicesDepuisApi(
        motCle: String,
        apiKey: String
    ): Result<List<ExerciceDTO>> {
        val recherche = motCle.trim()
        if (recherche.isEmpty()) {
            return Result.failure(IllegalArgumentException("Veuillez saisir un mot-clé."))
        }
        if (apiKey.isBlank()) {
            return Result.failure(IllegalArgumentException("La clé API RapidAPI n'est pas configurée."))
        }

        return try {
            val reponse = apiService.chercherExercices(
                motCle = recherche,
                apiKey = apiKey,
                langue = "fr",
                limite = 100
            )

            if (!reponse.success) {
                return Result.failure(Exception("La recherche MoveBase a échoué."))
            }
            Result.success(reponse.data)
        } catch (e: CancellationException) {
            throw e
        } catch (e: IOException) {
            Result.failure(Exception(
                "Impossible de joindre MoveBase. Vérifiez votre connexion Internet. " +
                    "Vos séances enregistrées restent accessibles et modifiables hors ligne.",
                e
            ))
        } catch (e: HttpException) {
            val message = when (e.code()) {
                401, 403 -> "Accès MoveBase refusé. Vérifiez votre clé et votre abonnement RapidAPI."
                429 -> "Limite de requêtes RapidAPI atteinte. Réessayez plus tard."
                else -> "Erreur du serveur MoveBase : ${e.code()}."
            }
            Result.failure(Exception(message, e))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun importerExercice(exercice: ExerciceDTO): Result<Unit> {
        return try {
            val notes = buildString {
                appendLine(exercice.instructions)
                appendLine()
                appendLine("Muscle ciblé : ${exercice.target}")
                appendLine("Équipement : ${exercice.equipment}")
                appendLine("Source MoveBase : ${exercice.id}")
                append(exercice.attribution)
            }

            ajouter(
                Entrainement(
                    id = 0,
                    titre = exercice.name,
                    activite = TypeActivite.MUSCULATION,
                    lieu = "Gym",
                    exterieur = false,
                    intensite = (1..10).random(),
                    notes = notes
                )
            )
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
