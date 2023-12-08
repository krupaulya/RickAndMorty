package com.example.rickandmorty.data


import com.example.rickandmorty.data.model.CharactersInfo
import retrofit2.Response
import retrofit2.http.GET

interface CharactersApi {
    @GET("character")
    suspend fun getCharacters(): Response<CharactersInfo>
}