package com.example.rickandmorty.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Episodes(
    @PrimaryKey
    @SerializedName("info")
    val info: EpisodesInfo,
    @SerializedName("results")
    val results: List<EpisodesResults>
) {
    data class EpisodesInfo(
        @SerializedName("count")
        val count: Int,
        @SerializedName("next")
        val next: String,
        @SerializedName("pages")
        val pages: Int,
        @SerializedName("prev")
        val prev: Any
    )

    data class EpisodesResults(
        @PrimaryKey
        @SerializedName("id")
        val id: Int,
        @SerializedName("name")
        val name: String,
        @SerializedName("air_date")
        val airDate: String,
        @SerializedName("episode")
        val episode: String,
        @SerializedName("characters")
        val characters: List<String>,
        @SerializedName("url")
        val url: String,
        @SerializedName("created")
        val created: String
    )
}


