package com.example.contactapp

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import java.io.IOException


data class Contact(

    val id: Int,
    val name: String,
    val lastName: String,
    val phoneNumber: String,
    val email: String,
)

fun addContact(
    contactsList: MutableList<Contact>,
    name: String,
    lastName: String,
    phoneNumber: String,
    email: String
) {
    val newContactId = contactsList.size + 1
    val newContact = Contact(
        id = newContactId,
        name = name,
        lastName = lastName,
        phoneNumber = phoneNumber,
        email = email
    )
    contactsList.add(newContact)
}
@Composable
fun ContactPhoto(
    size: Dp,
    contactInfo: Contact,

){
    val galleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) { uriList ->

        }
    Button(
        modifier = Modifier
            .size(size)
            .background(
                MaterialTheme.colorScheme.primary,
                RoundedCornerShape(size)
            ),
        onClick = {galleryLauncher.launch("image/*")}
    ) {
        Text(
            text = contactInfo.name.removePrefix("Name ").first().toString(),
            fontSize = 30.sp
        )
    }
}


@Composable
fun showContact(
){
    Row(

    ) {

    }

}
