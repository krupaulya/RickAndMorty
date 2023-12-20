package com.example.rickandmorty.di

import android.content.Context
import androidx.room.Room
import com.example.rickandmorty.database.RMDatabase
import com.example.rickandmorty.database.dao.CharactersDao
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
    fun provideRMDao(rmDatabase: RMDatabase): CharactersDao {
        return rmDatabase.charactersDao()
    }
}