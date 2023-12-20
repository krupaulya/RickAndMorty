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
import com.example.rickandmorty.getID
import com.example.rickandmorty.getIDs
import com.example.rickandmorty.paging.CharactersPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CharacterViewModel @Inject constructor(
    private val repository: RickMortyRepository
) : ViewModel() {

    private val _characters = MutableLiveData<List<Characters.CharactersResults>>()
    private val _character = MutableLiveData<Characters.CharactersResults>()
    val character: MutableLiveData<Characters.CharactersResults> get() = _character
    val characters: MutableLiveData<List<Characters.CharactersResults>> get() = _characters
    private var job: Job? = null
    private val _episodes = MutableLiveData<List<Episodes.EpisodesResults>>()
    val episodes: MutableLiveData<List<Episodes.EpisodesResults>> get() = _episodes
    private val _originID = MutableLiveData<Int>()
    val originID: MutableLiveData<Int> get() = _originID
    private val _locationID = MutableLiveData<Int>()
    val locationID: MutableLiveData<Int> get() = _locationID
    val characterList = Pager(PagingConfig(1)) {
        CharactersPagingSource(repository)
    }.flow.cachedIn(viewModelScope)

    fun getCharacterById(id: Int) {
        job = CoroutineScope(Dispatchers.IO).launch {
            val response = repository.getCharacterById(id)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    _character.postValue(response.body())
                    val episodeIDs = getIDs(response.body()!!.episode)
                    val episodeResponse = repository.getMultipleEpisodes(episodeIDs)
                    _episodes.postValue(episodeResponse.body())
                    val origin = getID(response.body()!!.origin.url)
                    val originResponse = repository.getLocationById(origin)
                    _originID.postValue(originResponse.body()!!.id)
                    val location = getID(response.body()!!.location.url)
                    val locationResponse = repository.getLocationById(location)
                    _locationID.postValue(locationResponse.body()!!.id)
                }
            }
        }
    }
}