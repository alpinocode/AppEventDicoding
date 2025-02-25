package com.example.appeventdicoding.di

import android.content.Context
import com.example.appeventdicoding.data.EventRepository
import com.example.appeventdicoding.data.local.room.EventDicodingDatabase
import com.example.appeventdicoding.utils.AppExecutor

object Injection {
    fun provideRepository(context:Context): EventRepository {
        val database = EventDicodingDatabase.getInstance(context)
        val dao = database.eventDao()
        val appExecutor = AppExecutor()

        return EventRepository.getInstance(dao, appExecutor)
    }
}