package com.example.tp1.repository

import com.example.tp1.data.Entrainement
import kotlinx.coroutines.flow.Flow

interface IEntrainementRepository {
    suspend fun chargerEntrainements(): List<Entrainement>
    fun observerEntrainements(): Flow<List<Entrainement>>

    suspend fun ajouterEntrainement(entrainement: Entrainement)
}