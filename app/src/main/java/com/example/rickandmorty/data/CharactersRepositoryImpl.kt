package com.example.rickandmorty.data

import com.example.rickandmorty.domain.CharactersRepository

class CharactersRepositoryImpl(
    private val api: CharactersApi
): CharactersRepository {
}