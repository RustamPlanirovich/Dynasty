package com.nauk0a.dynasty.budget

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.DocumentSnapshot

class BudgetViewModel : ViewModel() {

    val dateToDetailBudget = MutableLiveData<DocumentSnapshot>()
    val dateToDetailBudgetEdit = MutableLiveData<DocumentSnapshot>()
}