package com.example.rickandmorty.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.rickandmorty.data.model.Locations
import com.example.rickandmorty.domain.RickMortyRepository
import com.example.rickandmorty.paging.LocationsPagingSource
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
    private val _location = MutableLiveData<Locations.LocationsResults>()
    val location: MutableLiveData<Locations.LocationsResults> get() = _location
    private var job: Job? = null
    val locationList = Pager(PagingConfig(1)) {
        LocationsPagingSource(repository)
    }.flow.cachedIn(viewModelScope)

    fun getLocationById(id: Int) {
        job = CoroutineScope(Dispatchers.IO).launch {
            val response = repository.getLocationById(id)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    _location.postValue(response.body())
                }
            }
        }
    }
}