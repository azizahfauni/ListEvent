package com.dicoding.listevent

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.listevent.databinding.ItemEventBinding

class FavoriteEventAdapter(private val onItemClick: (FavoriteEvent) -> Unit) :
    ListAdapter<FavoriteEvent, FavoriteEventAdapter.FavoriteEventViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteEventViewHolder {
        val binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteEventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteEventViewHolder, position: Int) {
        val favoriteEvent = getItem(position)
        holder.bind(favoriteEvent, onItemClick)
    }

    class FavoriteEventViewHolder(private val binding: ItemEventBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: FavoriteEvent, onItemClick: (FavoriteEvent) -> Unit) {
            binding.tvItem.text = event.name

            Glide.with(binding.root.context)
                .load(event.mediaCover)
                .into(binding.ivEventLogo)

            // Listener untuk klik item
            binding.root.setOnClickListener {
                onItemClick(event) // Kirim seluruh objek FavoriteEvent
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FavoriteEvent>() {
            override fun areItemsTheSame(oldItem: FavoriteEvent, newItem: FavoriteEvent): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: FavoriteEvent, newItem: FavoriteEvent): Boolean {
                return oldItem == newItem
            }
        }
    }
}
