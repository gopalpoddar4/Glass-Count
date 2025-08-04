package com.gopalpoddar4.glasscount.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RecentDrinkDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun dailyLogInsert(recentDrinkModel: RecentDrinkModel)

    @Query("DELETE FROM recent_drink")
    suspend fun deleteAllDailyDrink()

    @Query("SELECT * FROM recent_drink ORDER BY id DESC")
    fun getDailyDrinkData(): LiveData<List<RecentDrinkModel>>
}