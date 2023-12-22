package com.example.rickandmorty.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.rickandmorty.data.model.LocationsResults

@Dao
interface LocationsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLocations(locations: List<LocationsResults>)

    @Query("select * from locations")
    fun getAllLocations(): PagingSource<Int, LocationsResults>

    @Query("DELETE FROM locations")
    suspend fun clearLocations()

    @Query("select * from locations where id = :id")
    fun getLocationById(id: Int): LocationsResults
}