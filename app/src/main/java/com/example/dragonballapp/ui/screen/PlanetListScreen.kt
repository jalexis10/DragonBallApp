package com.example.dragonballapp.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dragonballapp.viewmodel.PlanetViewModel
import com.example.dragonballapp.model.PlanetResponse
import com.example.dragonballapp.ui.component.PlanetCard

@Composable
fun PlanetListScreen(
    onPlanetClick: (PlanetResponse) -> Unit,
    viewModel: PlanetViewModel = viewModel()
) {
    val planets by viewModel.planets.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getPlanets()
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Planetas de Dragon Ball") }) }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            if (isLoading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(contentPadding = PaddingValues(16.dp)) {
                    items(planets) { planet ->
                        PlanetCard(
                            planet = planet,
                            onClick = { onPlanetClick(planet) }
                        )
                    }
                }
            }
        }
    }
}