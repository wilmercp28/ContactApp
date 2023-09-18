package com.example.contactapp

import android.Manifest
import androidx.activity.result.ActivityResultLauncher


fun requestPhoneCallPermission(phoneCallPermissionLauncher: ActivityResultLauncher<String>) {
    phoneCallPermissionLauncher.launch(Manifest.permission.CALL_PHONE)
}


