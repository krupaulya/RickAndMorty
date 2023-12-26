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

    @Query("select * from locations where name like :name")
    fun getNameFiltered(name: String): PagingSource<Int, LocationsResults>

    @Query("select * from locations where type like :type")
    fun getTypeFiltered(type: String): PagingSource<Int, LocationsResults>

    @Query("select * from locations where dimension like :dimension")
    fun getDimensionFiltered(dimension: String): PagingSource<Int, LocationsResults>

    @Query("select * from locations where name like :name and type like :type")
    fun getNameAndTypeFiltered(name: String, type: String): PagingSource<Int, LocationsResults>

    @Query("select * from locations where name like :name and dimension like :dimension")
    fun getNameAndDimensionFiltered(name: String, dimension: String): PagingSource<Int, LocationsResults>

    @Query("select * from locations where type like :type and dimension like :dimension")
    fun getTypeAndDimensionFiltered(type: String, dimension: String): PagingSource<Int, LocationsResults>

    @Query("select * from locations where name like :name and type like :type and dimension like :dimension")
    fun getLocationsFiltered(name: String, type: String, dimension: String): PagingSource<Int, LocationsResults>
}