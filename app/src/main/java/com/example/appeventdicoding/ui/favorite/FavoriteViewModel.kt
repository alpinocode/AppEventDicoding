package com.example.appeventdicoding.ui.favorite

import androidx.lifecycle.ViewModel
import com.example.appeventdicoding.data.EventRepository

class FavoriteViewModel(private val eventRepository: EventRepository) : ViewModel() {
    fun getFavoriteEvent() = eventRepository.getIsFavoriteEvent()
}