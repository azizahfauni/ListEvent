package com.dicoding.listevent.ui.upcoming

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.listevent.ApiConfig
import com.dicoding.listevent.EventResponse
import com.dicoding.listevent.ListEventsItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpcomingViewModel : ViewModel() {

    private val _events = MutableLiveData<List<ListEventsItem>>()
    val events: LiveData<List<ListEventsItem>> get() = _events

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
                    _events.value = response.body()?.listEvents
                } else {
                    _error.value = "Failed to load events"
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _loading.value = false
                _error.value = "Error: ${t.message}"
            }
        })
    }
}
