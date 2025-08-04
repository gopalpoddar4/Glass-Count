package com.gopalpoddar4.glasscount.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.gopalpoddar4.glasscount.AppClass
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object ResetGlassCount {

    fun resetData(context: Context){
        val repository = (context.applicationContext as AppClass).repository
        val pref = context.getSharedPreferences("glasscount",MODE_PRIVATE)

        //delete daily water intake log
        CoroutineScope(Dispatchers.IO).launch {
            repository.deleteAllRecentDrinkData()
        }

        //reset daily water intake
        pref.edit().putInt("dailyWater",0).apply()

    }
}