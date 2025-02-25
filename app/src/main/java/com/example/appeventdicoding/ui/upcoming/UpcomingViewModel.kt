package com.example.appeventdicoding.ui.upcoming

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.appeventdicoding.data.remote.response.EventDicodingResponse
import com.example.appeventdicoding.data.remote.response.ListEventsItem
import com.example.appeventdicoding.data.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpcomingViewModel : ViewModel() {
    private val _listEventUpcomingDicoding = MutableLiveData<List<ListEventsItem>>()
    val listEventUpcomingDicoding: LiveData<List<ListEventsItem>> = _listEventUpcomingDicoding

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        getEventUpcoming()
    }

    private fun getEventUpcoming() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getEvent(EVENT_UPCOMING)
        client.enqueue(object : Callback<EventDicodingResponse> {
            override fun onResponse(
                call: Call<EventDicodingResponse>,
                response: Response<EventDicodingResponse>
            ) {
                _isLoading.value = false
                if(response.isSuccessful) {
                    _listEventUpcomingDicoding.value = response.body()?.listEvents as List<ListEventsItem>?
                } else {
                    Log.e(TAG, "OnFailure Response :${response.message()}")
                }
            }

            override fun onFailure(call: Call<EventDicodingResponse>, t: Throwable) {
                Log.e(TAG, "Terjadi Pada Pemanggilan Api : ${t.message}")
            }

        })
    }

    companion object {
        private const val EVENT_UPCOMING = 1
        private const val TAG = "upcoming_view_model"
    }
}