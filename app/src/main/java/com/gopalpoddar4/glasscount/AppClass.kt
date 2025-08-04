package com.gopalpoddar4.glasscount

import android.app.Application
import com.gopalpoddar4.glasscount.data.DailyDrinkDao
import com.gopalpoddar4.glasscount.data.RecentDrinkDao
import com.gopalpoddar4.glasscount.vmandrepo.MainRepo

class AppClass(): Application() {

    lateinit var database: DB
    lateinit var repository: MainRepo
    lateinit var dailyDrinkDao: DailyDrinkDao
    lateinit var recentDrinkDao: RecentDrinkDao

    override fun onCreate() {
        super.onCreate()

        database = DB.getInstance(applicationContext)

        dailyDrinkDao = database.dailyDrinkDao()
        recentDrinkDao = database.recentDrinkDao()

        repository = MainRepo(dailyDrinkDao,recentDrinkDao)
    }
}