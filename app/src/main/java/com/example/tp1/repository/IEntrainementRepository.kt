package com.example.tp1.repository

import com.example.tp1.data.Entrainement
import com.example.tp1.network.ExerciceDTO
import kotlinx.coroutines.flow.Flow

interface IEntrainementRepository {
    suspend fun chargerEntrainements(): List<Entrainement>
    fun observerEntrainements(): Flow<List<Entrainement>>
    suspend fun obtenirParId(id: Int): Entrainement?
    suspend fun obtenirAvecExercices(id: Int): Entrainement?
    fun observerAvecExercices(id: Int): Flow<Entrainement?>
    suspend fun modifier(entrainement: Entrainement)
    suspend fun ajouter(entrainement: Entrainement)
    suspend fun basculerComplete(id: Int)
    suspend fun basculerFavori(id: Int)
    suspend fun supprimer(id: Int)

    suspend fun rechercherExercicesDepuisApi(motCle: String, apiKey: String): Result<List<ExerciceDTO>>
    suspend fun obtenirConsignesExercice(idExterne: String, apiKey: String): Result<ExerciceDTO>
}