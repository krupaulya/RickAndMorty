package com.example.rickandmorty.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.rickandmorty.data.model.CharactersResults
import com.example.rickandmorty.data.model.LocationsResults
import com.example.rickandmorty.domain.RickAndMortyRepository
import com.example.rickandmorty.getIDs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LocationsViewModel @Inject constructor(
    private val repository: RickAndMortyRepository
) : ViewModel() {

    private val _locations = MutableLiveData<List<LocationsResults>>()
    val locations: MutableLiveData<List<LocationsResults>> get() = _locations
    private val _location = MutableLiveData<LocationsResults>()
    val location: MutableLiveData<LocationsResults> get() = _location
    private val _characters = MutableLiveData<List<CharactersResults>>()
    val characters: MutableLiveData<List<CharactersResults>> get() = _characters

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading
    fun getLocations(): Flow<PagingData<LocationsResults>> =
        repository.getLocations()

    fun getLocationById(id: Int) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = repository.getLocationById(id)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        _location.postValue(response.body())
                        val characterIds = getIDs(response.body()!!.residents)
                        val charactersResponse = repository.getMultipleCharacters(characterIds)
                        _characters.postValue(charactersResponse.body())
                    }
                }
            } finally {
                _loading.value = false
            }

        }
    }
}