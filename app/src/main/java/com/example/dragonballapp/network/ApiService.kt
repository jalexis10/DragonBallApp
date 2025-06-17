package com.example.dragonballapp.network

import com.example.dragonballapp.model.CharacterDetailResponse
import com.example.dragonballapp.model.CharacterPageResponse
import com.example.dragonballapp.model.CharacterResponse

import com.example.dragonballapp.model.PlanetDetailResponse
import com.example.dragonballapp.model.PlanetPageResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {


    @GET("api/characters")
    suspend fun getCharactersPaginated(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 10
    ): CharacterPageResponse


    @GET("api/characters")
    suspend fun getFilteredCharacters(

        @Query("name") name: String? = null,
        @Query("race") race: String? = null,
        @Query("gender") gender: String? = null,
        @Query("affiliation") affiliation: String? = null
    ): List<CharacterResponse>

    @GET("api/characters/{id}")
    suspend fun getCharacterById(@Path("id") id: Int): CharacterDetailResponse


    @GET("api/planets")
    suspend fun getPlanets(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 10
    ): PlanetPageResponse

    @GET("api/planets/{id}")
    suspend fun getPlanetById(@Path("id") id: Int): PlanetDetailResponse
}