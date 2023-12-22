package com.example.rickandmorty.data.model

import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class CharactersInfo(
    @SerializedName("count")
    val count: Int,
    @PrimaryKey
    @SerializedName("next")
    val next: String,
    @SerializedName("pages")
    val pages: Int,
    @SerializedName("prev")
    val prev: Any
)

