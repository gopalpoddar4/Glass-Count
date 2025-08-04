package com.gopalpoddar4.glasscount

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.impl.Migration_1_2
import com.gopalpoddar4.glasscount.data.DailyDrinkDao
import com.gopalpoddar4.glasscount.data.DailyDrinkModel
import com.gopalpoddar4.glasscount.data.RecentDrinkDao
import com.gopalpoddar4.glasscount.data.RecentDrinkModel

@Database(entities = [DailyDrinkModel::class, RecentDrinkModel::class], version = 2)
abstract class DB: RoomDatabase() {

    abstract fun dailyDrinkDao(): DailyDrinkDao
    abstract fun recentDrinkDao(): RecentDrinkDao

    companion object{
        @Volatile private var INSTANCE: DB?=null

        fun getInstance(context: Context): DB{
            return INSTANCE?:synchronized (this){
                Room.databaseBuilder(
                    context.applicationContext,
                    DB::class.java,
                    "DATABASE"
                ).addMigrations(Migration_1_2)
                    .build()
            }
        }

        val Migration_1_2 = object : Migration(1,2){
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE daily_drink ADD COLUMN date TEXT DEFAULT '' NOT NULL")
            }

        }
    }
}