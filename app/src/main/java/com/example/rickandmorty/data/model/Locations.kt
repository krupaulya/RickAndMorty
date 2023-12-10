package com.example.rickandmorty.data.model

import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity
data class Locations(
    @SerializedName("info")
    val info: LocationsInfo,
    @SerializedName("results")
    val results: List<LocationsResults>
) {
    data class LocationsInfo(
        @SerializedName("count")
        val count: Int,
        @SerializedName("next")
        val next: String,
        @SerializedName("pages")
        val pages: Int,
        @SerializedName("prev")
        val prev: Any
    )

    data class LocationsResults(
        @SerializedName("id")
        val id: Int,
        @SerializedName("name")
        val name: String,
        @SerializedName("type")
        val type: String,
        @SerializedName("dimension")
        val dimension: String,
        @SerializedName("residents")
        val residents: List<String>,
        @SerializedName("url")
        val url: String,
        @SerializedName("created")
        val created: String
    )
}


