package com.nauk0a.dynasty.utils

import android.text.TextUtils
import android.view.View
import android.widget.EditText

inline fun EditText.onFocusChange(
    vieew: EditText,
    text: String,
    crossinline hasFocus: (Boolean) -> Unit
) {
    setOnFocusChangeListener(View.OnFocusChangeListener { view, b ->
        hasFocus(b)
        if (hasFocus()) {
            vieew.text.clear()
        } else {
            if (TextUtils.isEmpty(vieew.text.trim())){
                vieew.setText(text)
            }else{

            }
        }
    })



}