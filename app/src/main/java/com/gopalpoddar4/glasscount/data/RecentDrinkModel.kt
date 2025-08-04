package com.gopalpoddar4.glasscount.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recent_drink")
data class RecentDrinkModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int=0,
    val time: String
)
