package com.example.tp1.repository

import com.example.tp1.data.Entrainement
import com.example.tp1.database.EntrainementDAO
import com.example.tp1.database.toEntity
import com.example.tp1.database.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomEntrainementRepository(
    private val entrainementDao: EntrainementDAO
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
}