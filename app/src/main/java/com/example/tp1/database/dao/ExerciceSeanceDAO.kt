package com.example.tp1.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.tp1.database.entity.ExerciceSeanceEntity

@Dao
interface ExerciceSeanceDAO {
    @Insert
    suspend fun insererExercices(exercices: List<ExerciceSeanceEntity>)

    @Query("DELETE FROM exercices_seance WHERE entrainementId = :entrainementId")
    suspend fun supprimerParEntrainement(entrainementId: Int)
}
