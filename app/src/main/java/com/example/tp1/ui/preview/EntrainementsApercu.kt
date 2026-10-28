package com.example.tp1.ui.preview

import com.example.tp1.data.Entrainement
import com.example.tp1.data.TypeActivite

// Données utilisées seulement par les aperçus Compose
val entrainementsApercu = listOf(
    Entrainement(
        id = 1, titre = "Course à l'extérieur", activite = TypeActivite.COURSE,
        lieu = "Piste de course", exterieur = true, intensite = 8,
        notes = "Apporter équipements pour la course"
    ),
    Entrainement(
        id = 2, titre = "Vélo à l'extérieur", activite = TypeActivite.VELO,
        lieu = "Piste cyclable", exterieur = true, intensite = 7,
        notes = "Apporter un casque"
    ),
    Entrainement(
        id = 3, titre = "Muscu", activite = TypeActivite.MUSCULATION,
        lieu = "Gym", exterieur = false, intensite = 9,
        notes = "Étirez-vous avant"
    )
)
