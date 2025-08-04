package com.gopalpoddar4.glasscount

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import androidx.lifecycle.ViewModelProvider
import com.gopalpoddar4.glasscount.utils.GlassPref
import com.gopalpoddar4.glasscount.utils.WidgetHelper
import com.gopalpoddar4.glasscount.vmandrepo.MainViewModel
import com.gopalpoddar4.glasscount.vmandrepo.VMFactory

class NewAppWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    override fun onReceive(context: Context, intent: Intent?) {
        super.onReceive(context, intent)
        if (intent?.action == "ADD_GLASS") {

            val count = GlassPref.getCount(context)
            val goal = GlassPref.getGoal(context)

            if (goal==count){

            }else{
                GlassPref.addGlass(context)
                val data = WidgetHelper.recentTime()
                WidgetHelper.insertData(context,data)
            }

            val appWidgetManager = AppWidgetManager.getInstance(context)
            val ids = appWidgetManager.getAppWidgetIds(ComponentName(context, NewAppWidget::class.java))
            for (id in ids) {
                updateAppWidget(context, appWidgetManager, id)
            }
        }
    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    val views = RemoteViews(context.packageName, R.layout.new_app_widget)
    val count = GlassPref.getCount(context)
    val goal = GlassPref.getGoal(context)
    val persent = ((count.toFloat() / goal) * 100).toInt()

    views.setTextViewText(R.id.widgetPersentProgress, "$persent%")
   // views.setTextViewText(R.id.widgetRemainingGlass, "${goal - count} Glass more")


    val intent = Intent(context, NewAppWidget::class.java)
    intent.action = "ADD_GLASS"
    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)

    val pendingIntent = PendingIntent.getBroadcast(
        context,
        appWidgetId,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
    views.setOnClickPendingIntent(R.id.widgetParentLayout, pendingIntent)

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}