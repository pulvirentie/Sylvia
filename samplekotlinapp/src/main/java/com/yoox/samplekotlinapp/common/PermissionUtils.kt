package com.yoox.samplekotlinapp.common

import android.app.Activity
import android.content.pm.PackageManager.PERMISSION_GRANTED
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


fun Activity.askPermission(permission: String, requestCode: Int): Boolean {
    return if (ContextCompat.checkSelfPermission(this, permission) == PERMISSION_GRANTED) {
        true
    } else {
        ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
        false
    }
}

