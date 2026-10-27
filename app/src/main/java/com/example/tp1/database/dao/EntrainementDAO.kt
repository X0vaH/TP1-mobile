package com.example.tp1.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Transaction
import com.example.tp1.database.entity.EntrainementEntity
import com.example.tp1.database.relation.EntrainementAvecExercices
import kotlinx.coroutines.flow.Flow

@Dao
interface EntrainementDAO {
    @Query("SELECT * FROM entrainements ORDER BY titre ASC")
    fun obtenirTousLesEntrainements() : Flow<List<EntrainementEntity>>

    @Query("SELECT * FROM entrainements WHERE id = :id")
    suspend fun trouverParId(id: Int) : EntrainementEntity?

    @Insert
    suspend fun insererEntrainement(entrainement: EntrainementEntity): Long

    @Update
    suspend fun modifierEntrainement(entrainement: EntrainementEntity)

    @Delete
    suspend fun supprimerEntrainement(entrainement: EntrainementEntity)

    @Query("DELETE FROM entrainements WHERE id = :id")
    suspend fun supprimerParId(id: Int)

    @Query("SELECT * FROM entrainements WHERE id = :id")
    fun observerParId(id: Int) : Flow<EntrainementEntity?>

    @Query("SELECT * FROM entrainements ORDER BY titre ASC")
    suspend fun chargerTousLesEntrainements() : List<EntrainementEntity>

    @Transaction
    @Query("SELECT * FROM entrainements WHERE id = :id")
    suspend fun getEntrainementAvecExercices(id: Int) : EntrainementAvecExercices?

    @Transaction
    @Query("SELECT * FROM entrainements WHERE id = :id")
    fun observerEntrainementAvecExercices(id: Int) : Flow<EntrainementAvecExercices?>
}
