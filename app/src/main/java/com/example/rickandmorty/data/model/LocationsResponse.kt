package com.example.rickandmorty.data.model

import com.google.gson.annotations.SerializedName

data class LocationsResponse(
    @SerializedName("info")
    val info: LocationsInfo,
    @SerializedName("results")
    val results: List<LocationsResults>
)
