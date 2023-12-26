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
import com.example.rickandmorty.remotemediator.CharactersRemoteMediator
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
    fun getFilteredCharacters(
        name: String?,
        status: String?,
        species: String?,
        type: String?,
        gender: String?
    ): Flow<PagingData<CharactersResults>> {

        val correctedName = "%${name?.replace(' ', '%')}%"
        val correctedSpecies = "%${species?.replace(' ', '%')}%"
        val correctedType = "%${type?.replace(' ', '%')}%"

        val pagingSourceFactory = {
            when {
                name != null && status != null && species != null && type != null && gender != null -> database.charactersDao().getCharactersFiltered(correctedName, status, correctedSpecies, correctedType, gender)
                name != null && status != null && species != null && type != null -> database.charactersDao().getNameAndStatusAndSpeciesAndTypeFiltered(correctedName, status, correctedSpecies, correctedType)
                name != null && status != null && species != null && gender != null -> database.charactersDao().getNameAndStatusAndSpeciesAndGenderFiltered(correctedName, status, correctedSpecies, gender)
                name != null && status != null && type != null && gender != null -> database.charactersDao().getNameAndStatusAndTypeAndGenderFiltered(correctedName, status, correctedType, gender)
                name != null && species != null && type != null && gender != null -> database.charactersDao().getNameAndSpeciesAndTypeAndGenderFiltered(correctedName, correctedSpecies, correctedType, gender)
                status != null && species!= null && type != null && gender != null -> database.charactersDao().getStatusAndSpeciesAndTypeAndGenderFiltered(status, correctedSpecies, correctedType, gender)
                name != null && status != null && species != null -> database.charactersDao().getNameAndStatusAndSpeciesFiltered(correctedName, status, correctedSpecies)
                name != null && status != null && type != null -> database.charactersDao().getNameAndStatusAndTypeFiltered(correctedName, status, correctedType)
                name != null && status != null && gender != null -> database.charactersDao().getNameAndStatusAndGenderFiltered(correctedName, status, gender)
                name != null && species != null && type != null -> database.charactersDao().getNameAndSpeciesAndTypeFiltered(correctedName, correctedSpecies, correctedType)
                name != null && species != null && gender != null -> database.charactersDao().getNameAndSpeciesAndGenderFiltered(correctedName, correctedSpecies, gender)
                name != null && type != null && gender != null -> database.charactersDao().getNameAndTypeAndGenderFiltered(correctedName, correctedType, gender)
                status != null && species != null && type != null -> database.charactersDao().getStatusAndSpeciesAndTypeFiltered(status, correctedSpecies, correctedType)
                status != null && species != null && gender != null -> database.charactersDao().getStatusAndSpeciesAndGenderFiltered(status, correctedSpecies, gender)
                status != null && type != null && gender != null -> database.charactersDao().getStatusAndTypeAndGenderFiltered(status, correctedType, gender)
                species != null && type != null && gender != null -> database.charactersDao().getSpeciesAndTypeAndGenderFiltered(correctedSpecies, correctedType, gender)
                name != null && status != null -> database.charactersDao().getNameAndStatusFiltered(correctedName, status)
                name != null && species != null -> database.charactersDao().getNameAndSpeciesFiltered(correctedName, correctedSpecies)
                name != null && type != null -> database.charactersDao().getNameAndTypeFiltered(correctedName, correctedType)
                name != null && gender != null -> database.charactersDao().getNameAndGenderFiltered(correctedName, gender)
                status != null && species != null -> database.charactersDao().getStatusAndSpeciesFiltered(status, correctedSpecies)
                status != null && type != null -> database.charactersDao().getStatusAndTypeFiltered(status, correctedType)
                status != null && gender != null -> database.charactersDao().getStatusAndGenderFiltered(status, gender)
                species != null && type != null -> database.charactersDao().getSpeciesAndTypeFiltered(correctedSpecies, correctedType)
                species != null && gender != null -> database.charactersDao().getSpeciesAndGenderFiltered(correctedSpecies, gender)
                type != null && gender != null -> database.charactersDao().getTypeAndGenderFiltered(correctedType, gender)
                name != null -> database.charactersDao().getNameFiltered(correctedName)
                status != null -> database.charactersDao().getStatusFiltered(status)
                species != null -> database.charactersDao().getSpeciesFiltered(correctedSpecies)
                type != null -> database.charactersDao().getTypeFiltered(correctedType)
                gender != null -> database.charactersDao().getGenderFiltered(gender)
                else -> database.charactersDao().getAllCharacters()
            }
        }

        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = CharactersRemoteMediator(
                charactersApi,
                database,
                name,
                status,
                species,
                type,
                gender
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    fun getFilteredEpisodes(
        name: String?,
        episode: String?
    ): Flow<PagingData<EpisodesResults>> {

        val correctedName = "%${name?.replace(' ', '%')}%"
        val correctedEpisode = "%${episode?.replace(' ', '%')}%"

        val pagingSourceFactory = {
            when {
                name == null && episode == null -> database.episodesDao().getAllEpisodes()
                name == null -> database.episodesDao().getEpisodeFilteredEpisodes(correctedEpisode)
                episode == null -> database.episodesDao().getNameFilteredEpisodes(correctedName)
                else -> database.episodesDao().getFilteredEpisodes(correctedName, correctedEpisode)
            }
        }

        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = EpisodesRemoteMediator(
                episodesApi,
                database,
                name,
                episode
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    fun getFilteredLocations(
        name: String?,
        type: String?,
        dimension: String?
    ): Flow<PagingData<LocationsResults>> {

        val correctedName = "%${name?.replace(' ', '%')}%"
        val correctedType = "%${type?.replace(' ', '%')}%"
        val correctedDimension = "%${dimension?.replace(' ', '%')}%"

        val pagingSourceFactory = {
            when {
                name != null && type != null && dimension != null -> database.locationsDao().getLocationsFiltered(correctedName, correctedType, correctedDimension)
                name != null && type != null -> database.locationsDao().getNameAndTypeFiltered(correctedName, correctedType)
                name != null && dimension != null -> database.locationsDao().getNameAndDimensionFiltered(correctedName, correctedDimension)
                type != null && dimension != null -> database.locationsDao().getTypeAndDimensionFiltered(correctedType, correctedDimension)
                name != null -> database.locationsDao().getNameFiltered(correctedName)
                type != null -> database.locationsDao().getTypeFiltered(correctedType)
                dimension != null -> database.locationsDao().getDimensionFiltered(correctedDimension)
                else -> database.locationsDao().getAllLocations()
            }
        }

        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            remoteMediator = LocationsRemoteMediator(
                locationsApi,
                database,
                name, type, dimension
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    suspend fun getCharacterById(id: Int) = charactersApi.getCharacterById(id)
    fun getCachedCharacterById(id: Int) = database.charactersDao().getCharacterById(id)

    suspend fun getMultipleCharacters(ids: List<Int>) =
        charactersApi.getMultipleCharacters(ids)

    suspend fun getCachedMultipleCharacters(ids: List<Int>) =
        database.charactersDao().getMultipleCachedCharacters(ids)

    suspend fun getLocationById(id: Int) = locationsApi.getLocationById(id)

    fun getCachedLocationById(id: Int) = database.locationsDao().getLocationById(id)

    suspend fun getEpisodeById(id: Int) = episodesApi.getEpisodeById(id)

    fun getCachedEpisodeById(id: Int) = database.episodesDao().getEpisodeById(id)

    suspend fun getMultipleEpisodes(ids: List<Int>) = episodesApi.getMultipleEpisodes(ids)

    suspend fun getCachedMultipleEpisodes(ids: List<Int>) =
        database.episodesDao().getEpisodesForCharacters(ids)
}