package com.example.appeventdicoding.ui.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactorySetting(private val pref:SettingPrefenrece) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CASET")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SettingViewModel::class.java)) {
            return SettingViewModel(pref) as T
        }
        throw IllegalArgumentException("Terjadi masalah pada ViewModelClass" + modelClass.name)
    }
}