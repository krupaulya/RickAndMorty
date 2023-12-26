package com.example.rickandmorty.remotemediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.rickandmorty.data.CharactersApi
import com.example.rickandmorty.data.model.CharacterRemoteKeys
import com.example.rickandmorty.data.model.CharactersResults
import com.example.rickandmorty.database.RMDatabase
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class CharactersRemoteMediator(
    private val characterApi: CharactersApi,
    private val database: RMDatabase,
    private val name: String?,
    private val status: String?,
    private val species: String?,
    private val type: String?,
    private val gender: String?
) : RemoteMediator<Int, CharactersResults>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CharactersResults>
    ): MediatorResult {

        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: 1
            }

            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)

            }

            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)

            }
        }

        try {
            val response = when {
                name != null && status != null && species != null && type != null && gender != null -> characterApi.getCharactersFiltered(page, name, status, species, type, gender)
                name != null && status != null && species != null && type != null -> characterApi.getNameAndStatusAndSpeciesAndTypeFiltered(page, name, status, species, type)
                name != null && status != null && species != null && gender != null -> characterApi.getNameAndStatusAndSpeciesAndGenderFiltered(page, name, status, species, gender)
                name != null && status != null && type != null && gender != null -> characterApi.getNameAndStatusAndTypeAndGenderFiltered(page, name, status, type, gender)
                name != null && species != null && type != null && gender != null -> characterApi.getNameAndSpeciesAndTypeAndGenderFiltered(page, name, species, type, gender)
                status != null && species!= null && type != null && gender != null -> characterApi.getStatusAndSpeciesAndTypeAndGenderFiltered(page, status, species, type, gender)
                name != null && status != null && species != null -> characterApi.getNameAndStatusAndSpeciesFiltered(page, name, status, species)
                name != null && status != null && type != null -> characterApi.getNameAndStatusAndTypeFiltered(page, name, status, type)
                name != null && status != null && gender != null -> characterApi.getNameAndStatusAndGenderFiltered(page, name, status, gender)
                name != null && species != null && type != null -> characterApi.getNameAndSpeciesAndTypeFiltered(page, name, species, type)
                name != null && species != null && gender != null -> characterApi.getNameAndSpeciesAndGenderFiltered(page, name, species, gender)
                name != null && type != null && gender != null -> characterApi.getNameAndTypeAndGenderFiltered(page, name, type, gender)
                status != null && species != null && type != null -> characterApi.getStatusAndSpeciesAndTypeFiltered(page, status, species, type)
                status != null && species != null && gender != null -> characterApi.getStatusAndSpeciesAndGenderFiltered(page, status, species, gender)
                status != null && type != null && gender != null -> characterApi.getStatusAndTypeAndGenderFiltered(page, status, type, gender)
                species != null && type != null && gender != null -> characterApi.getSpeciesAndTypeAndGenderFiltered(page, species, type, gender)
                name != null && status != null -> characterApi.getNameAndStatusFiltered(page, name, status)
                name != null && species != null -> characterApi.getNameAndSpeciesFiltered(page, name, species)
                name != null && type != null -> characterApi.getNameAndTypeFiltered(page, name, type)
                name != null && gender != null -> characterApi.getNameAndGenderFiltered(page, name, gender)
                status != null && species != null -> characterApi.getStatusAndSpeciesFiltered(page, status, species)
                status != null && type != null -> characterApi.getStatusAndTypeFiltered(page, status, type)
                status != null && gender != null -> characterApi.getStatusAndGenderFiltered(page, status, gender)
                species != null && type != null -> characterApi.getSpeciesAndTypeFiltered(page, species, type)
                species != null && gender != null -> characterApi.getSpeciesAndGenderFiltered(page, species, gender)
                type != null && gender != null -> characterApi.getTypeAndGenderFiltered(page, type, gender)
                name != null -> characterApi.getNameFiltered(page, name)
                status != null -> characterApi.getStatusFiltered(page, status)
                species != null -> characterApi.getSpeciesFiltered(page, species)
                type != null -> characterApi.getTypeFiltered(page, type)
                gender != null -> characterApi.getGenderFiltered(page, gender)
                else -> characterApi.getCharacters(page)
            }
            val characters = response.results
            val endOfPaginationReached = characters.isEmpty()
            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.characterRemoteKeysDao().clearCharacterRemoteKeys()
                    database.charactersDao().clearCharacters()
                }
                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = characters.map {
                    CharacterRemoteKeys(
                        characterId = it.id,
                        prevKey = prevKey,
                        nextKey = nextKey
                    )
                }
                database.characterRemoteKeysDao().insertCharacterRemoteKeys(keys)
                database.charactersDao().insertCharacters(characters)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, CharactersResults>): CharacterRemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { character ->
                database.characterRemoteKeysDao().remoteKeysCharacterId(character.id)
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, CharactersResults>): CharacterRemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { character ->
                database.characterRemoteKeysDao().remoteKeysCharacterId(character.id)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, CharactersResults>
    ): CharacterRemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { characterID ->
                database.characterRemoteKeysDao().remoteKeysCharacterId(characterID)
            }
        }
    }
}
