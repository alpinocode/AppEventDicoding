package com.example.appeventdicoding.ui.setting

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.appeventdicoding.data.remote.response.EventDicodingResponse
import com.example.appeventdicoding.data.remote.response.ListEventsItem
import com.example.appeventdicoding.data.remote.retrofit.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SettingViewModel(private val pref:SettingPrefenrece) : ViewModel() {
    private val _reminderNotification = MutableLiveData<List<ListEventsItem>>()
    val reminderNotification:LiveData<List<ListEventsItem>> = _reminderNotification




    fun getThemeSettings():LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            pref.saveThemeSetting(isDarkModeActive)
        }
    }

}