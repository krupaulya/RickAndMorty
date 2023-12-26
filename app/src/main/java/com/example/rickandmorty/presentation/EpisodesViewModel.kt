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
import com.example.rickandmorty.getIDs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class EpisodesViewModel @Inject constructor(
    private val repository: RickAndMortyRepository,
    private val connectivityUtils: ConnectivityUtils
) : ViewModel() {

    private val _episodes = MutableLiveData<List<EpisodesResults>>()
    val episodes: MutableLiveData<List<EpisodesResults>> get() = _episodes
    private val _episode = MutableLiveData<EpisodesResults>()
    val episode: MutableLiveData<EpisodesResults> get() = _episode
    private val _characters = MutableLiveData<List<CharactersResults>>()
    val characters: MutableLiveData<List<CharactersResults>> get() = _characters

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _nameFilterValue = MutableLiveData<String>()

    fun updateNameFilterValue(newValue: String) {
        _nameFilterValue.value = newValue
    }

    private val _episodeFilterValue = MutableLiveData<String>()

    fun updateEpisodeFilterValue(newValue: String) {
        _episodeFilterValue.value = newValue
    }

    private val mediatorLiveData = MediatorLiveData<Pair<String?, String?>>()

    init {
        mediatorLiveData.addSource(_nameFilterValue) { value1 ->
            mediatorLiveData.value = Pair(value1, _episodeFilterValue.value)
        }
        mediatorLiveData.addSource(_episodeFilterValue) { value2 ->
            mediatorLiveData.value = Pair(_nameFilterValue.value, value2)
        }
    }

    fun getCombinedLiveData(): MediatorLiveData<Pair<String?, String?>> {
        return mediatorLiveData
    }

    fun getEpisodes(name: String?, episode: String?): Flow<PagingData<EpisodesResults>> =
        repository.getFilteredEpisodes(name, episode)


    fun getEpisodeById(id: Int) {
        viewModelScope.launch {
            _loading.value = true
            try {
                if (connectivityUtils.isNetworkAvailable()) {
                    val response = repository.getEpisodeById(id)
                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful) {
                            _episode.postValue(response.body())
                            val ids = getIDs(response.body()!!.characters)
                            val characterResponse = repository.getMultipleCharacters(ids)
                            _characters.postValue(characterResponse.body())
                        }
                    }
                } else {
                    val cachedEpisode = getCachedEpisodeById(id)
                    withContext(Dispatchers.Main) {
                        _episode.postValue(cachedEpisode)
                        val characterIDs = getIDs(cachedEpisode.characters)
                        val cachedCharacters = repository.getCachedMultipleCharacters(characterIDs)
                        _characters.postValue(cachedCharacters)
                    }
                }
            } finally {
                _loading.value = false
            }
        }
    }

    private suspend fun getCachedEpisodeById(id: Int): EpisodesResults {
        return withContext(Dispatchers.IO) {
            repository.getCachedEpisodeById(id)
        }
    }
}