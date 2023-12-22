package com.example.rickandmorty.data


import com.example.rickandmorty.data.model.CharactersResponse
import com.example.rickandmorty.data.model.CharactersResults
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CharactersApi {
    @GET("character")
    suspend fun getCharacters(
        @Query("page") page: Int
    ): CharactersResponse

    @GET("character/{id}")
    suspend fun getCharacterById(
        @Path("id") id: Int
    ): Response<CharactersResults>

    @GET("character/{ids}")
    suspend fun getMultipleCharacters(
        @Path("ids") ids: List<Int>
    ): Response<List<CharactersResults>>

    @GET("/api/character/")
    suspend fun getCharactersByStatus(
        @Query("status") status: String?
    ): Response<CharactersResponse>

}