package com.example.rickandmorty.data.model

import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity
data class CharactersInfo(
    @SerializedName("info")
    val info: Info,
    @SerializedName("results")
    val results: List<Results>
)

data class Info(
    @SerializedName("count")
    val count: Int,
    @SerializedName("next")
    val next: String,
    @SerializedName("pages")
    val pages: Int,
    @SerializedName("prev")
    val prev: Any
)

data class Results(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("species")
    val species: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("gender")
    val gender: String,
    @SerializedName("origin")
    val origin: Origin,
    @SerializedName("location")
    val location: Location,
    @SerializedName("image")
    val image: String,
    @SerializedName("episode")
    val episode: List<String>,
    @SerializedName("url")
    val url: String,
    @SerializedName("created")
    val created: String
)

data class Origin(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
)

data class Location(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
)


