package com.nauk0a.dynasty.notes

import android.os.Parcelable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.DocumentSnapshot










class NoteViewModel : ViewModel() {


    val dateToDetail = MutableLiveData<DocumentSnapshot>()




    private lateinit var state: Parcelable
    fun saveRecyclerViewState(parcelable: Parcelable) { state = parcelable }
    fun restoreRecyclerViewState() : Parcelable = state
    fun stateInitialized() : Boolean = ::state.isInitialized



}