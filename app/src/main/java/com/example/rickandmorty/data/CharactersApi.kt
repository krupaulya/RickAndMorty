package com.example.rickandmorty.data


import com.example.rickandmorty.data.model.CharactersResponse
import com.example.rickandmorty.data.model.CharactersResults
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface CharactersApi {
    @GET("character")
    suspend fun getCharacters(
        @Query("page") page: Int
    ): CharactersResponse

    @GET("character/{id}")
    suspend fun getCharacterById(
        @Path("id") id: Int
    ): Response<CharactersResults>

    @GET("character/{ids}")
    suspend fun getMultipleCharacters(
        @Path("ids") ids: List<Int>
    ): Response<List<CharactersResults>>

    // One filter
    @GET("character/")
    suspend fun getStatusFiltered(
        @Query("page") page: Int,
        @Query("status") status: String
    ): CharactersResponse

    @GET("character/")
    suspend fun getNameFiltered(
        @Query("page") page: Int,
        @Query("name") name: String
    ): CharactersResponse

    @GET("character/")
    suspend fun getTypeFiltered(
        @Query("page") page: Int,
        @Query("type") type: String
    ): CharactersResponse

    @GET("character/")
    suspend fun getGenderFiltered(
        @Query("page") page: Int,
        @Query("gender") gender: String
    ): CharactersResponse

    @GET("character/")
    suspend fun getSpeciesFiltered(
        @Query("page") page: Int,
        @Query("species") species: String
    ): CharactersResponse

    // Two filters
    @GET("character/")
    suspend fun getNameAndStatusFiltered(
        @Query("page") page: Int,
        @Query("name") name: String,
        @Query("status") status: String,
    ): CharactersResponse

    @GET("character/")
    suspend fun getNameAndSpeciesFiltered(
        @Query("page") page: Int,
        @Query("name") name: String,
        @Query("species") species: String
    ): CharactersResponse

    @GET("character/")
    suspend fun getNameAndTypeFiltered(
        @Query("page") page: Int,
        @Query("name") name: String,
        @Query("type") type: String,
    ): CharactersResponse

    @GET("character/")
    suspend fun getNameAndGenderFiltered(
        @Query("page") page: Int,
        @Query("name") name: String,
        @Query("gender") gender: String,
    ): CharactersResponse

    @GET("character/")
    suspend fun getStatusAndSpeciesFiltered(
        @Query("page") page: Int,
        @Query("status") status: String,
        @Query("species") species: String
    ): CharactersResponse

    @GET("character/")
    suspend fun getStatusAndTypeFiltered(
        @Query("page") page: Int,
        @Query("status") status: String,
        @Query("type") type: String
    ): CharactersResponse

    @GET("character/")
    suspend fun getStatusAndGenderFiltered(
        @Query("page") page: Int,
        @Query("status") status: String,
        @Query("gender") gender: String
    ): CharactersResponse

    @GET("character/")
    suspend fun getSpeciesAndTypeFiltered(
        @Query("page") page: Int,
        @Query("species") species: String,
        @Query("type") type: String
    ): CharactersResponse

    @GET("character/")
    suspend fun getSpeciesAndGenderFiltered(
        @Query("page") page: Int,
        @Query("species") species: String,
        @Query("gender") gender: String
    ): CharactersResponse

    @GET("character/")
    suspend fun getTypeAndGenderFiltered(
        @Query("page") page: Int,
        @Query("type") type: String,
        @Query("gender") gender: String
    ): CharactersResponse

    // Three filters
    @GET("character/")
    suspend fun getNameAndStatusAndSpeciesFiltered(
        @Query("page") page: Int,
        @Query("name") name: String,
        @Query("status") status: String,
        @Query("species") species: String,
    ): CharactersResponse

    @GET("character/")
    suspend fun getNameAndStatusAndTypeFiltered(
        @Query("page") page: Int,
        @Query("name") name: String,
        @Query("status") status: String,
        @Query("type") type: String,
    ): CharactersResponse

    @GET("character/")
    suspend fun getNameAndStatusAndGenderFiltered(
        @Query("page") page: Int,
        @Query("name") name: String,
        @Query("status") status: String,
        @Query("gender") gender: String,
    ): CharactersResponse

    @GET("character/")
    suspend fun getNameAndSpeciesAndTypeFiltered(
        @Query("page") page: Int,
        @Query("name") name: String,
        @Query("species") species: String,
        @Query("type") type: String,
    ): CharactersResponse

    @GET("character/")
    suspend fun getNameAndSpeciesAndGenderFiltered(
        @Query("page") page: Int,
        @Query("name") name: String,
        @Query("species") species: String,
        @Query("gender") gender: String,
    ): CharactersResponse

    @GET("character/")
    suspend fun getNameAndTypeAndGenderFiltered(
        @Query("page") page: Int,
        @Query("name") name: String,
        @Query("type") type: String,
        @Query("gender") gender: String,
    ): CharactersResponse

    @GET("character/")
    suspend fun getStatusAndSpeciesAndTypeFiltered(
        @Query("page") page: Int,
        @Query("status") status: String,
        @Query("species") species: String,
        @Query("type") type: String,
    ): CharactersResponse

    @GET("character/")
    suspend fun getStatusAndSpeciesAndGenderFiltered(
        @Query("page") page: Int,
        @Query("status") status: String,
        @Query("species") species: String,
        @Query("gender") gender: String,
    ): CharactersResponse

    @GET("character/")
    suspend fun getStatusAndTypeAndGenderFiltered(
        @Query("page") page: Int,
        @Query("status") status: String,
        @Query("type") type: String,
        @Query("gender") gender: String,
    ): CharactersResponse

    @GET("character/")
    suspend fun getSpeciesAndTypeAndGenderFiltered(
        @Query("page") page: Int,
        @Query("species") species: String,
        @Query("type") type: String,
        @Query("gender") gender: String,
    ): CharactersResponse

    // Four filters
    @GET("character/")
    suspend fun getNameAndStatusAndSpeciesAndTypeFiltered(
        @Query("page") page: Int,
        @Query("name") name: String,
        @Query("status") status: String,
        @Query("species") species: String,
        @Query("type") type: String,
    ): CharactersResponse

    @GET("character/")
    suspend fun getNameAndStatusAndSpeciesAndGenderFiltered(
        @Query("page") page: Int,
        @Query("name") name: String,
        @Query("status") status: String,
        @Query("species") species: String,
        @Query("gender") gender: String,
    ): CharactersResponse

    @GET("character/")
    suspend fun getNameAndStatusAndTypeAndGenderFiltered(
        @Query("page") page: Int,
        @Query("name") name: String,
        @Query("status") status: String,
        @Query("type") type: String,
        @Query("gender") gender: String
    ): CharactersResponse

    @GET("character/")
    suspend fun getNameAndSpeciesAndTypeAndGenderFiltered(
        @Query("page") page: Int,
        @Query("name") name: String,
        @Query("species") species: String,
        @Query("type") type: String,
        @Query("gender") gender: String
    ): CharactersResponse

    @GET("character/")
    suspend fun getStatusAndSpeciesAndTypeAndGenderFiltered(
        @Query("page") page: Int,
        @Query("status") status: String,
        @Query("species") species: String,
        @Query("type") type: String,
        @Query("gender") gender: String
    ): CharactersResponse

    // Five filters
    @GET("character/")
    suspend fun getCharactersFiltered(
        @Query("page") page: Int,
        @Query("name") name: String?,
        @Query("status") status: String?,
        @Query("species") species: String?,
        @Query("type") type: String?,
        @Query("gender") gender: String?
    ): CharactersResponse

}