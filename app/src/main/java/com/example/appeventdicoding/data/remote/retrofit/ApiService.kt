package com.example.appeventdicoding.data.remote.retrofit

import com.example.appeventdicoding.data.remote.response.EventDetailDicodingResponse
import com.example.appeventdicoding.data.remote.response.EventDicodingResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("events")
    fun getEvent(
        @Query("active") active: Int
    ): Call<EventDicodingResponse>
    @GET("events/{id}")
    fun getEventDetail(
        @Path("id") id: Int
    ): Call<EventDetailDicodingResponse>
    @GET("events")
    fun getReminder(
        @Query("active") active: Int,
        @Query("limit") limit: Int
    ): Call<EventDicodingResponse>
}