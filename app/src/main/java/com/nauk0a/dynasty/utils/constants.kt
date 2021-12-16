package com.nauk0a.dynasty.utils

import android.annotation.SuppressLint
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.nauk0a.dynasty.MainActivity
import javax.inject.Inject

lateinit var APP_ACTIVITY: MainActivity

lateinit var db: FirebaseFirestore
