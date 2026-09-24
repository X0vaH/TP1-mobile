package com.example.tp1.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.AndroidUiModes
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.tp1.R
import com.example.tp1.data.Entrainement
import com.example.tp1.repository.FakeListEntrainement
import com.example.tp1.ui.theme.TrainingApp
import com.example.tp1.ui.components.ConfirmationSuppressionDialog
import com.example.tp1.ui.components.DetailChamp
import com.example.tp1.viewmodel.EntrainementDetailUiState
import com.example.tp1.viewmodel.EntrainementsViewModel

@Composable
fun EntrainementDetailScreen(
    id: Int,
    viewModel: EntrainementsViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.detailUiState.collectAsStateWithLifecycle()

    LaunchedEffect(id) { viewModel.chargerDetail(id) }

    EntrainementDetailContent(
        uiState = uiState,
        onBack = onBack,
        onCompleteChange = { viewModel.basculerComplete(id) },
        onSupprimerClick = viewModel::afficherDialogueSuppression,
        onAnnulerSuppression = viewModel::masquerDialogueSuppression,
        onConfirmerSuppression = {
            viewModel.confirmerSuppression()
            onBack()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntrainementDetailContent(
    uiState: EntrainementDetailUiState,
    onBack: () -> Unit,
    onCompleteChange: () -> Unit,
    onSupprimerClick: () -> Unit,
    onAnnulerSuppression: () -> Unit,
    onConfirmerSuppression: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.txt_details)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.retour)
                        )
                    }
                },
                actions = {
                    if (uiState.entrainement != null) {
                        IconButton(onClick = onSupprimerClick) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = stringResource(R.string.supprimer)
                            )
                        }
                    }
                }
            )
        }
    ) { padding ->
        when {
            uiState.isLoading -> CentreEcran(padding) { CircularProgressIndicator() }
            uiState.entrainement == null -> CentreEcran(padding) {
                Text(stringResource(R.string.entrainement_introuvable))
            }
            else -> DetailCorps(uiState.entrainement, padding, onCompleteChange)
        }
    }

    if (uiState.afficherDialogueSuppression) {
        uiState.entrainement?.let { entrainement ->
            ConfirmationSuppressionDialog(
                titreEntrainement = entrainement.titre,
                onAnnuler = onAnnulerSuppression,
                onConfirmer = onConfirmerSuppression
            )
        }
    }
}

@Composable
private fun CentreEcran(padding: PaddingValues, content: @Composable () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize().padding(padding),
        contentAlignment = Alignment.Center
    ) { content() }
}

@Composable
private fun DetailCorps(
    entrainement: Entrainement,
    padding: PaddingValues,
    onCompleteChange: () -> Unit
) {
    val typeLieu = stringResource(
        if (entrainement.exterieur) R.string.lieu_exterieur else R.string.lieu_interieur
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = entrainement.titre,
            style = MaterialTheme.typography.headlineMedium
        )
        DetailChamp(
            label = stringResource(R.string.label_activite),
            valeur = stringResource(entrainement.activite.labelRes)
        )
        DetailChamp(
            label = stringResource(R.string.label_lieu),
            valeur = stringResource(R.string.lieu_avec_type, entrainement.lieu, typeLieu)
        )
        DetailChamp(
            label = stringResource(R.string.label_intensite),
            valeur = stringResource(R.string.intensite_valeur, entrainement.intensite)
        )
        DetailChamp(
            label = stringResource(R.string.label_notes),
            valeur = entrainement.notes
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(stringResource(R.string.seance_completee))
            Switch(
                checked = entrainement.estComplete,
                onCheckedChange = { onCompleteChange() }
            )
        }
    }
}

@Preview(name = "Clair")
@Preview(name = "Sombre", uiMode = AndroidUiModes.UI_MODE_NIGHT_YES)
@Composable
fun EntrainementDetailPreview() {
    TrainingApp {
        EntrainementDetailContent(
            uiState = EntrainementDetailUiState(
                entrainement = FakeListEntrainement.liste.first(),
                isLoading = false
            ),
            onBack = {},
            onCompleteChange = {},
            onSupprimerClick = {},
            onAnnulerSuppression = {},
            onConfirmerSuppression = {}
        )
    }
}

@Preview(name = "Dialogue suppression")
@Composable
fun EntrainementDetailDialoguePreview() {
    TrainingApp {
        EntrainementDetailContent(
            uiState = EntrainementDetailUiState(
                entrainement = FakeListEntrainement.liste.first(),
                isLoading = false,
                afficherDialogueSuppression = true
            ),
            onBack = {},
            onCompleteChange = {},
            onSupprimerClick = {},
            onAnnulerSuppression = {},
            onConfirmerSuppression = {}
        )
    }
}

@Preview(name = "Introuvable")
@Composable
fun EntrainementDetailIntrouvablePreview() {
    TrainingApp {
        EntrainementDetailContent(
            uiState = EntrainementDetailUiState(entrainement = null, isLoading = false),
            onBack = {},
            onCompleteChange = {},
            onSupprimerClick = {},
            onAnnulerSuppression = {},
            onConfirmerSuppression = {}
        )
    }
}