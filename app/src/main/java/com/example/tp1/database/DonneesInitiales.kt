package com.example.tp1.database

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

// Séances de départ, insérées seulement à la création de la base de données
object DonneesInitiales : RoomDatabase.Callback() {

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)

        db.execSQL(
            "INSERT INTO entrainements (id, titre, activite, lieu, exterieur, intensite, notes, estComplete, estFavori) " +
                "VALUES (1, 'Course à l''extérieur', 'COURSE', 'Piste de course', 1, 8, 'Apporter équipements pour la course', 0, 0)"
        )
        db.execSQL(
            "INSERT INTO entrainements (id, titre, activite, lieu, exterieur, intensite, notes, estComplete, estFavori) " +
                "VALUES (2, 'Vélo à l''extérieur', 'VELO', 'Piste cyclable', 1, 7, 'Apporter un casque', 0, 0)"
        )
        db.execSQL(
            "INSERT INTO entrainements (id, titre, activite, lieu, exterieur, intensite, notes, estComplete, estFavori) " +
                "VALUES (3, 'Muscu', 'MUSCULATION', 'Gym', 0, 9, 'Étirez-vous avant', 0, 0)"
        )
        db.execSQL(
            "INSERT INTO exercices_seance (entrainementId, idExterne, nom, cible, equipement, series, repetitions, charge) " +
                "VALUES (3, '0024', 'barbell bench front squat', 'quads', 'barbell', 3, 10, 60.0)"
        )
    }
}
