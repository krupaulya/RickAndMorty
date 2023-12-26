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

    @Query("delete from episodes")
    suspend fun clearEpisodes()

    @Query("select * from episodes where id = :id")
    fun getEpisodeById(id: Int): EpisodesResults

    @Query("SELECT * FROM episodes WHERE id IN (:ids)")
    suspend fun getEpisodesForCharacters(ids: List<Int>): List<EpisodesResults>

    @Query("select * from episodes where name like :name")
    fun getNameFilteredEpisodes(name: String): PagingSource<Int, EpisodesResults>

    @Query("select * from episodes where episode like :episode")
    fun getEpisodeFilteredEpisodes(episode: String): PagingSource<Int, EpisodesResults>

    @Query("select * from episodes where name like :name and episode like :episode")
    fun getFilteredEpisodes(name: String, episode: String): PagingSource<Int, EpisodesResults>
}