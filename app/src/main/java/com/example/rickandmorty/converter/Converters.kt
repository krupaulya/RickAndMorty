package com.example.rickandmorty.converter

import androidx.room.TypeConverter
import com.example.rickandmorty.data.model.CharactersResults
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    @TypeConverter
    fun fromOrigin(value: CharactersResults.Origin): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toOrigin(value: String): CharactersResults.Origin {
        val type = object : TypeToken<CharactersResults.Origin>() {}.type
        return Gson().fromJson(value, type)
    }

    @TypeConverter
    fun fromLocation(value: CharactersResults.Location): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toLocation(value: String): CharactersResults.Location {
        val type = object : TypeToken<CharactersResults.Location>() {}.type
        return Gson().fromJson(value, type)
    }


    @TypeConverter
    fun fromString(value: String?): List<String>? {
        if (value == null) {
            return null
        }

        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun toString(value: List<String>?): String? {
        return Gson().toJson(value)
    }
}