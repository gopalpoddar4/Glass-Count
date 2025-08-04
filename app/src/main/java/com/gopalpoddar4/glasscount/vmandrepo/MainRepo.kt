package com.gopalpoddar4.glasscount.vmandrepo

import androidx.lifecycle.LiveData
import com.gopalpoddar4.glasscount.data.DailyDrinkDao
import com.gopalpoddar4.glasscount.data.DailyDrinkModel
import com.gopalpoddar4.glasscount.data.RecentDrinkDao
import com.gopalpoddar4.glasscount.data.RecentDrinkModel

class MainRepo(private val dailyDrinkDao: DailyDrinkDao, private val recentDrinkDao: RecentDrinkDao) {

    val showRecentDrinkData: LiveData<List<RecentDrinkModel>> = recentDrinkDao.getDailyDrinkData()
    val dailyDrinkData: LiveData<List<DailyDrinkModel>> = dailyDrinkDao.getDailyDrinkData()

    suspend fun insertRecentDrinkData(recentDrinkModel: RecentDrinkModel){
        recentDrinkDao.dailyLogInsert(recentDrinkModel)
    }

    suspend fun deleteAllRecentDrinkData(){
        recentDrinkDao.deleteAllDailyDrink()
    }

    suspend fun insertDailyDrinkData(dailyDrinkModel: DailyDrinkModel){
        dailyDrinkDao.dailyDrinkInsert(dailyDrinkModel)
    }
}