package com.example.tp1.viewmodel

import com.example.tp1.data.Entrainement
import com.example.tp1.data.TypeActivite

data class EntrainementsUiState(
    val entrainements: List<Entrainement> = emptyList(),
    val rechercheQuery: String = "",
    val filtreFavoris: Boolean = false,
    val typeActiviteSelectionne: TypeActivite? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val entrainementSelectionne: Entrainement? = null
) {
    val entrainementsFiltres: List<Entrainement>
        get() {
            return entrainements.filter { entrainement ->
                val matchRecherche = rechercheQuery.isBlank() || entrainement.titre.contains(rechercheQuery, ignoreCase = true)
                val matchFavoris = !filtreFavoris || entrainement.estFavori
                val matchActivite = typeActiviteSelectionne == null || entrainement.activite == typeActiviteSelectionne
                matchRecherche && matchFavoris && matchActivite
            }
        }
}
