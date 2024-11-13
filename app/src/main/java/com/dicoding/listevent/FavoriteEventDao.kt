package com.dicoding.listevent

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FavoriteEventDao {

    @Query("SELECT * FROM favorite_events")
    fun getAllFavorites(): LiveData<List<FavoriteEvent>> // Menggunakan LiveData

    @Insert
    suspend fun addToFavorites(favoriteEvent: FavoriteEvent)

    @Delete
    suspend fun removeFromFavorites(favoriteEvent: FavoriteEvent)

    @Query("SELECT * FROM favorite_events WHERE id = :id LIMIT 1")
    suspend fun findFavoriteById(id: Int): FavoriteEvent?
}
