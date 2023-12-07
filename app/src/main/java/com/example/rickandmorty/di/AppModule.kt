package com.example.rickandmorty.di

import android.content.Context
import com.example.rickandmorty.domain.CharactersRepository
import com.example.rickandmorty.data.CharactersApi
import com.example.rickandmorty.data.CharactersRepositoryImpl
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

interface AppModule {
    val charactersApi: CharactersApi
    val charactersRepository: CharactersRepository
}

class AppModuleImpl(
    private val appContext: Context
) : AppModule {
    override val charactersApi: CharactersApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://rickandmortyapi.com/api/characters")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create()
    }
    override val charactersRepository: CharactersRepository by lazy {
        CharactersRepositoryImpl(charactersApi)
    }
}