package com.example.contactapp

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp


data class Contact(

    val id: Int,
    val photo: MutableState<Bitmap?>,
    val name: String,
    val lastName: String,
    val phoneNumber: String,
    val email: String,
)

fun addContact(
    contactsList: MutableList<Contact>,
    photo: MutableState<Bitmap?>,
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
        email = email,
        photo = photo
    )
    contactsList.add(newContact)
}
@Composable
fun ContactPhoto(
    size: Dp,
    contactInfo: Contact,
    photo: Bitmap?
){
    Box(
        modifier = Modifier
            .size(size)
            .background(
                MaterialTheme.colorScheme.primary,
                RoundedCornerShape(size)
            )
    ) {
        if (photo == null) {
            Text(
                text = contactInfo.name.removePrefix("Name ").first().toString(),
                fontSize = 30.sp
            )
        } else {
            Image(
                bitmap = photo.asImageBitmap(),
                contentDescription = "SelectedPhoto",
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.FillBounds,
                alignment = Alignment.Center

            )
        }
    }
}
