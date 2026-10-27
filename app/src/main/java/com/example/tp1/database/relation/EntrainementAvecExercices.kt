package com.example.tp1.database.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.example.tp1.database.entity.EntrainementEntity
import com.example.tp1.database.entity.ExerciceSeanceEntity

data class EntrainementAvecExercices(
    @Embedded val entrainement: EntrainementEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "entrainementId"
    )
    val exercices: List<ExerciceSeanceEntity>
)
