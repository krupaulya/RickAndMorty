package com.example.rickandmorty.data

import com.example.rickandmorty.data.model.LocationsResponse
import com.example.rickandmorty.data.model.LocationsResults
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface LocationsApi {
    @GET("location")
    suspend fun getLocations(@Query("page") page: Int): LocationsResponse

    @GET("location/{id}")
    suspend fun getLocationById(
        @Path("id") id: Int
    ): Response<LocationsResults>

    @GET("location/{ids}")
    suspend fun getMultipleLocations(
        @Path("ids") ids: List<Int>
    ): Response<List<LocationsResults>>
}