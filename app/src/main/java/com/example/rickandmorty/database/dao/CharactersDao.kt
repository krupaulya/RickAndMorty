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

    @Query("SELECT * FROM characters WHERE id IN (:ids)")
    suspend fun getMultipleCachedCharacters(ids: List<Int>): List<CharactersResults>

    // one filter
    @Query("select * from characters where name like :name")
    fun getNameFiltered(name: String): PagingSource<Int, CharactersResults>

    @Query("select * from characters where status = :status")
    fun getStatusFiltered(status: String): PagingSource<Int, CharactersResults>

    @Query("select * from characters where species like :species")
    fun getSpeciesFiltered(species: String): PagingSource<Int, CharactersResults>

    @Query("select * from characters where type like :type")
    fun getTypeFiltered(type: String): PagingSource<Int, CharactersResults>

    @Query("select * from characters where gender = :gender")
    fun getGenderFiltered(gender: String): PagingSource<Int, CharactersResults>

    //two filters
    @Query("select * from characters where name like :name and status = :status")
    fun getNameAndStatusFiltered(name: String, status: String): PagingSource<Int, CharactersResults>

    @Query("SELECT * FROM characters WHERE name LIKE :name AND species like :species")
    fun getNameAndSpeciesFiltered(
        name: String,
        species: String
    ): PagingSource<Int, CharactersResults>

    @Query("SELECT * FROM characters WHERE name LIKE :name AND type like :type")
    fun getNameAndTypeFiltered(name: String, type: String): PagingSource<Int, CharactersResults>

    @Query("SELECT * FROM characters WHERE name LIKE :name AND gender = :gender")
    fun getNameAndGenderFiltered(name: String, gender: String): PagingSource<Int, CharactersResults>

    @Query("SELECT * FROM characters WHERE status = :status AND species like :species")
    fun getStatusAndSpeciesFiltered(
        status: String,
        species: String
    ): PagingSource<Int, CharactersResults>

    @Query("SELECT * FROM characters WHERE status = :status AND type like :type")
    fun getStatusAndTypeFiltered(
        status: String,
        type: String
    ): PagingSource<Int, CharactersResults>

    @Query("SELECT * FROM characters WHERE status = :status AND gender = :gender")
    fun getStatusAndGenderFiltered(
        status: String,
        gender: String
    ): PagingSource<Int, CharactersResults>

    @Query("SELECT * FROM characters WHERE species like :species AND type like :type")
    fun getSpeciesAndTypeFiltered(
        species: String,
        type: String
    ): PagingSource<Int, CharactersResults>

    @Query("SELECT * FROM characters WHERE species like :species AND gender = :gender")
    fun getSpeciesAndGenderFiltered(
        species: String,
        gender: String
    ): PagingSource<Int, CharactersResults>

    @Query("select * from characters where type like :type and gender = :gender")
    fun getTypeAndGenderFiltered(
        type: String,
        gender: String
    ): PagingSource<Int, CharactersResults>

    // three filters
    @Query("select * from characters where name like :name and status = :status and species like :species")
    fun getNameAndStatusAndSpeciesFiltered(
        name: String,
        status: String,
        species: String
    ): PagingSource<Int, CharactersResults>

    @Query("select * from characters where name like :name and status = :status and type like :type")
    fun getNameAndStatusAndTypeFiltered(
        name: String,
        status: String,
        type: String
    ): PagingSource<Int, CharactersResults>

    @Query("select * from characters where name like :name and status = :status and gender = :gender")
    fun getNameAndStatusAndGenderFiltered(
        name: String,
        status: String,
        gender: String
    ): PagingSource<Int, CharactersResults>

    @Query("select * from characters where name like :name and species like :species and type like :type")
    fun getNameAndSpeciesAndTypeFiltered(
        name: String,
        species: String,
        type: String
    ): PagingSource<Int, CharactersResults>

    @Query("select * from characters where name like :name and species like :species and gender = :gender")
    fun getNameAndSpeciesAndGenderFiltered(
        name: String,
        species: String,
        gender: String
    ): PagingSource<Int, CharactersResults>

    @Query("select * from characters where name like :name and type like :type and gender = :gender")
    fun getNameAndTypeAndGenderFiltered(
        name: String,
        type: String,
        gender: String
    ): PagingSource<Int, CharactersResults>

    @Query("select * from characters where status = :status and species like :species and type like :type")
    fun getStatusAndSpeciesAndTypeFiltered(
        status: String,
        species: String,
        type: String
    ): PagingSource<Int, CharactersResults>

    @Query("select * from characters where status = :status and species like :species and gender = :gender")
    fun getStatusAndSpeciesAndGenderFiltered(
        status: String,
        species: String,
        gender: String
    ): PagingSource<Int, CharactersResults>

    @Query("select * from characters where status = :status and type like :type and gender = :gender")
    fun getStatusAndTypeAndGenderFiltered(
        status: String,
        type: String,
        gender: String
    ): PagingSource<Int, CharactersResults>

    @Query("select * from characters where species like :species and type like :type and gender = :gender")
    fun getSpeciesAndTypeAndGenderFiltered(
        species: String,
        type: String,
        gender: String
    ): PagingSource<Int, CharactersResults>

    // four filters
    @Query("select * from characters where name like :name and status = :status and species like :species and type like :type")
    fun getNameAndStatusAndSpeciesAndTypeFiltered(
        name: String,
        status: String,
        species: String,
        type: String
    ): PagingSource<Int, CharactersResults>

    @Query("select * from characters where name like :name and status = :status and species like :species and gender = :gender")
    fun getNameAndStatusAndSpeciesAndGenderFiltered(
        name: String,
        status: String,
        species: String,
        gender: String
    ): PagingSource<Int, CharactersResults>

    @Query("select * from characters where name like :name and status = :status and type like :type and gender = :gender")
    fun getNameAndStatusAndTypeAndGenderFiltered(
        name: String,
        status: String,
        type: String,
        gender: String
    ): PagingSource<Int, CharactersResults>

    @Query("select * from characters where name like :name and species like :species and type like :type and gender = :gender")
    fun getNameAndSpeciesAndTypeAndGenderFiltered(
        name: String,
        species: String,
        type: String,
        gender: String
    ): PagingSource<Int, CharactersResults>

    @Query("select * from characters where status = :status and species like :species and type like :type and gender = :gender")
    fun getStatusAndSpeciesAndTypeAndGenderFiltered(
        status: String,
        species: String,
        type: String,
        gender: String
    ): PagingSource<Int, CharactersResults>

    // five filters
    @Query("select * from characters where name like :name and status = :status and species like :species and type like :type and gender = :gender")
    fun getCharactersFiltered(
        name: String,
        status: String,
        species: String,
        type: String,
        gender: String
    ): PagingSource<Int, CharactersResults>
}