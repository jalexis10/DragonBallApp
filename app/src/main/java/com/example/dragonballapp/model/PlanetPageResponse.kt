package com.example.dragonballapp.model

data class PlanetPageResponse(
    val items: List<PlanetResponse>,
    val meta: Meta,
    val links: Links
)


