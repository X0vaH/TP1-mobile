package com.example.tp1.database

import com.example.tp1.data.Entrainement
import com.example.tp1.data.TypeActivite
import com.example.tp1.network.ExerciceDTO

fun EntrainementEntity.toModel() : Entrainement {
    return Entrainement(
        id = this.id,
        titre = this.titre,
        activite = this.activite,
        lieu = this.lieu,
        exterieur = this.exterieur,
        intensite = this.intensite,
        notes = this.notes,
        estComplete = this.estComplete,
        estFavori = this.estFavori
    )
}

fun Entrainement.toEntity() : EntrainementEntity {
    return EntrainementEntity(
        id = this.id,
        titre = this.titre,
        activite = this.activite,
        lieu = this.lieu,
        exterieur = this.exterieur,
        intensite = this.intensite,
        notes = this.notes,
        estComplete = this.estComplete,
        estFavori = this.estFavori
    )
}

fun ExerciceDTO.toEntity(): EntrainementEntity {
    return EntrainementEntity(
        id = 0, // Auto-généré par Room
        titre = this.name,
        activite = TypeActivite.MUSCULATION,
        lieu = "Gym",
        exterieur = false,
        intensite = 0,
        notes = "",
        estComplete = false,
        estFavori = false
    )
}