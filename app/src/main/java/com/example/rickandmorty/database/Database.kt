package com.example.rickandmorty.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.rickandmorty.converter.Converters
import com.example.rickandmorty.data.model.CharacterRemoteKeys
import com.example.rickandmorty.data.model.CharactersResults
import com.example.rickandmorty.data.model.EpisodesRemoteKeys
import com.example.rickandmorty.data.model.EpisodesResults
import com.example.rickandmorty.data.model.LocationsRemoteKeys
import com.example.rickandmorty.data.model.LocationsResults
import com.example.rickandmorty.database.dao.CharacterRemoteKeysDao
import com.example.rickandmorty.database.dao.CharactersDao
import com.example.rickandmorty.database.dao.EpisodesDao
import com.example.rickandmorty.database.dao.EpisodesRemoteKeysDao
import com.example.rickandmorty.database.dao.LocationsDao
import com.example.rickandmorty.database.dao.LocationsRemoteKeysDao

@Database(
    entities = [CharacterRemoteKeys::class, EpisodesRemoteKeys::class, LocationsResults::class, LocationsRemoteKeys::class, EpisodesResults::class, CharactersResults::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class RMDatabase : RoomDatabase() {
    abstract fun charactersDao(): CharactersDao
    abstract fun characterRemoteKeysDao(): CharacterRemoteKeysDao

    abstract fun episodesDao(): EpisodesDao
    abstract fun episodesRemoteKeysDao(): EpisodesRemoteKeysDao

    abstract fun locationsDao(): LocationsDao
    abstract fun locationsRemoteKeysDao(): LocationsRemoteKeysDao
}