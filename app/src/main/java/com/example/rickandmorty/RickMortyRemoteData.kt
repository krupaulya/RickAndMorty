package com.example.rickandmorty

import com.example.rickandmorty.data.RickMortyApi
import javax.inject.Inject

class RickMortyRemoteData @Inject constructor(
    private val api: RickMortyApi
) {
    suspend fun getCharacters() = api.getCharacters()

    suspend fun getCharacterById(id: Int) = api.getCharacterById(id)

    suspend fun getLocations() = api.getLocations()

    suspend fun getEpisodes() = api.getEpisodes()


}