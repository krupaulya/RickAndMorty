package com.example.rickandmorty.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.rickandmorty.ConnectivityUtils
import com.example.rickandmorty.data.model.CharactersResults
import com.example.rickandmorty.data.model.EpisodesResults
import com.example.rickandmorty.domain.RickAndMortyRepository
import com.example.rickandmorty.filter.CharacterFilter
import com.example.rickandmorty.getID
import com.example.rickandmorty.getIDs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CharacterViewModel @Inject constructor(
    private val repository: RickAndMortyRepository,
    private val connectivityUtils: ConnectivityUtils
) : ViewModel() {

    private val _characters = MutableLiveData<List<CharactersResults>>()
    private val _character = MutableLiveData<CharactersResults>()
    val character: MutableLiveData<CharactersResults> get() = _character

    val characters: MutableLiveData<List<CharactersResults>> get() = _characters
    private val _episodes = MutableLiveData<List<EpisodesResults>>()
    val episodes: MutableLiveData<List<EpisodesResults>> get() = _episodes
    private val _originID = MutableLiveData<Int>()
    val originID: MutableLiveData<Int> get() = _originID
    private val _locationID = MutableLiveData<Int>()
    val locationID: MutableLiveData<Int> get() = _locationID

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _nameFilterValue = MutableLiveData<String?>()
    fun updateNameFilterValue(newValue: String?) {
        _nameFilterValue.value = newValue
    }

    private val _statusFilterValue = MutableLiveData<String?>()
    fun updateStatusFilterValue(newValue: String?) {
        _statusFilterValue.value = newValue
    }

    private val _speciesFilterValue = MutableLiveData<String?>()
    fun updateSpeciesFilterValue(newValue: String?) {
        _speciesFilterValue.value = newValue
    }

    private val _typeFilterValue = MutableLiveData<String?>()
    fun updateTypeFilterValue(newValue: String?) {
        _typeFilterValue.value = newValue
    }

    private val _genderFilterValue = MutableLiveData<String?>()
    fun updateGenderFilterValue(newValue: String?) {
        _genderFilterValue.value = newValue
    }

    private val mediatorLiveData = MediatorLiveData<CharacterFilter>()

    init {
        mediatorLiveData.addSource(_nameFilterValue) { value1 ->
            mediatorLiveData.value = CharacterFilter(
                value1,
                _statusFilterValue.value,
                _speciesFilterValue.value,
                _typeFilterValue.value,
                _genderFilterValue.value
            )
        }
        mediatorLiveData.addSource(_statusFilterValue) { value2 ->
            mediatorLiveData.value = CharacterFilter(
                _nameFilterValue.value,
                value2,
                _speciesFilterValue.value,
                _typeFilterValue.value,
                _genderFilterValue.value
            )
        }
        mediatorLiveData.addSource(_speciesFilterValue) { value3 ->
            mediatorLiveData.value = CharacterFilter(
                _nameFilterValue.value,
                _statusFilterValue.value,
                value3,
                _typeFilterValue.value,
                _genderFilterValue.value
            )
        }
        mediatorLiveData.addSource(_typeFilterValue) { value4 ->
            mediatorLiveData.value = CharacterFilter(
                _nameFilterValue.value,
                _statusFilterValue.value,
                _speciesFilterValue.value,
                value4,
                _genderFilterValue.value
            )
        }
        mediatorLiveData.addSource(_genderFilterValue) { value5 ->
            mediatorLiveData.value = CharacterFilter(
                _nameFilterValue.value,
                _statusFilterValue.value,
                _speciesFilterValue.value,
                _typeFilterValue.value,
                value5
            )
        }
    }

    fun getCombinedLiveData(): MediatorLiveData<CharacterFilter> {
        return mediatorLiveData
    }

    fun getFilteredCharacters(
        name: String?,
        status: String?,
        species: String?,
        type: String?,
        gender: String?
    ): Flow<PagingData<CharactersResults>> =
        repository.getFilteredCharacters(name, status, species, type, gender)


    fun getCharacterById(id: Int) {
        viewModelScope.launch {
            _loading.value = true
            try {
                if (connectivityUtils.isNetworkAvailable()) {
                    val response = repository.getCharacterById(id)
                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful) {
                            _character.postValue(response.body())
                            val episodeIDs = getIDs(response.body()!!.episode)
                            val episodeResponse = repository.getMultipleEpisodes(episodeIDs)
                            _episodes.postValue(episodeResponse.body())
                            val origin = getID(response.body()!!.origin.url)
                            val originResponse = repository.getLocationById(origin)
                            if (originResponse.isSuccessful) {
                                _originID.postValue(originResponse.body()!!.id)
                            } else {
                                _originID.postValue(0)
                            }
                            val location = getID(response.body()!!.location.url)
                            val locationResponse = repository.getLocationById(location)
                            if (locationResponse.isSuccessful) {
                                _locationID.postValue(locationResponse.body()!!.id)
                            } else {
                                _locationID.postValue(0)
                            }
                        }
                    }
                } else {
                    val cachedCharacter = getCachedCharacterById(id)
                    val episodeIDs = getIDs(cachedCharacter.episode)
                    val cachedEpisodes = getCachedEpisodes(episodeIDs)
                    withContext(Dispatchers.Main) {
                        _character.postValue(cachedCharacter)
                        _episodes.postValue(cachedEpisodes)
                        val origin = getID(cachedCharacter.origin.url)
                        _originID.postValue(origin)
                        val location = getID(cachedCharacter.location.url)
                        _locationID.postValue(location)
                    }
                }
            } finally {
                _loading.value = false
            }
        }
    }

    private suspend fun getCachedCharacterById(id: Int): CharactersResults {
        return withContext(Dispatchers.IO) {
            repository.getCachedCharacterById(id)
        }
    }

    private suspend fun getCachedEpisodes(ids: List<Int>): List<EpisodesResults> {
        return withContext(Dispatchers.IO) {
            repository.getCachedMultipleEpisodes(ids)
        }
    }
}
