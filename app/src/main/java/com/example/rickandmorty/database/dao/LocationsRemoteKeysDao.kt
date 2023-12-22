package com.example.rickandmorty.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.rickandmorty.data.model.LocationsRemoteKeys

@Dao
interface LocationsRemoteKeysDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocationsRemoteKeys(remoteKey: List<LocationsRemoteKeys>)

    @Query("SELECT * FROM locations_remote_keys WHERE locationId = :locationId")
    suspend fun remoteKeysLocationId(locationId: Int): LocationsRemoteKeys?

    @Query("DELETE FROM locations_remote_keys")
    suspend fun clearLocationRemoteKeys()
}