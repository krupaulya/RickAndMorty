package com.example.rickandmorty.remotemediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.rickandmorty.data.LocationsApi
import com.example.rickandmorty.data.model.LocationsRemoteKeys
import com.example.rickandmorty.data.model.LocationsResults
import com.example.rickandmorty.database.RMDatabase
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class LocationsRemoteMediator(
    private val locationsApi: LocationsApi,
    private val database: RMDatabase
) : RemoteMediator<Int, LocationsResults>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, LocationsResults>
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
            val response = locationsApi.getLocations(page)

            val locations = response.results
            val endOfPaginationReached = locations.isEmpty()
            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.locationsRemoteKeysDao().clearLocationRemoteKeys()
                    database.locationsDao().clearLocations()
                }
                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = locations.map {
                    LocationsRemoteKeys(locationId = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                database.locationsRemoteKeysDao().insertLocationsRemoteKeys(keys)
                database.locationsDao().insertLocations(locations)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, LocationsResults>): LocationsRemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { location ->
                database.locationsRemoteKeysDao().remoteKeysLocationId(location.id)
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, LocationsResults>): LocationsRemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { location ->
                database.locationsRemoteKeysDao().remoteKeysLocationId(location.id)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, LocationsResults>
    ): LocationsRemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { locationID ->
                database.locationsRemoteKeysDao().remoteKeysLocationId(locationID)
            }
        }
    }

}
    
    

