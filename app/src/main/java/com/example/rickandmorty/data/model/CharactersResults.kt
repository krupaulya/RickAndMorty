package com.example.rickandmorty.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
@Entity(tableName = "characters")
data class CharactersResults(
    @PrimaryKey
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
) {
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
}








