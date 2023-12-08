package com.example.rickandmorty.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.rickandmorty.data.model.Results
import com.example.rickandmorty.domain.CharactersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CharacterViewModel @Inject constructor(
    private val repository: CharactersRepository
) : ViewModel() {

    private val _characters = MutableLiveData<List<Results>>()
    val characters: MutableLiveData<List<Results>> get() = _characters
    private var job: Job? = null

    fun getCharacters() {
        job = CoroutineScope(Dispatchers.IO).launch {
            val response = repository.getCharacters()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    _characters.postValue(response.body()!!.results)
                }
            }
        }
    }
}