package com.gopalpoddar4.glasscount.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object GlassPref {
    fun getCount(context: Context): Int{
        val pref = context.getSharedPreferences("glasscount",MODE_PRIVATE)
        val lastSync = pref.getLong("last_date",0L)

        val lastDate = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date(lastSync))
        val currentDate = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())

        if (lastDate!= currentDate){
            return 0
        }
        return pref.getInt("dailyWater",0)
    }

    fun getGoal(context: Context): Int{
        val pref = context.getSharedPreferences("glasscount",MODE_PRIVATE)
        return pref.getInt("goal",12)

    }

    fun addGlass(context: Context){
        val pref = context.getSharedPreferences("glasscount",MODE_PRIVATE)
        val count = getCount(context) + 1
        pref.edit().putInt("dailyWater",count).apply()

    }

    fun getStreak(context: Context): Int{
        val pref = context.getSharedPreferences("glasscount",MODE_PRIVATE)
        return pref.getInt("streak",0)
    }

    fun addStreak(context: Context){
        val pref = context.getSharedPreferences("glasscount",MODE_PRIVATE)
        pref.edit().putInt("streak",getStreak(context)+1).apply()
    }
    fun resetStreak(context: Context){
        val pref = context.getSharedPreferences("glasscount",MODE_PRIVATE)
        pref.edit().putInt("streak",0).apply()

    }
}