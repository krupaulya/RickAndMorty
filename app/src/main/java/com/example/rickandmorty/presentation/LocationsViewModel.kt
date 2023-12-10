package com.example.rickandmorty.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.rickandmorty.data.model.Locations
import com.example.rickandmorty.domain.RickMortyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LocationsViewModel @Inject constructor(
    private val repository: RickMortyRepository
) : ViewModel() {

    private val _locations = MutableLiveData<List<Locations.LocationsResults>>()
    val locations: MutableLiveData<List<Locations.LocationsResults>> get() = _locations
    private var job: Job? = null

    fun getLocations() {
        job = CoroutineScope(Dispatchers.IO).launch {
            val response = repository.getLocations()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    _locations.postValue(response.body()!!.results)
                }
            }
        }
    }
}