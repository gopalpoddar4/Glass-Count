package com.gopalpoddar4.glasscount.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DailyDrinkDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun dailyDrinkInsert(dailyDrinkModel: DailyDrinkModel)

    @Query("SELECT * FROM daily_drink ORDER BY id ASC")
    fun getDailyDrinkData(): LiveData<List<DailyDrinkModel>>
}