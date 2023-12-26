package com.example.rickandmorty.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.rickandmorty.ConnectivityUtils
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
    private val repository: RickAndMortyRepository,
    private val connectivityUtils: ConnectivityUtils
) : ViewModel() {

    private val _locations = MutableLiveData<List<LocationsResults>>()
    val locations: MutableLiveData<List<LocationsResults>> get() = _locations
    private val _location = MutableLiveData<LocationsResults>()
    val location: MutableLiveData<LocationsResults> get() = _location
    private val _characters = MutableLiveData<List<CharactersResults>>()
    val characters: MutableLiveData<List<CharactersResults>> get() = _characters

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _nameFilterValue = MutableLiveData<String>()

    fun updateNameFilterValue(newValue: String) {
        _nameFilterValue.value = newValue
    }

    private val _typeFilterValue = MutableLiveData<String>()

    fun updateTypeFilterValue(newValue: String) {
        _typeFilterValue.value = newValue
    }

    private val _dimensionFilterValue = MutableLiveData<String>()

    fun updateDimensionFilterValue(newValue: String) {
        _dimensionFilterValue.value = newValue
    }

    private val mediatorLiveData = MediatorLiveData<Triple<String?, String?, String?>>()

    init {
        mediatorLiveData.addSource(_nameFilterValue) { value1 ->
            mediatorLiveData.value = Triple(value1, _typeFilterValue.value, _dimensionFilterValue.value)
        }
        mediatorLiveData.addSource(_typeFilterValue) { value2 ->
            mediatorLiveData.value = Triple(_nameFilterValue.value, value2, _dimensionFilterValue.value)
        }
        mediatorLiveData.addSource(_dimensionFilterValue) { value3 ->
            mediatorLiveData.value = Triple(_nameFilterValue.value, _typeFilterValue.value, value3)
        }
    }

    fun getCombinedLiveData(): MediatorLiveData<Triple<String?, String?, String?>> {
        return mediatorLiveData
    }
    fun getLocations(
        name: String?,
        type: String?,
        dimension: String?
    ): Flow<PagingData<LocationsResults>> =
        repository.getFilteredLocations(name, type, dimension)

    fun getLocationById(id: Int) {
        viewModelScope.launch {
            _loading.value = true
            try {
                if (connectivityUtils.isNetworkAvailable()) {
                    val response = repository.getLocationById(id)
                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful) {
                            _location.postValue(response.body())
                            val characterIds = getIDs(response.body()!!.residents)
                            val charactersResponse =
                                repository.getMultipleCharacters(characterIds)
                            _characters.postValue(charactersResponse.body())
                        }
                    }
                } else {
                    if (id > 0) {
                        val cachedLocation = getCachedLocationById(id)
                        withContext(Dispatchers.Main) {
                            _location.postValue(cachedLocation)
                            val characterIDs = getIDs(cachedLocation.residents)
                            val cachedCharacters =
                                repository.getCachedMultipleCharacters(characterIDs)
                            _characters.postValue(cachedCharacters)
                        }
                    }
                }
            } finally {
                _loading.value = false
            }
        }
    }

    private suspend fun getCachedLocationById(id: Int): LocationsResults {
        return withContext(Dispatchers.IO) {
            repository.getCachedLocationById(id)
        }
    }
}