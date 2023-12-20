package com.example.rickandmorty.converter

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.example.rickandmorty.data.model.Characters
import com.example.rickandmorty.data.model.Episodes
import com.example.rickandmorty.data.model.Locations
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


@ProvidedTypeConverter
class Converters {
    @TypeConverter
    fun fromCharactersInfo(value: Characters.CharactersInfo): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toCharactersInfo(value: String): Characters.CharactersInfo {
        val type = object : TypeToken<Characters.CharactersInfo>() {}.type
        return Gson().fromJson(value, type)
    }

    @TypeConverter
    fun fromCharactersResultsList(value: List<Characters.CharactersResults?>?): String? {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toCharactersResultsList(value: String?): List<Characters.CharactersResults>? {
        if (value == null) {
            return emptyList<Characters.CharactersResults>()
        }
        val type = object : TypeToken<List<Characters.CharactersResults?>?>() {}.type
        return Gson().fromJson(value, type)
    }

    @TypeConverter
    fun fromOrigin(value: Characters.CharactersResults.Origin): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toOrigin(value: String): Characters.CharactersResults.Origin {
        val type = object : TypeToken<Characters.CharactersResults.Origin>() {}.type
        return Gson().fromJson(value, type)
    }

    @TypeConverter
    fun fromLocation(value: Characters.CharactersResults.Location): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toLocation(value: String): Characters.CharactersResults.Location {
        val type = object : TypeToken<Characters.CharactersResults.Location>() {}.type
        return Gson().fromJson(value, type)
    }

    @TypeConverter
    fun fromEpisodesInfo(value: Episodes.EpisodesInfo?): String? {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toEpisodesInfo(value: String?): Episodes.EpisodesInfo? {

        val type = object : TypeToken<Episodes.EpisodesInfo?>() {}.type
        return Gson().fromJson(value, type)
    }

    @TypeConverter
    fun fromEpisodesResults(value: List<Episodes.EpisodesResults?>?): String? {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toEpisodesResults(value: String?): List<Episodes.EpisodesResults>? {
        if (value == null) {
            return emptyList<Episodes.EpisodesResults>()
        }
        val type = object : TypeToken<List<Episodes.EpisodesResults?>?>() {}.type
        return Gson().fromJson(value, type)
    }

    @TypeConverter
    fun fromLocationsInfo(value: Locations.LocationsInfo): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toLocationsInfo(value: String): Locations.LocationsInfo {
        val type = object : TypeToken<Locations.LocationsInfo>() {}.type
        return Gson().fromJson(value, type)
    }

    @TypeConverter
    fun fromLocationsResults(value: List<Locations.LocationsResults?>?): String? {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toLocationsResults(value: String?): List<Locations.LocationsResults>? {
        if (value == null) {
            return emptyList<Locations.LocationsResults>()
        }
        val type = object : TypeToken<List<Locations.LocationsResults?>?>() {}.type
        return Gson().fromJson(value, type)
    }
}