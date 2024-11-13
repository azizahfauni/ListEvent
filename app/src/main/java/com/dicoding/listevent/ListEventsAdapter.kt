package com.dicoding.listevent

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.listevent.databinding.ItemEventBinding

class ListEventsAdapter(private val onItemClick: (ListEventsItem) -> Unit) : ListAdapter<ListEventsItem, ListEventsAdapter.EventViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val eventItem = getItem(position)
        holder.bind(eventItem, onItemClick)
    }

    class EventViewHolder(private val binding: ItemEventBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: ListEventsItem, ignoredOnItemClick: (ListEventsItem) -> Unit) {
            binding.tvItem.text = event.name

            Glide.with(binding.root.context)
                .load(event.imageLogo)
                .into(binding.ivEventLogo)

            // Listener untuk klik item
            binding.root.setOnClickListener {
                ignoredOnItemClick(event) // Kirim seluruh objek ListEventsItem
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListEventsItem>() {
            override fun areItemsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
