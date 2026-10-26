package com.example.tp1.viewmodel

import com.example.tp1.data.Entrainement
import com.example.tp1.data.TypeActivite
import com.example.tp1.network.ExerciceDTO

data class EntrainementsUiState(
    val entrainements: List<Entrainement> = emptyList(),
    val rechercheQuery: String = "",
    val rechercheMoveBase: String = "",
    val isSearching: Boolean = false,
    val rechercheApi: RechercheMoveBaseUiState = RechercheMoveBaseUiState(),
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

// La recherche réseau ne modifie Room que lorsque l'utilisateur choisit Ajouter.
data class RechercheMoveBaseUiState(
    val query: String = "",
    val isSearching: Boolean = false,
    val erreur: String? = null,
    val resultats: List<ExerciceDTO> = emptyList(),
    val afficherResultats: Boolean = false,
    val idImportEnCours: String? = null,
    val idsImportes: Set<String> = emptySet()
)