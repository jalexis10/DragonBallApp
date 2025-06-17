package com.example.dragonballapp.model
data class PlanetResponse(
    val id: Int,
    val name: String,
    val description: String,
    val image: String,
    val isDestroyed: Boolean,
    val characters: List<CharacterResponse>? = null
)
