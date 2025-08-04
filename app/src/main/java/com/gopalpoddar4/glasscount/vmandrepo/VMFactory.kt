package com.gopalpoddar4.glasscount.vmandrepo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class VMFactory(private val repository: MainRepo): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
       if (modelClass.isAssignableFrom(MainViewModel::class.java)){
           return MainViewModel(repository) as T
       }
        throw IllegalArgumentException("Unknown Viewmodel class")
    }
}