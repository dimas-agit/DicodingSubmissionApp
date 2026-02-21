package com.example.dicodingsubmissionapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dicodingsubmissionapp.data.response.EventResponse
import com.example.dicodingsubmissionapp.data.response.ListEventsItem
import com.example.dicodingsubmissionapp.data.service.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {
    private val _listActiveEvent = MutableLiveData<List<ListEventsItem?>?>()
    val listActiveEvent: LiveData<List<ListEventsItem?>?> = _listActiveEvent

    private val _listFinishedEvent = MutableLiveData<List<ListEventsItem?>?>()
    val listFinishedEvent: LiveData<List<ListEventsItem?>?> = _listFinishedEvent

    private val _searchResult = MutableLiveData<List<ListEventsItem?>?>()
    val searchResult: LiveData<List<ListEventsItem?>?> = _searchResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    init {
        getActiveEvents()
        getFinishedEvents()
    }

    fun getActiveEvents() {
        _isLoading.value = true
        _errorMessage.value = null
        val client = ApiConfig.getApiService().getEvents(1)
        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listActiveEvent.value = response.body()?.listEvents
                } else {
                    _errorMessage.value = "Error: ${response.message()}"
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = t.message ?: "Failed to load events"
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun getFinishedEvents() {
        _isLoading.value = true
        _errorMessage.value = null
        val client = ApiConfig.getApiService().getEvents(0)
        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listFinishedEvent.value = response.body()?.listEvents
                } else {
                    _errorMessage.value = "Error: ${response.message()}"
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = t.message ?: "Failed to load events"
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun searchEvents(query: String) {
        _isLoading.value = true
        _errorMessage.value = null
        val client = ApiConfig.getApiService().getSearchEvent(-1, query)
        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val results = response.body()?.listEvents
                    _searchResult.value = results
                    if (results.isNullOrEmpty()) {
                        _errorMessage.value = "No events found for \"$query\""
                    }
                } else {
                    _errorMessage.value = "Error: ${response.message()}"
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = t.message ?: "Search failed"
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}