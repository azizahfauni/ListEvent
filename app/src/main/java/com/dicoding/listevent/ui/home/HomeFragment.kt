package com.dicoding.listevent.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.listevent.DetailActivity
import com.dicoding.listevent.ListEventsAdapter
import com.dicoding.listevent.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var eventAdapter: ListEventsAdapter
    private lateinit var finishedEventAdapter: ListEventsAdapter

    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize RecyclerView untuk upcoming events (Horizontal)
        eventAdapter = ListEventsAdapter { event ->
            val intent = Intent(requireContext(), DetailActivity::class.java).apply {
                putExtra("EVENT_DETAIL", event) // Kirim objek ListEventsItem
            }
            startActivity(intent)
        }

        binding.rvUpcomingEvents.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = eventAdapter
        }

        // Initialize RecyclerView untuk finished events (Vertical)
        finishedEventAdapter = ListEventsAdapter { event ->
            val intent = Intent(requireContext(), DetailActivity::class.java).apply {
                putExtra("EVENT_DETAIL", event) // Kirim event sebagai Parcelable
            }
            startActivity(intent)
        }
        binding.rvFinishedEvents.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = finishedEventAdapter
        }

        // Observe LiveData dari ViewModel
        homeViewModel.upcomingEvents.observe(viewLifecycleOwner, Observer { events ->
            eventAdapter.submitList(events)
        })

        homeViewModel.finishedEvents.observe(viewLifecycleOwner, Observer { events ->
            finishedEventAdapter.submitList(events)
        })

        homeViewModel.loading.observe(viewLifecycleOwner, Observer { isLoading ->
            showLoading(isLoading)
        })

        homeViewModel.error.observe(viewLifecycleOwner, Observer { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
            }
        })

        // Load upcoming dan finished events
        if (homeViewModel.upcomingEvents.value == null) {
            homeViewModel.loadUpcomingEvents()
        }
        if (homeViewModel.finishedEvents.value == null) {
            homeViewModel.loadFinishedEvents()
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
