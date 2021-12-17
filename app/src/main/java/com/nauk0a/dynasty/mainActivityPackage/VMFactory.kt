package com.nauk0a.dynasty.mainActivityPackage

import androidx.annotation.NonNull
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nauk0a.dynasty.internet_connection.NetworkStatusTracker

class VMFactory(networkStatusTracker: NetworkStatusTracker) :
    ViewModelProvider.NewInstanceFactory() {

    val _networkStatusTracker: NetworkStatusTracker = networkStatusTracker

    @NonNull
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(_networkStatusTracker) as T
    }
}