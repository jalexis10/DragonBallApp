package com.example.dragonballapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CharacterDetailViewModelFactory(private val characterId: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CharacterDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CharacterDetailViewModel(characterId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}