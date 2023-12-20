package com.example.rickandmorty.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.rickandmorty.data.model.Characters
import com.example.rickandmorty.data.model.Episodes
import com.example.rickandmorty.domain.RickMortyRepository
import com.example.rickandmorty.getIDs
import com.example.rickandmorty.paging.EpisodesPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class EpisodesViewModel @Inject constructor(
    private val repository: RickMortyRepository
) : ViewModel() {

    private val _episodes = MutableLiveData<List<Episodes.EpisodesResults>>()
    val episodes: MutableLiveData<List<Episodes.EpisodesResults>> get() = _episodes
    private val _episode = MutableLiveData<Episodes.EpisodesResults>()
    val episode: MutableLiveData<Episodes.EpisodesResults> get() = _episode
    private var job: Job? = null
    private val _characters = MutableLiveData<List<Characters.CharactersResults>>()
    val characters: MutableLiveData<List<Characters.CharactersResults>> get() = _characters

    val episodeList = Pager(PagingConfig(1)) {
        EpisodesPagingSource(repository)
    }.flow.cachedIn(viewModelScope)

    fun getEpisodeById(id: Int) {
        job = CoroutineScope(Dispatchers.IO).launch {
            val response = repository.getEpisodeById(id)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    _episode.postValue(response.body())
                    val ids = getIDs(response.body()!!.characters)
                    val characterResponse = repository.getMultipleCharacters(ids)
                    _characters.postValue(characterResponse.body())
                }
            }
        }
    }
}