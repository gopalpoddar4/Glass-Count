package com.gopalpoddar4.glasscount.vmandrepo

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gopalpoddar4.glasscount.data.DailyDrinkModel
import com.gopalpoddar4.glasscount.data.RecentDrinkModel
import kotlinx.coroutines.launch

class MainViewModel(private val repository: MainRepo): ViewModel() {

    fun insertRecentData(recentDrinkModel: RecentDrinkModel){
        viewModelScope.launch {
            repository.insertRecentDrinkData(recentDrinkModel)
        }
    }

    fun deleteRecentData(){
        viewModelScope.launch {
            repository.deleteAllRecentDrinkData()
        }
    }

    fun insertDailyData(dailyDrinkModel: DailyDrinkModel){
        viewModelScope.launch {
            repository.insertDailyDrinkData(dailyDrinkModel)
        }
    }

    fun getRecentData(): LiveData<List<RecentDrinkModel>> =  repository.showRecentDrinkData

    fun dailyDrinkData(): LiveData<List<DailyDrinkModel>> = repository.dailyDrinkData

}