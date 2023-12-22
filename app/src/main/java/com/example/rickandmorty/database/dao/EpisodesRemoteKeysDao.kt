package com.example.rickandmorty.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.rickandmorty.data.model.EpisodesRemoteKeys

@Dao
interface EpisodesRemoteKeysDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEpisodesRemoteKeys(remoteKey: List<EpisodesRemoteKeys>)

    @Query("SELECT * FROM episode_remote_keys WHERE episodeId = :episodeId")
    suspend fun remoteKeysEpisodeId(episodeId: Int): EpisodesRemoteKeys?

    @Query("DELETE FROM episode_remote_keys")
    suspend fun clearEpisodesRemoteKeys()
}