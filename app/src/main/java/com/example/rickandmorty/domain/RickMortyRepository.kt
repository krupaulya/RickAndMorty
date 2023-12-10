package com.example.rickandmorty.domain

import com.example.rickandmorty.RickMortyRemoteData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RickMortyRepository @Inject constructor(
    private val rickMortyRemoteData: RickMortyRemoteData
) {
    suspend fun getCharacters() = rickMortyRemoteData.getCharacters()

    suspend fun getCharacterById(id: Int) = rickMortyRemoteData.getCharacterById(id)

    suspend fun getLocations() = rickMortyRemoteData.getLocations()

    suspend fun getEpisodes() = rickMortyRemoteData.getEpisodes()

}