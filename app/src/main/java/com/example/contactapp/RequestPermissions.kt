package com.example.contactapp

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat


fun requestPhoneCallPermission(phoneCallPermissionLauncher: ActivityResultLauncher<String>) {
    phoneCallPermissionLauncher.launch(Manifest.permission.CALL_PHONE)
}
fun hasPhoneCallPermission(
    context: Context
): Boolean {
    return ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.CALL_PHONE
    ) == PackageManager.PERMISSION_GRANTED
}

