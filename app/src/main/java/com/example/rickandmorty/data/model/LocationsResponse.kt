package com.example.rickandmorty.data.model

import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class LocationsResponse(
    @PrimaryKey
    @SerializedName("info")
    val info: LocationsInfo,
    @SerializedName("results")
    val results: List<LocationsResults>
)
