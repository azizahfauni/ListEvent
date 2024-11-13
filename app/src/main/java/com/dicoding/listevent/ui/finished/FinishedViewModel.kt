package com.dicoding.listevent.ui.finished

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.listevent.ApiConfig
import com.dicoding.listevent.EventResponse
import com.dicoding.listevent.ListEventsItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FinishedViewModel : ViewModel() {

    private val _events = MutableLiveData<List<ListEventsItem>>()
    val events: LiveData<List<ListEventsItem>> get() = _events

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    // Fungsi untuk memuat event yang telah selesai
    fun loadFinishedEvents() {
        _loading.value = true
        val client = ApiConfig.getApiService().getFinish()
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
