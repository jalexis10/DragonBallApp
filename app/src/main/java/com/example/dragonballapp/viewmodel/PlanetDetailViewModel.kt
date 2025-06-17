package com.example.dragonballapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dragonballapp.model.PlanetDetailResponse
import com.example.dragonballapp.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlanetDetailViewModel(planetId: Int) : ViewModel() {

    private val _planet = MutableStateFlow<PlanetDetailResponse?>(null)
    val planet: StateFlow<PlanetDetailResponse?> = _planet.asStateFlow() // Usa asStateFlow

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow() // Usa asStateFlow

    init {
        getPlanetById(planetId)
    }

    fun getPlanetById(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = RetrofitInstance.api.getPlanetById(id)
                _planet.value = response
            } catch (e: Exception) {
                _planet.value = null
                e.printStackTrace()

            } finally {
                _isLoading.value = false
            }
        }
    }
}