package com.dicoding.listevent.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.listevent.ApiConfig
import com.dicoding.listevent.EventResponse
import com.dicoding.listevent.ListEventsItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {

    private val _upcomingEvents = MutableLiveData<List<ListEventsItem>>()
    val upcomingEvents: LiveData<List<ListEventsItem>> get() = _upcomingEvents

    private val _finishedEvents = MutableLiveData<List<ListEventsItem>>()
    val finishedEvents: LiveData<List<ListEventsItem>> get() = _finishedEvents

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun loadUpcomingEvents() {
        _loading.value = true
        val client = ApiConfig.getApiService().getUpcoming()
        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                _loading.value = false
                if (response.isSuccessful) {
                    _upcomingEvents.value = response.body()?.listEvents?.take(5)
                } else {
                    _error.value = "Failed to load upcoming events"
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _loading.value = false
                _error.value = "Error: ${t.message}"
            }
        })
    }

    fun loadFinishedEvents() {
        _loading.value = true
        val client = ApiConfig.getApiService().getFinish()
        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                _loading.value = false
                if (response.isSuccessful) {
                    _finishedEvents.value = response.body()?.listEvents?.take(5)
                } else {
                    _error.value = "Failed to load finished events"
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _loading.value = false
                _error.value = "Error: ${t.message}"
            }
        })
    }
}
