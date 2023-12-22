package com.example.rickandmorty.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.rickandmorty.data.model.CharactersResults

@Dao
interface CharactersDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCharacters(characters: List<CharactersResults>)

    @Query("select * from characters")
    fun getAllCharacters(): PagingSource<Int, CharactersResults>

    @Query("DELETE FROM characters")
    suspend fun clearCharacters()

    @Query("select * from characters where id = :id")
    fun getCharacterById(id: Int): CharactersResults

}