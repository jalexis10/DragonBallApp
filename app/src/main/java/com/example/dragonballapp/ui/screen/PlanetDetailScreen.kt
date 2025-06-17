package com.example.dragonballapp.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

import com.example.dragonballapp.viewmodel.PlanetDetailViewModel
import com.example.dragonballapp.ui.component.CharacterCard
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dragonballapp.viewmodel.PlanetDetailViewModelFactory

@Composable
fun PlanetDetailScreen(
    planetId: Int,
    onCharacterClick: (Int) -> Unit,
    // Usa la factory para instanciar el ViewModel
    viewModel: PlanetDetailViewModel = viewModel(
        factory = PlanetDetailViewModelFactory(planetId) // <-- USA LA FACTORY
    )
) {
    val planet by viewModel.planet.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()



    Scaffold(
        topBar = {
            TopAppBar(title = { Text(planet?.name ?: "Detalle de Planeta") })
        }
    ) { paddingValues ->
        if (isLoading) {
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            planet?.let { p ->
                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(16.dp)
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(p.image),
                        contentDescription = p.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(p.description, style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(12.dp))


                    if (!p.characters.isNullOrEmpty()) {
                        Text("Habitantes:", style = MaterialTheme.typography.titleMedium)
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            items(p.characters) { character ->
                                CharacterCard(
                                    character = character,
                                    onClick = { onCharacterClick(character.id) }
                                )
                            }
                        }
                    } else {
                        Text("No se conocen habitantes.", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            } ?: run {
                Box(
                    Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Planeta no encontrado o error al cargar.")
                }
            }
        }
    }
}