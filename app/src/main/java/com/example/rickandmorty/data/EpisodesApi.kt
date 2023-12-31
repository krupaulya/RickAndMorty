package com.example.rickandmorty.data

import com.example.rickandmorty.data.model.EpisodesResponse
import com.example.rickandmorty.data.model.EpisodesResults
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface EpisodesApi {

    @GET("episode")
    suspend fun getEpisodes(
        @Query("page") page: Int
    ): EpisodesResponse

    @GET("episode/{id}")
    suspend fun getEpisodeById(
        @Path("id") id: Int
    ): Response<EpisodesResults>

    @GET("episode/{ids}")
    suspend fun getMultipleEpisodes(
        @Path("ids") ids: List<Int>
    ): Response<List<EpisodesResults>>

    // One Filter
    @GET("episode/")
    suspend fun getNameFiltered(
        @Query("page") page: Int,
        @Query("name") name: String
    ): EpisodesResponse

    @GET("episode/")
    suspend fun getEpisodeFiltered(
        @Query("page") page: Int,
        @Query("episode") episode: String
    ): EpisodesResponse

    @GET("episode/")
    suspend fun getNameAndEpisodeFiltered(
        @Query("page") page: Int,
        @Query("name") name: String,
        @Query("episode") episode: String
    ): EpisodesResponse
}