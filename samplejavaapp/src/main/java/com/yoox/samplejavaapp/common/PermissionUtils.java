package com.yoox.samplejavaapp.common;

import android.app.Activity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class PermissionUtils {

    public static boolean askPermission(String permission, Activity activity, int requestCode) {
        if (ContextCompat.checkSelfPermission(activity, permission) == PERMISSION_GRANTED) {
            return true;
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);
            return false;
        }
    }
}
