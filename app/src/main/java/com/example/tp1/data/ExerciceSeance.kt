package com.example.tp1.data

data class ExerciceSeance(
    val id: Int = 0,
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
