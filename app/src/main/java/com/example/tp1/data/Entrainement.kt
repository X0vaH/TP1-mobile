package com.example.tp1.data

data class Entrainement(
    val id: Int,
    val titre: String,
    val activite: TypeActivite,
    val lieu: String,
    val exterieur: Boolean,
    val intensite: Int,
    val notes: String,
    val estComplete: Boolean =false,
    val estFavori: Boolean = false
)
