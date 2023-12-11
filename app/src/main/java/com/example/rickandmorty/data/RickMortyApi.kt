package com.example.rickandmorty.data


import com.example.rickandmorty.data.model.Characters
import com.example.rickandmorty.data.model.Episodes
import com.example.rickandmorty.data.model.Locations
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RickMortyApi {
    @GET("character")
    suspend fun getCharacters(@Query("page") page: Int): Response<Characters>

    @GET("character/{id}")
    suspend fun getCharacterById(
        @Path("id") id: Int
    ): Response<Characters.CharactersResults>

    @GET("character/{ids}")
    suspend fun getMultipleCharacters(
        @Path("ids") ids: List<Int>
    ): Response<List<Characters.CharactersResults>>

    @GET("location")
    suspend fun getLocations(@Query("page") page: Int): Response<Locations>

    @GET("location/{id}")
    suspend fun getLocationById(
        @Path("id") id: Int
    ): Response<Locations.LocationsResults>

    @GET("location/{ids}")
    suspend fun getMultipleLocations(
        @Path("ids") ids: List<Int>
    ): Response<List<Locations.LocationsResults>>

    @GET("episode")
    suspend fun getEpisodes(@Query("page") page: Int): Response<Episodes>

    @GET("episode/{id}")
    suspend fun getEpisodeById(
        @Path("id") id: Int
    ): Response<Episodes.EpisodesResults>

    @GET("episode/{ids}")
    suspend fun getMultipleEpisodes(
        @Path("ids") ids: List<Int>
    ): Response<List<Episodes.EpisodesResults>>
}