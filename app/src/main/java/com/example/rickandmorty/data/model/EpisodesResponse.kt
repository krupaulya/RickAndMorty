package com.example.rickandmorty.data.model

import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class EpisodesResponse(
    @PrimaryKey
    @SerializedName("info")
    val info: EpisodesInfo,
    @SerializedName("results")
    val results: List<EpisodesResults>
)
