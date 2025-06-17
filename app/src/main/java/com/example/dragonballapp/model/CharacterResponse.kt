package com.example.dragonballapp.model

import com.google.gson.annotations.SerializedName

data class CharacterResponse(
    val id: Int,
    val name: String,
    val ki: String,
    @SerializedName("maxKi")
    val maxKi: String,
    val gender: String,
    val description: String,
    val image: String

)