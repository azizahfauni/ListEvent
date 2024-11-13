package com.dicoding.listevent.ui.favorite

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.listevent.DetailActivity
import com.dicoding.listevent.FavoriteEventAdapter
import com.dicoding.listevent.FavoriteEventDatabase
import com.dicoding.listevent.databinding.FragmentFavoriteBinding

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private lateinit var favoriteDatabase: FavoriteEventDatabase
    private lateinit var favoriteEventAdapter: FavoriteEventAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favoriteDatabase = FavoriteEventDatabase.getDatabase(requireContext())

        // Inisialisasi adapter dengan listener untuk handle klik
        favoriteEventAdapter = FavoriteEventAdapter { favoriteEvent ->
            // Kirim data lengkap dari favoriteEvent ke DetailActivity
            val intent = Intent(context, DetailActivity::class.java).apply {
                putExtra(DetailActivity.EVENT_DETAIL, favoriteEvent.toListEventsItem())  // Kirim seluruh event detail
            }
            startActivity(intent)
        }

        // Setup RecyclerView
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = favoriteEventAdapter
        }

        // Load favorite events dari database
        loadFavorites()
    }

    private fun loadFavorites() {
        favoriteDatabase.favoriteEventDao().getAllFavorites().observe(viewLifecycleOwner) { favoriteEvents ->
            if (favoriteEvents.isEmpty()) {
                binding.tvEmptyMessage.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
            } else {
                binding.tvEmptyMessage.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
                favoriteEventAdapter.submitList(favoriteEvents)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
