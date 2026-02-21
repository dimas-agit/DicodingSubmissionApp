package com.example.dicodingsubmissionapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dicodingsubmissionapp.data.response.Event
import com.example.dicodingsubmissionapp.data.response.EventDetailResponse
import com.example.dicodingsubmissionapp.data.service.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel : ViewModel() {
    private val _event = MutableLiveData<Event?>()
    val event: LiveData<Event?> = _event

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getEventDetail(id: Int) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getEventDetail(id)
        client.enqueue(object : Callback<EventDetailResponse> {
            override fun onResponse(
                call: Call<EventDetailResponse>,
                response: Response<EventDetailResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _event.value = response.body()?.event
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<EventDetailResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    companion object {
        private const val TAG = "DetailViewModel"
    }
}