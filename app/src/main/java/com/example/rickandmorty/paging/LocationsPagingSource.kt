package com.example.rickandmorty.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.rickandmorty.data.model.Locations
import com.example.rickandmorty.domain.RickMortyRepository
import retrofit2.HttpException

class LocationsPagingSource(
    private val repository: RickMortyRepository
) : PagingSource<Int, Locations.LocationsResults>() {
    override fun getRefreshKey(state: PagingState<Int, Locations.LocationsResults>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Locations.LocationsResults> {
        return try {
            val currentPage = params.key ?: 1
            val response = repository.getLocations(currentPage)
            val data = response.body()!!.results
            val responseData = mutableListOf<Locations.LocationsResults>()
            responseData.addAll(data)
            LoadResult.Page(
                data = responseData,
                prevKey = if (currentPage == 1) null else -1,
                nextKey = currentPage.plus(1)
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        } catch (httpE: HttpException) {
            LoadResult.Error(httpE)
        }
    }
}