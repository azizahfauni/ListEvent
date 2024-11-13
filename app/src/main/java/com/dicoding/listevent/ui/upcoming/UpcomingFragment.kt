package com.dicoding.listevent.ui.upcoming

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
import com.dicoding.listevent.databinding.FragmentUpcomingBinding

class UpcomingFragment : Fragment() {

    private var _binding: FragmentUpcomingBinding? = null
    private val binding get() = _binding!!

    private lateinit var eventAdapter: ListEventsAdapter

    // Menggunakan viewModels untuk mendapatkan instance dari UpcomingViewModel
    private val upcomingViewModel: UpcomingViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)
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

        // Observe data events dari ViewModel
        upcomingViewModel.events.observe(viewLifecycleOwner) { events ->
            eventAdapter.submitList(events)
        }

        // Observe status loading dari ViewModel
        upcomingViewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }

        // Observe error dari ViewModel
        upcomingViewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
            }
        }

        // Panggil API untuk memuat events jika belum ada data
        if (upcomingViewModel.events.value == null) {
            upcomingViewModel.loadUpcomingEvents()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
