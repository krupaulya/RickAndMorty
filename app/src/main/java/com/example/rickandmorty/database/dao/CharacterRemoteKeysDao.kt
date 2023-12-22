package com.example.rickandmorty.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.rickandmorty.data.model.CharacterRemoteKeys

@Dao
interface CharacterRemoteKeysDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacterRemoteKeys(remoteKey: List<CharacterRemoteKeys>)

    @Query("SELECT * FROM character_remote_keys WHERE characterId = :characterId")
    suspend fun remoteKeysCharacterId(characterId: Int): CharacterRemoteKeys?

    @Query("DELETE FROM character_remote_keys")
    suspend fun clearCharacterRemoteKeys()
}