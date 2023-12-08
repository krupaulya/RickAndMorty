package com.example.rickandmorty

import com.example.rickandmorty.data.CharactersApi
import javax.inject.Inject

class CharactersRemoteData @Inject constructor(
    private val api: CharactersApi
) {
    suspend fun getCharacters() = api.getCharacters()
}