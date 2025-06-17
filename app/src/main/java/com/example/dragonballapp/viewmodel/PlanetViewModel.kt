package com.example.dragonballapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dragonballapp.model.PlanetResponse
import com.example.dragonballapp.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PlanetViewModel : ViewModel() {

    private val _planets = MutableStateFlow<List<PlanetResponse>>(emptyList())
    val planets: StateFlow<List<PlanetResponse>> = _planets

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun getPlanets() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = RetrofitInstance.api.getPlanets()
                _planets.value = response.items
            } catch (e: Exception) {
                e.printStackTrace()
            }
            _isLoading.value = false
        }
    }
}
