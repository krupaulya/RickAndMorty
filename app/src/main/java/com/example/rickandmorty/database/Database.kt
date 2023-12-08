package com.example.rickandmorty.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.rickandmorty.data.model.CharactersInfo
import com.example.rickandmorty.data.model.Episodes
import com.example.rickandmorty.data.model.Locations
import com.example.rickandmorty.database.dao.CharactersDao

@Database(entities = [CharactersInfo::class, Locations::class, Episodes::class], version = 2)
abstract class RMDatabase: RoomDatabase() {
    abstract fun charactersDao(): CharactersDao

    companion object {
        @Volatile
        private var INSTANCE: RMDatabase? = null

        fun getInstance(context: Context): RMDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        RMDatabase::class.java,
                        "database.db"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}