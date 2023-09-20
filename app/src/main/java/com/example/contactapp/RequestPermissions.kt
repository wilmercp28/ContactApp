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

fun requestSmsPermission(smsPermissionLauncher: ActivityResultLauncher<String>) {
    smsPermissionLauncher.launch(Manifest.permission.SEND_SMS)
}

fun hasSmsPermission(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.SEND_SMS
    ) == PackageManager.PERMISSION_GRANTED
}

