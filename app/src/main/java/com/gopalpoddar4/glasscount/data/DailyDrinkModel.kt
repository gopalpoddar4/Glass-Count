package com.gopalpoddar4.glasscount.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_drink")
data class DailyDrinkModel(

    @PrimaryKey(autoGenerate = true)
    val id: Int=0,
    val date: String,
    val dailyDrink:Int,
    val goalAchieve: Boolean
)
