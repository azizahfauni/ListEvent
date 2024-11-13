package com.dicoding.listevent

import android.content.Context

object Injection {
    fun provideRepository(context: Context): EventRepository {
        val apiService = ApiConfig.getApiService() // Retrofit
        val database = FavoriteEventDatabase.getDatabase(context) // Room Database
        val favoriteEventDao = database.favoriteEventDao()

        return EventRepository(apiService, favoriteEventDao)
    }
}