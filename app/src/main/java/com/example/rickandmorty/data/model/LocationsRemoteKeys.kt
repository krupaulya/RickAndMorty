package com.example.rickandmorty.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "locations_remote_keys")
data class LocationsRemoteKeys(
    @PrimaryKey
    val locationId: Int,
    val prevKey: Int?,
    val nextKey: Int?
)
