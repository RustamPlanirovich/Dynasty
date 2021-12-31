package com.nauk0a.dynasty.utils

import android.content.ClipData
import android.net.Uri
import android.view.View
import androidx.core.util.component1
import androidx.core.util.component2

import androidx.core.view.ContentInfoCompat
import androidx.core.view.OnReceiveContentListener


class MyReceiver : OnReceiveContentListener {
    override fun onReceiveContent(view: View, payload: ContentInfoCompat): ContentInfoCompat? {
        val (uriContent, remaining) = payload.partition { item: ClipData.Item -> item.uri != null }
        if (uriContent != null) {
            val clip: ClipData = uriContent.getClip()
            for (i in 0 until clip.itemCount) {
                val uri: Uri = clip.getItemAt(i).uri
                ToastFun(uri.toString())

            }
        }
        // Return anything that your app didn't handle. This preserves the default platform
        // behavior for text and anything else that you aren't implementing custom handling for.
        return remaining
    }

    companion object {
        val MIME_TYPES = arrayOf("image/*", "video/*")
    }

}