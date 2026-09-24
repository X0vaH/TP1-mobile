package com.example.tp1.ui.theme.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.AndroidUiModes
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tp1.R
import com.example.tp1.data.TypeActivite
import com.example.tp1.ui.theme.TrainingApp
import com.example.tp1.viewmodel.CreateEntrainementsUiState
import com.example.tp1.viewmodel.CreateEntrainementsViewModel
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEntrainementScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onEntrainementCreated: () -> Unit,
    viewModel: CreateEntrainementsViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.creer_entrainement_cd)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.retour)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        CreateEntrainementContent(
            uiState = uiState,
            modifier = modifier.padding(innerPadding),
            onTitreChange = viewModel::modifierTitre,
            onSaveClick = {
                viewModel.creerEntrainement(onEntrainementCreated)
            },
            onActiviteChange = viewModel::modifierActivite,
            onLieuChange = viewModel::modifierLieu,
            onExterieurChange = viewModel::modifierExterieur,
            onIntensiteChange = viewModel::modifierIntensite,
            onNotesChange = viewModel::modifierNotes
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEntrainementContent(
    uiState: CreateEntrainementsUiState,
    modifier: Modifier = Modifier,
    onTitreChange: (String) -> Unit,
    onSaveClick: () -> Unit,
    onActiviteChange: (TypeActivite) -> Unit,
    onLieuChange: (String) -> Unit,
    onExterieurChange: (Boolean) -> Unit,
    onIntensiteChange: (Int) -> Unit,
    onNotesChange: (String) -> Unit
) {
    val isFormValid = uiState.titre.isNotBlank() &&
            uiState.activite != null &&
            uiState.lieu.isNotBlank() &&
            uiState.erreurTitre == null &&
            uiState.erreurActivite == null &&
            uiState.erreurLieu == null &&
            uiState.erreurNotes == null

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Titre
        OutlinedTextField(
            value = uiState.titre,
            onValueChange = onTitreChange,
            label = { Text(stringResource(R.string.titre_form_lbl)) },
            isError = uiState.erreurTitre != null,
            supportingText = {
                uiState.erreurTitre?.let { Text(stringResource(R.string.error_title)) }
            },
            modifier = Modifier.fillMaxWidth()
        )

        // Activité Dropdown
        var dropDownExpanded by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(
            expanded = dropDownExpanded,
            onExpandedChange = { dropDownExpanded = !dropDownExpanded }
        ) {
            OutlinedTextField(
                value = uiState.activite?.let { stringResource(it.labelRes) } ?: "",
                onValueChange = {},
                readOnly = true,
                label = { Text(stringResource(R.string.activite_form_lbl)) },
                isError = uiState.erreurActivite != null,
                supportingText = {
                    uiState.erreurActivite?.let { Text(it) }
                },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(dropDownExpanded) },
                modifier = Modifier
                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = dropDownExpanded,
                onDismissRequest = { dropDownExpanded = false }
            ) {
                TypeActivite.entries.forEach { activite ->
                    DropdownMenuItem(
                        text = { Text(stringResource(activite.labelRes)) },
                        onClick = {
                            onActiviteChange(activite)
                            dropDownExpanded = false
                        }
                    )
                }
            }
        }

        // Lieu
        OutlinedTextField(
            value = uiState.lieu,
            onValueChange = onLieuChange,
            label = { Text(stringResource(R.string.lieu_form_lbl)) },
            isError = uiState.erreurLieu != null,
            supportingText = {
                uiState.erreurLieu?.let { Text(it) }
            },
            modifier = Modifier.fillMaxWidth()
        )

        // Location Type (Extérieur / Intérieur)
        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
            SegmentedButton(
                selected = uiState.exterieur,
                onClick = { onExterieurChange(true) },
                shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2)
            ) { Text(stringResource(R.string.lieu_exterieur)) }

            SegmentedButton(
                selected = !uiState.exterieur,
                onClick = { onExterieurChange(false) },
                shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2)
            ) { Text(stringResource(R.string.lieu_interieur)) }
        }

        // Intensité
        Text(
            stringResource(R.string.intensite_form_lbl, uiState.intensite),
            style = MaterialTheme.typography.labelLarge
        )
        Slider(
            value = uiState.intensite.toFloat(),
            onValueChange = { onIntensiteChange(it.roundToInt()) },
            valueRange = 0f..10f,
            steps = 9,
            modifier = Modifier.fillMaxWidth()
        )

        // Notes
        OutlinedTextField(
            value = uiState.notes,
            onValueChange = onNotesChange,
            label = { Text(stringResource(R.string.label_notes)) },
            isError = uiState.erreurNotes != null,
            supportingText = {
                uiState.erreurNotes?.let { Text(it) }
            },
            minLines = 3,
            maxLines = 6,
            modifier = Modifier.fillMaxWidth()
        )

        // Save Button
        Button(
            onClick = onSaveClick,
            enabled = isFormValid && !uiState.isSaving,
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(stringResource(R.string.creer_form_btn_txt))
        }

        // Global Error Message
        uiState.erreur?.let {
            Text(
                text = stringResource(R.string.erreur_form_lbl, it),
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Preview(uiMode = AndroidUiModes.UI_MODE_NIGHT_NO)
@Composable
fun CreateEntrainementScreenPreview() {
    TrainingApp {
        CreateEntrainementContent(
            uiState = CreateEntrainementsUiState(
                titre = "Course matinale",
                activite = TypeActivite.COURSE,
                lieu = "Parc",
                exterieur = true,
                intensite = 6,
                notes = "Belle sortie"
            ),
            onTitreChange = {},
            onSaveClick = {},
            onActiviteChange = {},
            onLieuChange = {},
            onExterieurChange = {},
            onIntensiteChange = {},
            onNotesChange = {}
        )
    }
}