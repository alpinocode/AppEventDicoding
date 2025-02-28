package com.example.appeventdicoding.ui.viewModelFactory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.appeventdicoding.data.EventRepository
import com.example.appeventdicoding.di.Injection
import com.example.appeventdicoding.ui.detail.DetailViewModel
import com.example.appeventdicoding.ui.favorite.FavoriteViewModel

class ViewModelFactory private constructor(private val eventRepository: EventRepository): ViewModelProvider.NewInstanceFactory(){
    @Suppress("UNCHECKEE_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(eventRepository) as T
        } else if(modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            return FavoriteViewModel(eventRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModelClass" + modelClass.name)
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null
        fun getInstance(context:Context): ViewModelFactory =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: ViewModelFactory(Injection.provideRepository(context))
            }.also { INSTANCE = it }
    }
}