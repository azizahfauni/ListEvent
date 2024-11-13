package com.dicoding.listevent

import androidx.lifecycle.LiveData
import retrofit2.awaitResponse

class EventRepository(
    private val apiService: ApiService,
    private val favoriteEventDao: FavoriteEventDao
) {
    // Mengambil event yang akan datang
    suspend fun getUpcomingEvents(): EventResponse {
        return apiService.getUpcoming().awaitResponse().body() ?: EventResponse(emptyList(), true, "No data")
    }

    // Mengambil event yang sudah selesai
    suspend fun getFinishedEvents(): EventResponse {
        return apiService.getFinish().awaitResponse().body() ?: EventResponse(emptyList(), true, "No data")
    }

    // Pencarian event
    suspend fun searchEvents(query: String): EventResponse {
        return apiService.searchEvents(query).awaitResponse().body() ?: EventResponse(emptyList(), true, "No data")
    }

    // Mengambil event terdekat
    suspend fun getClosestEvent(): EventResponse {
        return apiService.getClosestEvent().body() ?: EventResponse(emptyList(), true, "No data")
    }

    // Menambahkan event ke favorit
    suspend fun addToFavorites(event: FavoriteEvent) {
        favoriteEventDao.addToFavorites(event)
    }

    // Menghapus event dari favorit
    suspend fun removeFromFavorites(event: FavoriteEvent) {
        favoriteEventDao.removeFromFavorites(event)
    }

    // Mengambil semua event favorit
    fun getAllFavoriteEvents(): LiveData<List<FavoriteEvent>> {
        return favoriteEventDao.getAllFavorites()
    }

    // Memeriksa apakah event sudah difavoritkan
    suspend fun findFavoriteById(id: Int): FavoriteEvent? {
        return favoriteEventDao.findFavoriteById(id)
    }
}
