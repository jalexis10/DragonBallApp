package com.example.dragonballapp.ui.screen

import androidx.compose.runtime.collectAsState
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
import com.example.dragonballapp.viewmodel.CharacterDetailViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dragonballapp.viewmodel.CharacterDetailViewModelFactory

@Composable
fun CharacterDetailScreen(
    characterId: Int,

    viewModel: CharacterDetailViewModel = viewModel(
        factory = CharacterDetailViewModelFactory(characterId) // <-- USA LA FACTORY
    )
) {
    val character by viewModel.character.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()



    Scaffold(
        topBar = {
            TopAppBar(title = { Text(character?.name ?: "Detalle de Personaje") })
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
            character?.let { ch ->
                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(16.dp)
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(ch.image),
                        contentDescription = ch.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Raza: ${ch.race}", style = MaterialTheme.typography.bodyMedium)
                    Text("Afiliación: ${ch.affiliation}", style = MaterialTheme.typography.bodyMedium)
                    Text(
                        "Planeta de origen: ${ch.originPlanet?.name ?: "Desconocido"}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(ch.description, style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.height(12.dp))

                    if (ch.transformations.isNotEmpty()) {
                        Text("Transformaciones:", style = MaterialTheme.typography.titleMedium)
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            items(
                                items = ch.transformations,
                                key = { transformation -> transformation.name }
                            ) { trans ->
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = MaterialTheme.shapes.medium
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Image(
                                            painter = rememberAsyncImagePainter(trans.image),
                                            contentDescription = trans.name,
                                            modifier = Modifier
                                                .size(80.dp)
                                                .padding(4.dp)
                                        )
                                        Column(Modifier.padding(8.dp)) {
                                            Text(
                                                text = trans.name,
                                                style = MaterialTheme.typography.titleSmall
                                            )
                                            Text(
                                                text = "Ki: ${trans.ki}",
                                                style = MaterialTheme.typography.bodySmall
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        Text("No tiene transformaciones conocidas.")
                    }
                }
            } ?: run {
                Box(
                    Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Personaje no encontrado o error al cargar.")
                }
            }
        }
    }
}