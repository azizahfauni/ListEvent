package com.dicoding.listevent

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.dicoding.listevent.databinding.ActivityDetailBinding
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var favoriteDatabase: FavoriteEventDatabase
    private var isFavorite = false
    private lateinit var event: ListEventsItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi database
        favoriteDatabase = FavoriteEventDatabase.getDatabase(this)

        // Tampilkan ProgressBar saat data sedang dimuat
        binding.progressBar.visibility = View.VISIBLE

        // Terima data dari Intent
        event = intent.getParcelableExtra(EVENT_DETAIL) ?: return

        // Cek apakah event sudah menjadi favorit
        checkIfFavorite(event.id)

        // Tampilkan data event
        displayEventDetails(event)

        // Set click listener untuk tombol buka link
        binding.btnOpenLink.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(event.link))
            startActivity(intent)
        }

        // Set click listener untuk FloatingActionButton favorit
        binding.fabFavorite.setOnClickListener {
            toggleFavoriteStatus(event)
        }

        // Sembunyikan ProgressBar setelah data ditampilkan
        binding.progressBar.visibility = View.GONE
    }

    private fun displayEventDetails(event: ListEventsItem) {
        binding.tvEventName.text = event.name
        binding.tvOwnerName.text = event.ownerName
        binding.tvEventTime.text = event.beginTime
        binding.tvRemainingQuota.text = (event.quota - event.registrants).toString()
        binding.tvEventDescription.text = HtmlCompat.fromHtml(event.description, HtmlCompat.FROM_HTML_MODE_LEGACY)

        // Muat gambar dengan Glide
        Glide.with(this)
            .load(event.mediaCover)
            .into(binding.ivEventImage)
    }

    private fun checkIfFavorite(eventId: Int) {
        lifecycleScope.launch {
            val favoriteEvent = favoriteDatabase.favoriteEventDao().findFavoriteById(eventId)
            isFavorite = favoriteEvent != null
            updateFavoriteIcon()
        }
    }

    private fun toggleFavoriteStatus(event: ListEventsItem) {
        lifecycleScope.launch {
            if (isFavorite) {
                // Hapus dari favorit
                favoriteDatabase.favoriteEventDao().removeFromFavorites(FavoriteEvent(event.id, event.name, event.ownerName, event.mediaCover, event.beginTime, event.quota, event.registrants, event.description, event.link))
                isFavorite = false
            } else {
                // Tambahkan ke favorit
                favoriteDatabase.favoriteEventDao().addToFavorites(FavoriteEvent(event.id, event.name, event.ownerName, event.mediaCover, event.beginTime, event.quota, event.registrants, event.description, event.link))
                isFavorite = true
            }
            updateFavoriteIcon()
        }
    }

    private fun updateFavoriteIcon() {
        if (isFavorite) {
            binding.fabFavorite.setImageResource(R.drawable.favorite1) // Ganti dengan ikon favorit terisi
        } else {
            binding.fabFavorite.setImageResource(R.drawable.favorite2) // Ganti dengan ikon favorit kosong
        }
    }

    companion object {
        const val EVENT_ID = "EVENT_ID" // Kunci untuk menerima event ID
        const val EVENT_DETAIL = "EVENT_DETAIL" // Kunci untuk menerima parcelable event detail
    }
}
