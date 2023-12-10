package com.example.rickandmorty.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.rickandmorty.data.model.Episodes
import com.example.rickandmorty.domain.RickMortyRepository
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
    private var job: Job? = null

    fun getEpisodes() {
        job = CoroutineScope(Dispatchers.IO).launch {
            val response = repository.getEpisodes()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    _episodes.postValue(response.body()!!.results)
                }
            }
        }
    }
}