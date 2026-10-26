package com.example.tp1.network

import kotlinx.serialization.Serializable

@Serializable
data class ExerciceDTO(
    val id: String,
    val slug: String,
    val name: String,
    val category: String,
    val equipment: String,
    val target: String,
    val muscleGroup: String,
    val secondaryMuscles: List<String>,
    val lang: String,
    val instructions: String,
    val instructionSteps: List<String>,
    val attribution: String,
    val imageUrl: String,
    val gifUrl: String,
    val createdAt: String
)

@Serializable
data class RechercheExercicesDTO(
    val success: Boolean,
    val data: List<ExerciceDTO>,
)

