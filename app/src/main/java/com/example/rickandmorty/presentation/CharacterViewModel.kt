package com.example.rickandmorty.presentation

import androidx.lifecycle.ViewModel
import com.example.rickandmorty.domain.CharactersRepository

class CharacterViewModel(
    private val repository: CharactersRepository
) : ViewModel() {
}

