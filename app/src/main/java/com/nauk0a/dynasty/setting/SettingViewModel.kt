package com.nauk0a.dynasty.setting

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.prefs.Preferences

class SettingViewModel : ViewModel() {

    val darkThemeEnabled = MutableLiveData<Boolean>()


}