package com.dicoding.listevent

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_events")
data class FavoriteEvent(
    @PrimaryKey val id: Int,
    val name: String,
    val ownerName: String,
    val mediaCover: String,
    val beginTime: String,
    val quota: Int,
    val registrants: Int,
    val description: String,
    val link: String
) {
    fun toListEventsItem(): ListEventsItem {
        return ListEventsItem(
            id = this.id,
            name = this.name,
            ownerName = this.ownerName,
            mediaCover = this.mediaCover,
            beginTime = this.beginTime,
            quota = this.quota,
            registrants = this.registrants,
            description = this.description,
            link = this.link,
            summary = "",  // Optional: jika tidak ada data, bisa dikosongkan
            imageLogo = this.mediaCover,  // Menggunakan mediaCover sebagai imageLogo
            cityName = "",  // Bisa disesuaikan
            endTime = "",  // Optional
            category = ""  // Optional
        )
    }
}
