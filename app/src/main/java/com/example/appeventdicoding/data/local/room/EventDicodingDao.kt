package com.example.appeventdicoding.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.appeventdicoding.data.local.entity.EventDicodingEntity

@Dao
interface EventDicodingDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
     fun insertFavorite(favorite: EventDicodingEntity)

    @Query("SELECT * FROM favorite_event WHERE is_favorite = 1")
    fun getFavorite(): LiveData<EventDicodingEntity>

    @Query("DELETE FROM favorite_event where id= :id")
     fun deleteAll(id: Int)

    @Query("SELECT EXISTS(SELECT * FROM favorite_event WHERE id= :id AND is_favorite=1 )")
    fun isFavoriteEventAdd(id:Int): Boolean
}