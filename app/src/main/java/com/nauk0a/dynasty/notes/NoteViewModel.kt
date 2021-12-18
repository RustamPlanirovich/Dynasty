package com.nauk0a.dynasty.notes

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.DocumentSnapshot

class NoteViewModel : ViewModel() {


    val dateToDetail = MutableLiveData<DocumentSnapshot>()
}