package com.example.rickandmorty.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "episode_remote_keys")
data class EpisodesRemoteKeys(
    @PrimaryKey
    val episodeId: Int,
    val prevKey: Int?,
    val nextKey: Int?
)
