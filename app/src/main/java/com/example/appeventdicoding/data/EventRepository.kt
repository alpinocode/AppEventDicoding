package com.example.appeventdicoding.data

import androidx.lifecycle.LiveData
import com.example.appeventdicoding.data.local.entity.EventDicodingEntity
import com.example.appeventdicoding.data.local.room.EventDicodingDao
import com.example.appeventdicoding.utils.AppExecutor

class EventRepository private constructor(
    private val eventDicodingDao: EventDicodingDao,
    private val appExecutor: AppExecutor
){
    fun insertFavorite(eventIsFavorite: EventDicodingEntity) {
        appExecutor.diskIO.execute {
            eventDicodingDao.insertFavorite(eventIsFavorite)
        }
    }

    fun getIsFavoriteEvent(): LiveData<EventDicodingEntity> {
        return eventDicodingDao.getFavorite()
    }

    fun deleteIsFavorite(eventId: Int) {
        appExecutor.diskIO.execute {
            eventDicodingDao.deleteAll(eventId)
        }
    }



    companion object {
        private const val TAG = "event_repository"

        @Volatile
        private var INSTANCE:EventRepository? = null
        fun getInstance(
            eventDicodingDao: EventDicodingDao,
            appExecutor: AppExecutor
        ): EventRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: EventRepository(eventDicodingDao,appExecutor)
            }.also { INSTANCE = it }
    }
}