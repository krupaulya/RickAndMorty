package com.example.rickandmorty.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.rickandmorty.ConnectivityObserver
import com.example.rickandmorty.data.model.CharactersResults
import com.example.rickandmorty.data.model.EpisodesResults
import com.example.rickandmorty.domain.RickAndMortyRepository
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
    private val connectivityObserver: ConnectivityObserver
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

    private val _networkStatus = MutableLiveData<ConnectivityObserver.Status>()
    val networkStatus: LiveData<ConnectivityObserver.Status> get() = _networkStatus

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading
    fun getAllCharacters(): Flow<PagingData<CharactersResults>> =
        repository.getAllCharacters()

    init {
        viewModelScope.launch {
            connectivityObserver.observe()
                .collect { status ->
                    _networkStatus.value = status
                    Log.d("ViewModelStatus", status.toString())

                }
        }
    }


    fun getCharacterById(id: Int) {
        viewModelScope.launch {
            _loading.value = true
            try {
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
                        _locationID.postValue(locationResponse.body()!!.id)
                    }
                }
            } finally {
                _loading.value = false
            }
        }
    }

    suspend fun getCachedCharacterById(id: Int): CharactersResults {
        return withContext(Dispatchers.IO) {
            repository.getCachedCharacterById(id)
        }
    }


}