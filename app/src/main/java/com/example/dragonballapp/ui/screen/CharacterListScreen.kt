package com.example.dragonballapp.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dragonballapp.viewmodel.CharacterViewModel
import com.example.dragonballapp.ui.component.CharacterCard
import com.example.dragonballapp.ui.component.DropdownFilter

@Composable
fun CharacterListScreen(
    onCharacterClick: (Int) -> Unit,
    onPlanetSectionClick: () -> Unit,
    viewModel: CharacterViewModel = viewModel()
) {
    val charactersList by viewModel.characters.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isLoadingNextPage by viewModel.isLoadingNextPage.collectAsState()

    val raceOptions by viewModel.availableRaces.collectAsState()
    val selectedRace by viewModel.selectedRace.collectAsState()

    val affiliationOptions by viewModel.availableAffiliations.collectAsState()
    val selectedAffiliation by viewModel.selectedAffiliation.collectAsState()

    val listState = rememberLazyListState()

    Scaffold(
        topBar = {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                TopAppBar(
                    title = { Text("Personajes de Dragon Ball") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))

                DropdownFilter(
                    label = "Raza",
                    options = raceOptions,
                    selectedOption = selectedRace,
                    onOptionSelected = { optionFromDropdown ->
                        viewModel.onRaceSelected(optionFromDropdown)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                DropdownFilter(
                    label = "Afiliación",
                    options = affiliationOptions,
                    selectedOption = selectedAffiliation,
                    onOptionSelected = { optionFromDropdown ->
                        viewModel.onAffiliationSelected(optionFromDropdown)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = onPlanetSectionClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Ver Planetas")
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    ) { paddingValues ->

        Column(modifier = Modifier.padding(paddingValues)) {
            if (isLoading && charactersList.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (charactersList.isEmpty() && !isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "No se encontraron personajes. Prueba con otros filtros o revisa tu conexión.",
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyColumn(
                    state = listState,
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(charactersList, key = { it.id }) { character ->
                        CharacterCard(
                            character = character,
                            onClick = { onCharacterClick(character.id) }
                        )
                    }

                    if (isLoadingNextPage) {
                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                }

                LaunchedEffect(listState) {
                    snapshotFlow { listState.layoutInfo.visibleItemsInfo }
                        .collect { visibleItems ->
                            if (visibleItems.isNotEmpty() && !isLoading && !isLoadingNextPage) {
                                val lastVisibleItemIndex = visibleItems.last().index
                                if (lastVisibleItemIndex >= charactersList.size - 5 && viewModel.currentPage < viewModel.totalPages) {
                                    viewModel.loadNextPage()
                                }
                            }
                        }
                }
            }
        }
    }
}