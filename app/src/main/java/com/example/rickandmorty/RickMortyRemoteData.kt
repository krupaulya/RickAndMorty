package com.example.rickandmorty

import com.example.rickandmorty.data.RickMortyApi
import javax.inject.Inject

class RickMortyRemoteData @Inject constructor(
    private val api: RickMortyApi
) {
    suspend fun getCharacters(page: Int) = api.getCharacters(page)
    suspend fun getCharacterById(id: Int) = api.getCharacterById(id)
    suspend fun getMultipleCharacters(ids: List<Int>) = api.getMultipleCharacters(ids)
    suspend fun getLocations(page: Int) = api.getLocations(page)
    suspend fun getLocationById(id: Int) = api.getLocationById(id)
    suspend fun getEpisodes(page: Int) = api.getEpisodes(page)
    suspend fun getEpisodeById(id: Int) = api.getEpisodeById(id)
    suspend fun getMultipleEpisodes(ids: List<Int>) = api.getMultipleEpisodes(ids)

}