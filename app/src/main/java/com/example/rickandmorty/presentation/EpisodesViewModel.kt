package com.example.rickandmorty.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.rickandmorty.data.model.CharactersResults
import com.example.rickandmorty.data.model.EpisodesResults
import com.example.rickandmorty.domain.RickAndMortyRepository
import com.example.rickandmorty.getIDs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class EpisodesViewModel @Inject constructor(
    private val repository: RickAndMortyRepository
) : ViewModel() {

    private val _episodes = MutableLiveData<List<EpisodesResults>>()
    val episodes: MutableLiveData<List<EpisodesResults>> get() = _episodes
    private val _episode = MutableLiveData<EpisodesResults>()
    val episode: MutableLiveData<EpisodesResults> get() = _episode
    private val _characters = MutableLiveData<List<CharactersResults>>()
    val characters: MutableLiveData<List<CharactersResults>> get() = _characters

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    fun getEpisodes(): Flow<PagingData<EpisodesResults>> =
        repository.getEpisodes()

    fun getEpisodeById(id: Int) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = repository.getEpisodeById(id)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        _episode.postValue(response.body())
                        val ids = getIDs(response.body()!!.characters)
                        val characterResponse = repository.getMultipleCharacters(ids)
                        _characters.postValue(characterResponse.body())
                    }
                }
            } finally {
                _loading.value = false
            }
        }
    }
}