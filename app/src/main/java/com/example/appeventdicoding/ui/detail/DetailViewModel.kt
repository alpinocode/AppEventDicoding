package com.example.appeventdicoding.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.appeventdicoding.data.EventRepository
import com.example.appeventdicoding.data.local.entity.EventDicodingEntity
import com.example.appeventdicoding.data.remote.response.EventDetailDicodingResponse
import com.example.appeventdicoding.data.remote.response.ListEventsItem
import com.example.appeventdicoding.data.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(private val eventRepository: EventRepository ) :ViewModel() {
    private val _eventDetail = MutableLiveData<EventDetailDicodingResponse>()
    val eventDetail:LiveData<EventDetailDicodingResponse> = _eventDetail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading:LiveData<Boolean> = _isLoading

    fun getDetailEvent(eventId:Int) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getEventDetail(eventId)
        client.enqueue(object : Callback<EventDetailDicodingResponse>{
            override fun onResponse(
                call: Call<EventDetailDicodingResponse>,
                response: Response<EventDetailDicodingResponse>
            ) {
                _isLoading.value = false
                if(response.isSuccessful) {
                    val response = response.body()
                    Log.e(TAG, "Cek Data Response : ${response?.event}")
                    _eventDetail.value = response as EventDetailDicodingResponse
                } else {
                    Log.e(TAG, "OnFailure Response : ${response.message()}")
                }
            }

            override fun onFailure(call: Call<EventDetailDicodingResponse>, t: Throwable) {
                Log.e(TAG, "OnFailure : ${t.message}")
            }
        })
    }

    fun insertFavorite(eventIsFavorite: EventDicodingEntity) = eventRepository.insertFavorite(eventIsFavorite)

    fun deteleteFavorite(eventId: Int) = eventRepository.deleteIsFavorite(eventId)
    companion object {
        private const val TAG = "detail_view_model"
    }
}