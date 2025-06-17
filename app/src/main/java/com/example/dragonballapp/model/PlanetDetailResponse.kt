package com.example.dragonballapp.model

data class PlanetDetailResponse(
    val id: Int,
    val name: String,
    val image: String,
    val description: String,
    val isDestroyed: Boolean,
    val characters: List<CharacterResponse>
)
