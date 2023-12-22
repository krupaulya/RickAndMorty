package com.example.rickandmorty.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.rickandmorty.data.model.EpisodesResults

@Dao
interface EpisodesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEpisodes(episodes: List<EpisodesResults>)

    @Query("select * from episodes")
    fun getAllEpisodes(): PagingSource<Int, EpisodesResults>

    @Query("DELETE FROM episodes")
    suspend fun clearEpisodes()

    @Query("select * from episodes where id = :id")
    fun getEpisodeById(id: Int): EpisodesResults
}