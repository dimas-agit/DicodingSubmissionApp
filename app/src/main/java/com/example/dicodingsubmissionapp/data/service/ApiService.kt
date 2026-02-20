package com.example.dicodingsubmissionapp.data.service

import com.example.dicodingsubmissionapp.data.response.EventDetailResponse
import com.example.dicodingsubmissionapp.data.response.EventResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("events")
    fun getEvents(@Query("active") active: Int): Call<EventResponse>

    @GET("events/{id}")
    fun getEventDetail(@Path("id") id: Int) : Call<EventDetailResponse>

    @GET("events")
    fun getSearchEvent(@Query("active") active: Int, @Query("q") q: String): Call<EventResponse>

}