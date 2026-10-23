package com.example.tp1.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.tp1.data.Entrainement
import kotlinx.coroutines.flow.Flow

@Dao
interface EntrainementDAO {
    @Query("SELECT * FROM entrainements ORDER BY titre ASC")
    fun obtenirTousLesEntrainements() : Flow<List<EntrainementEntity>>

    @Query("SELECT * FROM entrainements WHERE id = :id")
    suspend fun trouverParId(id: Int) : EntrainementEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insererEntrainement(entrainement: Entrainement)

    @Update
    suspend fun modifierEntrainement(entrainement: Entrainement)

    @Delete
    suspend fun supprimerEntrainement(entrainement: Entrainement)

    @Query("DELETE FROM entrainements WHERE id = :id")
    suspend fun supprimerParId(id: Int)

    @Query("SELECT * FROM entrainements WHERE id = :id")
    fun observerParId(id: Int) : Flow<EntrainementEntity?>

    @Query("SELECT * FROM entrainements ORDER BY titre ASC")
    suspend fun chargerTousLesEntrainements() : List<EntrainementEntity>
}