package com.example.tp1.database

import com.example.tp1.data.Entrainement
import com.example.tp1.data.ExerciceSeance
import com.example.tp1.database.entity.EntrainementEntity
import com.example.tp1.database.entity.ExerciceSeanceEntity
import com.example.tp1.database.relation.EntrainementAvecExercices

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

fun ExerciceSeanceEntity.toModel(): ExerciceSeance {
    return ExerciceSeance(
        id = this.id,
        idExterne = this.idExterne,
        nom = this.nom,
        cible = this.cible,
        equipement = this.equipement,
        series = this.series,
        repetitions = this.repetitions,
        charge = this.charge
    )
}

fun ExerciceSeance.toEntity(entrainementId: Int): ExerciceSeanceEntity {
    return ExerciceSeanceEntity(
        id = 0, // Auto-généré par Room
        entrainementId = entrainementId,
        idExterne = this.idExterne,
        nom = this.nom,
        cible = this.cible,
        equipement = this.equipement,
        series = this.series,
        repetitions = this.repetitions,
        charge = this.charge
    )
}

fun EntrainementAvecExercices.toModel(): Entrainement {
    return entrainement.toModel().copy(
        exercices = exercices.map { it.toModel() }
    )
}
