package com.example.tp1.database

import com.example.tp1.data.Entrainement

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