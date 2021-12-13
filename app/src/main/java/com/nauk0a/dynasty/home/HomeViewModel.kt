package com.nauk0a.dynasty.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.nauk0a.dynasty.internet_connection.NetworkStatusTracker
import com.nauk0a.dynasty.internet_connection.map
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi

sealed class MyState {
    object Fetched : MyState()
    object Error : MyState()
}

class HomeViewModel(networkStatusTracker: NetworkStatusTracker) : ViewModel() {


    @ExperimentalCoroutinesApi
    val state =
        networkStatusTracker.networkStatus
            .map(
                onUnavailable = { MyState.Error },
                onAvailable = { MyState.Fetched },
            )
            .asLiveData(Dispatchers.IO)

}