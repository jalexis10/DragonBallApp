package com.example.dragonballapp.model

data class CharacterPageResponse(
    val items: List<CharacterResponse>,
    val meta: Meta,
    val links: Links
)

data class Meta(
    val totalItems: Int,
    val itemCount: Int,
    val itemsPerPage: Int,
    val totalPages: Int,
    val currentPage: Int
)

data class Links(
    val first: String,
    val previous: String?,
    val next: String?,
    val last: String
)
