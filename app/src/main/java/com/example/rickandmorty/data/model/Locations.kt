package com.example.rickandmorty.data.model

import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity
data class Locations(
    @SerializedName("info")
    val id: Int,
    @SerializedName("info")
    val name: String,
    @SerializedName("info")
    val type: String,
    @SerializedName("info")
    val dimension: String,
    @SerializedName("info")
    val residents: List<String>,
    @SerializedName("info")
    val url: String,
    @SerializedName("info")
    val created: String
)
