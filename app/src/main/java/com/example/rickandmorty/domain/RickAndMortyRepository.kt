package com.example.rickandmorty.domain

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.rickandmorty.data.CharactersApi
import com.example.rickandmorty.data.EpisodesApi
import com.example.rickandmorty.data.LocationsApi
import com.example.rickandmorty.data.model.CharactersResults
import com.example.rickandmorty.data.model.EpisodesResults
import com.example.rickandmorty.data.model.LocationsResults
import com.example.rickandmorty.database.RMDatabase
import com.example.rickandmorty.remotemediator.CharacterRemoteMediator
import com.example.rickandmorty.remotemediator.EpisodesRemoteMediator
import com.example.rickandmorty.remotemediator.LocationsRemoteMediator
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RickAndMortyRepository @Inject constructor(
    private val charactersApi: CharactersApi,
    private val episodesApi: EpisodesApi,
    private val locationsApi: LocationsApi,
    private val database: RMDatabase
) {
    fun getAllCharacters(): Flow<PagingData<CharactersResults>> {

        val pagingSourceFactory = { database.charactersDao().getAllCharacters() }

        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = CharacterRemoteMediator(
                charactersApi,
                database
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    fun getEpisodes(): Flow<PagingData<EpisodesResults>> {

        val pagingSourceFactory = { database.episodesDao().getAllEpisodes() }

        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            remoteMediator = EpisodesRemoteMediator(
                episodesApi,
                database
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    fun getLocations(): Flow<PagingData<LocationsResults>> {

        val pagingSourceFactory = { database.locationsDao().getAllLocations() }

        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            remoteMediator = LocationsRemoteMediator(
                locationsApi,
                database
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    suspend fun getCharacterById(id: Int) = charactersApi.getCharacterById(id)
    fun getCachedCharacterById(id: Int) = database.charactersDao().getCharacterById(id)

    suspend fun getMultipleCharacters(ids: List<Int>) =
        charactersApi.getMultipleCharacters(ids)

    suspend fun getLocationById(id: Int) = locationsApi.getLocationById(id)

    suspend fun getEpisodeById(id: Int) = episodesApi.getEpisodeById(id)

    suspend fun getMultipleEpisodes(ids: List<Int>) = episodesApi.getMultipleEpisodes(ids)

}