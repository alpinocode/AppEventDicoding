package com.example.appeventdicoding.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.appeventdicoding.data.local.entity.EventDicodingEntity

@Database(entities = [EventDicodingEntity::class], version = 1, exportSchema = false)
abstract class EventDicodingDatabase : RoomDatabase(){
    abstract fun eventDao(): EventDicodingDao

    companion object{
        @Volatile
        private var INSTANCE: EventDicodingDatabase? = null

        fun getInstance(context:Context): EventDicodingDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    EventDicodingDatabase::class.java,
                    "Event.db"
                ).build()
            }
    }
}