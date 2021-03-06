package com.nauk0a.dynasty.mainActivityPackage

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
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

class MainViewModel(networkStatusTracker: NetworkStatusTracker) : ViewModel() {


    //Создаем LiveData в которую приходят изменения о состоянии сети
    @ExperimentalCoroutinesApi
    val state =
        networkStatusTracker.networkStatus
            .map(
                onUnavailable = { MyState.Error },
                onAvailable = { MyState.Fetched },
            )
            .asLiveData(Dispatchers.IO)




}