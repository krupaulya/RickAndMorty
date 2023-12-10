package com.example.rickandmorty.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.rickandmorty.data.model.Characters
import com.example.rickandmorty.domain.RickMortyRepository
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

    fun getCharacterById(id: Int) {
        job = CoroutineScope(Dispatchers.IO).launch {
            val response = repository.getCharacterById(id)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    _character.postValue(response.body())
                }
            }
        }
    }
}