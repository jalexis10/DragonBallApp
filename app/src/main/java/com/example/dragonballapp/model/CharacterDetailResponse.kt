package com.example.dragonballapp.model
data class CharacterDetailResponse(
    val id: Int,
    val name: String,
    val race: String,
    val gender: String,
    val affiliation: String,
    val description: String,
    val image: String,
    val originPlanet: PlanetShortResponse,
    val transformations: List<TransformationResponse>
)

data class PlanetShortResponse(
    val id: Int,
    val name: String,
    val image: String,
    val description: String
)

data class TransformationResponse(
    val name: String,
    val ki: String,
    val image: String
)
