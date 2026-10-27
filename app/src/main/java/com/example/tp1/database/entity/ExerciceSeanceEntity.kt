package com.example.tp1.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "exercices_seance",
    foreignKeys = [
        ForeignKey(
            entity = EntrainementEntity::class,
            parentColumns = ["id"],
            childColumns = ["entrainementId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["entrainementId"])]
)
data class ExerciceSeanceEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val entrainementId: Int,
    // Informations provenant de MoveBase
    val idExterne: String,
    val nom: String,
    val cible: String,
    val equipement: String,
    // Informations propres à la séance
    val series: Int,
    val repetitions: Int,
    val charge: Double
)
