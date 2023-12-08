package com.example.rickandmorty.domain

import com.example.rickandmorty.CharactersRemoteData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CharactersRepository @Inject constructor(
    private val charactersRemoteData: CharactersRemoteData
) {
    suspend fun getCharacters() = charactersRemoteData.getCharacters()

}