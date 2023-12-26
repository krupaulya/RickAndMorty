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

    //one filter
    @GET("location/")
    suspend fun getNameFiltered(
        @Query("page") page: Int,
        @Query("name") name: String
    ): LocationsResponse

    @GET("location/")
    suspend fun getTypeFiltered(
        @Query("page") page: Int,
        @Query("type") type: String
    ): LocationsResponse

    @GET("location/")
    suspend fun getDimensionFiltered(
        @Query("page") page: Int,
        @Query("dimension") dimension: String
    ): LocationsResponse

    //two filters
    @GET("location/")
    suspend fun getNameAndTypeFiltered(
        @Query("page") page: Int,
        @Query("name") name: String,
        @Query("type") type: String
    ): LocationsResponse

    @GET("location/")
    suspend fun getNameAndDimensionFiltered(
        @Query("page") page: Int,
        @Query("name") name: String,
        @Query("dimension") dimension: String
    ): LocationsResponse

    @GET("location/")
    suspend fun getTypeAndDimensionFiltered(
        @Query("page") page: Int,
        @Query("type") type: String,
        @Query("dimension") dimension: String
    ): LocationsResponse

    //three filters
    @GET("location/")
    suspend fun getLocationsFiltered(
        @Query("page") page: Int,
        @Query("name") name: String,
        @Query("type") type: String,
        @Query("dimension") dimension: String
    ): LocationsResponse
}