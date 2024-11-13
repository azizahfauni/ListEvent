package com.dicoding.listevent

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("/events?active=1")
    fun getUpcoming(): Call<EventResponse>

    @GET("/events?active=0")
    fun getFinish(): Call<EventResponse>

    @GET("/events")
    fun searchEvents(
        @Query("q") query: String
    ): Call<EventResponse>

    @GET("/events")
    suspend fun  getClosestEvent(
        @Query("active") active: Int = 1,  // Event yang sedang aktif (1)
        @Query("limit") limit: Int = 1     // Batasi hanya 1 event terdekat
    ): Response<EventResponse>


}
