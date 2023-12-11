package com.example.rickandmorty.domain

import com.example.rickandmorty.RickMortyRemoteData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RickMortyRepository @Inject constructor(
    private val rickMortyRemoteData: RickMortyRemoteData
) {
    suspend fun getCharacters(page: Int) = rickMortyRemoteData.getCharacters(page)

    suspend fun getCharacterById(id: Int) = rickMortyRemoteData.getCharacterById(id)

    suspend fun getLocations(page: Int) = rickMortyRemoteData.getLocations(page)

    suspend fun getEpisodes(page: Int) = rickMortyRemoteData.getEpisodes(page)

    suspend fun getEpisodeById(id: Int) = rickMortyRemoteData.getEpisodeById(id)

    suspend fun getLocationById(id: Int) = rickMortyRemoteData.getLocationById(id)

}