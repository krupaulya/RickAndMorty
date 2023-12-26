package com.example.rickandmorty.data.model

import com.google.gson.annotations.SerializedName

data class EpisodesResponse(
    @SerializedName("info")
    val info: EpisodesInfo,
    @SerializedName("results")
    val results: List<EpisodesResults>
)
