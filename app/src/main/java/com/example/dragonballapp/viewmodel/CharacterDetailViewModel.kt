package com.example.dragonballapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dragonballapp.model.CharacterDetailResponse
import com.example.dragonballapp.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CharacterDetailViewModel(characterId: Int) : ViewModel() {

    private val _character = MutableStateFlow<CharacterDetailResponse?>(null)
    val character: StateFlow<CharacterDetailResponse?> = _character

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        getCharacterById(characterId)
    }

    fun getCharacterById(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = RetrofitInstance.api.getCharacterById(id)
                _character.value = response
            } catch (e: Exception) {
                e.printStackTrace()
            }
            _isLoading.value = false
        }
    }
}
