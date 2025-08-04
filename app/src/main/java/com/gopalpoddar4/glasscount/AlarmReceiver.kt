package com.gopalpoddar4.glasscount

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import com.gopalpoddar4.glasscount.data.DailyDrinkModel
import com.gopalpoddar4.glasscount.utils.GlassPref
import com.gopalpoddar4.glasscount.utils.ResetGlassCount
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class AlarmReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, p1: Intent) {

        val repository = (context.applicationContext as AppClass).repository

        val today = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("dd MMM")
        val formateToday = today.format(formatter)

        val count = GlassPref.getCount(context)
        val goal = GlassPref.getGoal(context)

        if (goal==count){
            GlassPref.addStreak(context)
            CoroutineScope(Dispatchers.IO).launch {
                repository.insertDailyDrinkData(DailyDrinkModel(date = formateToday, dailyDrink = count, goalAchieve = true))
            }
        }else{
            GlassPref.resetStreak(context)
            CoroutineScope(Dispatchers.IO).launch {
                repository.insertDailyDrinkData(DailyDrinkModel(date = formateToday, dailyDrink = count, goalAchieve = false))
            }

        }


        ResetGlassCount.resetData(context)
    }

}