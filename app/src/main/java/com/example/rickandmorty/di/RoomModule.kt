package com.example.rickandmorty.di

import android.content.Context
import androidx.room.Room
import com.example.rickandmorty.database.RMDatabase
import com.example.rickandmorty.database.dao.CharacterRemoteKeysDao
import com.example.rickandmorty.database.dao.CharactersDao
import com.example.rickandmorty.database.dao.EpisodesDao
import com.example.rickandmorty.database.dao.EpisodesRemoteKeysDao
import com.example.rickandmorty.database.dao.LocationsDao
import com.example.rickandmorty.database.dao.LocationsRemoteKeysDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module

@InstallIn(SingletonComponent::class)
object RoomModule {

    @Provides
    @Singleton
    fun provideRMDatabase(@ApplicationContext context: Context): RMDatabase {
        return Room.databaseBuilder(context, RMDatabase::class.java, "database.db")
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideCharactersDao(rmDatabase: RMDatabase): CharactersDao {
        return rmDatabase.charactersDao()
    }

    @Provides
    @Singleton
    fun provideCharacterRemoteKeysDao(rmDatabase: RMDatabase): CharacterRemoteKeysDao {
        return rmDatabase.characterRemoteKeysDao()
    }

    @Provides
    @Singleton
    fun provideEpisodesDao(rmDatabase: RMDatabase): EpisodesDao {
        return rmDatabase.episodesDao()
    }

    @Provides
    @Singleton
    fun provideEpisodesRemoteKeysDao(rmDatabase: RMDatabase): EpisodesRemoteKeysDao {
        return rmDatabase.episodesRemoteKeysDao()
    }

    @Provides
    @Singleton
    fun provideLocationsDao(rmDatabase: RMDatabase): LocationsDao {
        return rmDatabase.locationsDao()
    }

    @Provides
    @Singleton
    fun provideLocationsRemoteKeysDao(rmDatabase: RMDatabase): LocationsRemoteKeysDao {
        return rmDatabase.locationsRemoteKeysDao()
    }

}