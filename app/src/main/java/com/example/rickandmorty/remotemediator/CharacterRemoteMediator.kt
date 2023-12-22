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
class CharacterRemoteMediator(
    private val characterApi: CharactersApi,
    private val database: RMDatabase
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
            val response = characterApi.getCharacters(page)

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
                    CharacterRemoteKeys(characterId = it.id, prevKey = prevKey, nextKey = nextKey)
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
