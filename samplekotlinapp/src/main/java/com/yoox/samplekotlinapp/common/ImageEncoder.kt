package com.yoox.samplekotlinapp.common

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import java.io.ByteArrayOutputStream

object ImageEncoder {

    fun encode(uri: Uri, contentResolver: ContentResolver): String? = kotlin.runCatching {
        contentResolver.openInputStream(uri)
    }.map { inputStream ->
        inputStream.use {
            val bitmap = BitmapFactory.decodeStream(it)
            encodeImage(bitmap)
        }
    }.getOrNull()

    private fun encodeImage(bitmap: Bitmap): String {
        ByteArrayOutputStream().use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 60, it)
            val b = it.toByteArray()
            return Base64.encodeToString(b, Base64.DEFAULT)
        }
    }
}
