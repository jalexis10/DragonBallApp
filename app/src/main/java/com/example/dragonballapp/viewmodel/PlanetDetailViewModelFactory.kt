package com.example.dragonballapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PlanetDetailViewModelFactory(private val planetId: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlanetDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PlanetDetailViewModel(planetId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}