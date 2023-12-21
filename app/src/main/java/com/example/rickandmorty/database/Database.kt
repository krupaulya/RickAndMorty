package com.example.rickandmorty.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.rickandmorty.converter.Converters
import com.example.rickandmorty.data.model.Characters
import com.example.rickandmorty.data.model.Episodes
import com.example.rickandmorty.data.model.Locations
import com.example.rickandmorty.database.dao.CharactersDao

@Database(entities = [Characters::class, Locations::class, Episodes::class], version = 3, exportSchema = false)
@TypeConverters(Converters::class)
abstract class RMDatabase: RoomDatabase() {
    abstract fun charactersDao(): CharactersDao
}