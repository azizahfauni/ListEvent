package com.dicoding.listevent.ui.finished

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.listevent.DetailActivity
import com.dicoding.listevent.ListEventsAdapter
import com.dicoding.listevent.ListEventsItem
import com.dicoding.listevent.R
import com.dicoding.listevent.databinding.FragmentFinishedBinding

class FinishedFragment : Fragment() {

    private var _binding: FragmentFinishedBinding? = null
    private val binding get() = _binding!!

    private lateinit var eventAdapter: ListEventsAdapter
    private var eventList: List<ListEventsItem> = emptyList()  // Data event asli untuk filter pencarian

    private val finishedViewModel: FinishedViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFinishedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize RecyclerView dan adapter
        eventAdapter = ListEventsAdapter { event ->
            val intent = Intent(requireContext(), DetailActivity::class.java).apply {
                putExtra("EVENT_DETAIL", event) // Kirim event sebagai Parcelable
            }
            startActivity(intent)
        }

        binding.rvEvents.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = eventAdapter
        }

        // Observe LiveData dari ViewModel
        finishedViewModel.events.observe(viewLifecycleOwner) { events ->
            eventList = events ?: emptyList() // Simpan list untuk filtering
            eventAdapter.submitList(eventList)

            if (events.isNullOrEmpty()) {
                binding.textNoData.visibility = View.VISIBLE
                binding.rvEvents.visibility = View.GONE
            } else {
                binding.textNoData.visibility = View.GONE
                binding.rvEvents.visibility = View.VISIBLE
            }
        }

        finishedViewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }

        finishedViewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
            }
        }

        // Setup search feature
        setupSearchView()

        // Panggil API untuk memuat events jika belum ada data
        if (finishedViewModel.events.value == null) {
            finishedViewModel.loadFinishedEvents()
        }
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true // Tidak diperlukan aksi pada submit
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val filteredList = eventList.filter {
                    it.name.contains(newText ?: "", ignoreCase = true)
                }
                eventAdapter.submitList(filteredList)

                // Menampilkan pesan "event tidak ditemukan" jika hasil pencarian kosong
                if (filteredList.isEmpty()) {
                    binding.textNoData.text = getString(R.string.event_tidak_ditemukan)
                    binding.textNoData.visibility = View.VISIBLE
                    binding.rvEvents.visibility = View.GONE
                } else {
                    binding.textNoData.visibility = View.GONE
                    binding.rvEvents.visibility = View.VISIBLE
                }
                return true
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
