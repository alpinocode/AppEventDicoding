package com.example.appeventdicoding.ui.home

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

class HomeViewModel : ViewModel() {
    private val _listEventsFinished = MutableLiveData<List<ListEventsItem>>()
    val listEventsFinished: LiveData<List<ListEventsItem>> = _listEventsFinished

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        getEventFinished()
    }
    private fun getEventFinished() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getEvent(EVENT_FINISHED)
        client.enqueue(object : Callback<EventDicodingResponse> {
            override fun onResponse(
                call: Call<EventDicodingResponse>,
                response: Response<EventDicodingResponse>
            ) {
                _isLoading.value = false
                if(response.isSuccessful) {
                    val result = response.body()
                    Log.d(TAG, "Cek Body nya : ${result}")
                    _listEventsFinished.value = response.body()?.listEvents as List<ListEventsItem>?
                } else {
                    Log.e(TAG, "OnFailure Response: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<EventDicodingResponse>, t: Throwable) {
                Log.e(TAG, "OnFailure : ${t.message}")
            }


        })
    }

    companion object{
        private const val TAG = "home_view_model"
        private const val EVENT_FINISHED = 0
    }
}