package com.nauk0a.dynasty.notes

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NoteViewModel : ViewModel() {


    val dateToDetail = MutableLiveData<Notes>()
}