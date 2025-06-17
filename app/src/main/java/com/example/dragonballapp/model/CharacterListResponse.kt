
package com.example.dragonballapp.model



data class CharacterListResponse(
    val items: List<CharacterResponse>,
    val meta: MetaData
)

data class MetaData(
    val totalItems: Int,
    val itemCount: Int,
    val itemsPerPage: Int,
    val totalPages: Int,
    val currentPage: Int
)