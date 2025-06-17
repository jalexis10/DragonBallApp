package com.example.dragonballapp.viewmodel

import android.util.Log

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dragonballapp.model.CharacterPageResponse
import com.example.dragonballapp.model.CharacterResponse

import com.example.dragonballapp.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CharacterViewModel : ViewModel() {

    private val _characters = MutableStateFlow<List<CharacterResponse>>(emptyList())
    val characters: StateFlow<List<CharacterResponse>> = _characters.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isLoadingNextPage = MutableStateFlow(false)
    val isLoadingNextPage: StateFlow<Boolean> = _isLoadingNextPage.asStateFlow()

    var currentPage by mutableStateOf(1)
        private set
    var totalPages by mutableStateOf(1)
        private set

    private val _currentQuery = MutableStateFlow<String?>(null)
    private val _selectedRace = MutableStateFlow<String?>(null)
    val selectedRace: StateFlow<String?> = _selectedRace.asStateFlow()
    private val _selectedAffiliation = MutableStateFlow<String?>(null)
    val selectedAffiliation: StateFlow<String?> = _selectedAffiliation.asStateFlow()

    private val _availableRaces = MutableStateFlow(
        listOf(
            "",
            "Human", "Saiyan", "Namekian", "Majin", "Frieza Race",
            "Android", "Jiren Race", "God", "Angel", "Evil",
            "Nucleico", "Nucleico benigno", "Unknown"
        )
    )
    val availableRaces: StateFlow<List<String>> = _availableRaces.asStateFlow()

    private val _availableAffiliations = MutableStateFlow(
        listOf(
            "",
            "Z Fighter", "Red Ribbon Army", "Namekian Warrior", "Freelancer",
            "Army of Frieza", "Pride Troopers", "Assistant of Vermoud",
            "God", "Assistant of Beerus", "Villain", "Other"
        )
    )
    val availableAffiliations: StateFlow<List<String>> = _availableAffiliations.asStateFlow()

    private val itemsPerPage = 20

    init {

        fetchCharacters(isInitialLoadOrNewFilter = true)
    }

    private fun hasActiveFilters(): Boolean {
        return !_currentQuery.value.isNullOrBlank() ||
                !_selectedRace.value.isNullOrBlank() ||
                !_selectedAffiliation.value.isNullOrBlank()

    }

    fun fetchCharacters(
        pageToLoad: Int = 1,
        isInitialLoadOrNewFilter: Boolean = false,
        isLoadingNext: Boolean = false
    ) {
        viewModelScope.launch {
            val localQuery = _currentQuery.value?.trim()?.takeIf { it.isNotEmpty() }
            val localRace = _selectedRace.value?.takeIf { it.isNotEmpty() }
            val localAffiliation = _selectedAffiliation.value?.takeIf { it.isNotEmpty() }


            if (hasActiveFilters()) {

                if (_isLoading.value) return@launch
                _isLoading.value = true
                Log.d("CharacterViewModel", "Fetching FILTERED characters: query=$localQuery, race=$localRace, affiliation=$localAffiliation")

                try {
                    val filteredList: List<CharacterResponse> = RetrofitInstance.api.getFilteredCharacters(
                        name = localQuery,
                        race = localRace,
                        affiliation = localAffiliation

                    )
                    _characters.value = filteredList
                    currentPage = 1
                    totalPages = 1
                    Log.d("CharacterViewModel", "Filtered API response items count: ${filteredList.size}")

                } catch (e: Exception) {
                    Log.e("CharacterViewModel", "Error fetching FILTERED characters", e)
                    _characters.value = emptyList()
                    currentPage = 1
                    totalPages = 1
                } finally {
                    _isLoading.value = false
                }

            } else {

                if (isLoadingNext) {
                    if (_isLoading.value || _isLoadingNextPage.value) return@launch
                    _isLoadingNextPage.value = true
                } else {
                    if (_isLoading.value) return@launch
                    _isLoading.value = true
                }
                Log.d("CharacterViewModel", "Fetching PAGINATED characters: page=$pageToLoad")

                try {

                    val paginatedResponse: CharacterPageResponse = RetrofitInstance.api.getCharactersPaginated(
                        page = pageToLoad,
                        limit = itemsPerPage
                    )
                    val charactersList = paginatedResponse.items

                    if (isInitialLoadOrNewFilter || pageToLoad == 1) {
                        _characters.value = charactersList
                    } else {
                        _characters.value = _characters.value + charactersList
                    }


                    currentPage = paginatedResponse.meta.currentPage
                    totalPages = paginatedResponse.meta.totalPages
                    Log.d("CharacterViewModel", "Paginated API response: Items=${charactersList.size}, CurrentP=${currentPage}, TotalP=${totalPages}")

                } catch (e: Exception) {
                    Log.e("CharacterViewModel", "Error fetching PAGINATED characters", e)
                    if (isInitialLoadOrNewFilter || pageToLoad == 1) {
                        _characters.value = emptyList()
                        currentPage = 1
                        totalPages = 1
                    }
                } finally {
                    if (isLoadingNext) {
                        _isLoadingNextPage.value = false
                    } else {
                        _isLoading.value = false
                    }
                }
            }
        }
    }


    fun onQueryChanged(newQuery: String?) {
        val trimmedQuery = newQuery?.trim()
        if (_currentQuery.value == trimmedQuery && (trimmedQuery != null || _currentQuery.value == null)) return
        _currentQuery.value = trimmedQuery
        fetchCharacters(pageToLoad = 1, isInitialLoadOrNewFilter = true)
    }

    fun onRaceSelected(race: String?) {
        val effectiveRace = race?.takeIf { it.isNotEmpty() }
        _selectedRace.value = effectiveRace
        fetchCharacters(pageToLoad = 1, isInitialLoadOrNewFilter = true)
    }

    fun onAffiliationSelected(affiliation: String?) {
        val effectiveAffiliation = affiliation?.takeIf { it.isNotEmpty() }
        if (_selectedAffiliation.value == effectiveAffiliation) return
        _selectedAffiliation.value = effectiveAffiliation
        fetchCharacters(pageToLoad = 1, isInitialLoadOrNewFilter = true)
    }


    fun loadNextPage() {
        if (!hasActiveFilters() && currentPage < totalPages && !_isLoading.value && !_isLoadingNextPage.value) {
            Log.d("CharacterViewModel", "loadNextPage called for PAGINATED. Current: $currentPage, Total: $totalPages")
            fetchCharacters(
                pageToLoad = currentPage + 1,
                isLoadingNext = true
            )
        } else {
            if (hasActiveFilters()) {
                Log.d("CharacterViewModel", "loadNextPage: Filters are active, no pagination for filtered results.")
            } else if (currentPage >= totalPages) {
                Log.d("CharacterViewModel", "loadNextPage: Already on the last page or no more pages. Current: $currentPage, Total: $totalPages")
            } else {
                Log.d("CharacterViewModel", "loadNextPage: Cannot load next page. Loading: ${_isLoading.value}, LoadingNext: ${_isLoadingNextPage.value}")
            }
        }
    }
}