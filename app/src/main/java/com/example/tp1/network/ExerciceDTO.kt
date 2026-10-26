package com.example.tp1.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExerciceDTO(
    @SerialName("id") val id: String,
    @SerialName("slug") val slug: String,
    @SerialName("name") val name: String,
    @SerialName("instructions") val instructions: String,
    @SerialName("target") val target: String,
    @SerialName("category") val category: String,
    @SerialName("equipment") val equipment: String,
    @SerialName("lang") val lang: String,
    @SerialName("attribution") val attribution: String,
    @SerialName("createdAt") val createdAt: String
)

@Serializable
data class RechercheExercicesDTO(
    val success: Boolean,
    val data: List<ExerciceDTO>,
)

