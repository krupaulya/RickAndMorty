package com.example.rickandmorty.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "locations")
data class LocationsResults(
    @PrimaryKey
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


