package com.example.appeventdicoding.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_event")
data class EventDicodingEntity(
    @ColumnInfo("id")
    @PrimaryKey
    val id: Int,

    @ColumnInfo("published_at")
    val publishedAt:String,

    @ColumnInfo("nama_event")
    val nameEvent:String,

    @ColumnInfo("image")
    val image:String? = null,

    @ColumnInfo("is_favorite")
    var isFavorite:Boolean
)