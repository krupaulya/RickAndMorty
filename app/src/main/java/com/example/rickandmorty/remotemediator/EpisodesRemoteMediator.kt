package com.example.rickandmorty.remotemediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.rickandmorty.data.EpisodesApi
import com.example.rickandmorty.data.model.EpisodesRemoteKeys
import com.example.rickandmorty.data.model.EpisodesResults
import com.example.rickandmorty.database.RMDatabase
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class EpisodesRemoteMediator(
    private val episodesApi: EpisodesApi,
    private val database: RMDatabase
) : RemoteMediator<Int, EpisodesResults>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, EpisodesResults>
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
            val response = episodesApi.getEpisodes(page)

            val episodes = response.results
            val endOfPaginationReached = episodes.isEmpty()
            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.episodesRemoteKeysDao().clearEpisodesRemoteKeys()
                    database.episodesDao().clearEpisodes()
                }
                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = episodes.map {
                    EpisodesRemoteKeys(episodeId = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                database.episodesRemoteKeysDao().insertEpisodesRemoteKeys(keys)
                database.episodesDao().insertEpisodes(episodes)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, EpisodesResults>): EpisodesRemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { episode ->
                database.episodesRemoteKeysDao().remoteKeysEpisodeId(episode.id)
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, EpisodesResults>): EpisodesRemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { episode ->
                database.episodesRemoteKeysDao().remoteKeysEpisodeId(episode.id)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, EpisodesResults>
    ): EpisodesRemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { episodeID ->
                database.episodesRemoteKeysDao().remoteKeysEpisodeId(episodeID)
            }
        }
    }

}
