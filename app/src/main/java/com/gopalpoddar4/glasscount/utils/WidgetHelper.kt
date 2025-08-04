package com.gopalpoddar4.glasscount.utils

import android.content.Context
import com.gopalpoddar4.glasscount.AppClass
import com.gopalpoddar4.glasscount.data.RecentDrinkModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object WidgetHelper {

    fun insertData(context: Context,recentDrinkModel: RecentDrinkModel){
        val repository = (context.applicationContext as AppClass).repository

      CoroutineScope(Dispatchers.IO).launch {
          repository.insertRecentDrinkData(recentDrinkModel)
      }
    }

    fun recentTime(): RecentDrinkModel{
        val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val recentDrinkModel = sdf.format(Date())
        return RecentDrinkModel(time = recentDrinkModel)
    }
}