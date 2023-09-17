package com.example.contactapp

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.tools.build.jetifier.core.utils.Log


@SuppressLint("SuspiciousIndentation")
@Composable
fun ContactListShow(
    contactsList: MutableList<Contact>,
    paddingValues: PaddingValues,
    openContact: MutableState<Boolean>,
    selectedContact: MutableState<Int>
) {
    val initialIconSize = 50.dp
    val fontSize = 20
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
        ) {
            items(contactsList.size) { contact ->
                val contactInfo = contactsList[contact]
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                        .clickable {
                            selectedContact.value = contact
                            openContact.value = true
                        },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(initialIconSize)
                            .background(
                                MaterialTheme.colorScheme.primary,
                                RoundedCornerShape(initialIconSize)
                            )
                            .aspectRatio(1f)
                            .clip(CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (contactInfo.photo.isNullOrEmpty()) {
                            Text(
                                text = contactInfo.name.removePrefix("Name ").first().toString(),
                                fontSize = fontSize.sp
                            )
                        } else {
                            val imageBytes = Base64.decode(contactInfo.photo, Base64.DEFAULT)
                            if (imageBytes != null) {
                                val bitmapPhoto: Bitmap? =
                                    BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                                if (bitmapPhoto != null) {
                                    Image(
                                        bitmap = bitmapPhoto.asImageBitmap(),
                                        contentDescription = "SelectedPhoto",
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.FillBounds,
                                        alignment = Alignment.Center
                                    )
                                } else {
                                    Text(
                                        text = contactInfo.name.removePrefix("Name ").first()
                                            .toString(),
                                        fontSize = fontSize.sp
                                    )
                                }
                            } else {
                                Text(
                                    text = contactInfo.name.removePrefix("Name ").first()
                                        .toString(),
                                    fontSize = fontSize.sp
                                )
                            }
                        }
                    }
                    Text(
                        text = "${contactInfo.name.removePrefix("Name ")} ${
                            contactInfo.lastName.removePrefix(
                                "Last Name "
                            )
                        }",
                        fontSize = fontSize.sp
                    )
                }

            }
        }
    }
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowContactDetails(
    contactsList: MutableList<Contact>,
    showingContact: Int,
    openContact: MutableState<Boolean>
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val iconFontSize = 100
        val fontSize = 20
        val contactInfo = contactsList[showingContact]
        val name = contactInfo.name.removePrefix("Name ").trim()
        val lastName = contactInfo.lastName.removePrefix("Last Name ").trim()
        val email = contactInfo.email.removePrefix("Email ").trim()
        val phoneNumber = contactInfo.phoneNumber.removePrefix("Phone Number ").trim()
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = "") },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                openContact.value = false
                            }
                        ) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }

                )
            }
        ) {
            Column(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(150.dp)
                        .background(
                            MaterialTheme.colorScheme.primary,
                            RoundedCornerShape(100.dp)
                        )
                        .aspectRatio(1f)
                        .clip(CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    if (contactInfo.photo != null) {
                        val imageBytes = Base64.decode(contactInfo.photo, Base64.DEFAULT)
                        if (imageBytes != null) {
                            val bitmapPhoto: Bitmap? = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                            if (bitmapPhoto != null) {
                                Image(
                                    bitmap = bitmapPhoto.asImageBitmap(),
                                    contentDescription = "SelectedPhoto",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.FillBounds,
                                    alignment = Alignment.Center
                                )
                            } else {
                                Text(
                                    text = contactInfo.name.removePrefix("Name ").first().toString().trim(),
                                    fontSize = iconFontSize.sp,
                                    textAlign = TextAlign.Center,

                                )
                            }
                        } else {
                            Text(
                                text = contactInfo.name.removePrefix("Name ").first().toString().trim(),
                                fontSize = iconFontSize.sp,
                                textAlign = TextAlign.Center,

                            )
                        }
                    } else {
                        Text(
                            text = contactInfo.name.removePrefix("Name ").first().toString().trim(),
                            fontSize = iconFontSize.sp,
                            textAlign = TextAlign.Center,

                        )
                    }
                }
                Spacer(modifier = Modifier.size(20.dp))
                Text(text ="$name $lastName",fontSize = (fontSize + 10).sp)
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(30.dp)
                        .background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(20))
                ) {
                    Text(
                        text = "Contact Details",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        textAlign = TextAlign.Center,
                        fontSize = fontSize.sp
                    )
                    if (email.isNotEmpty()) {
                        Row(
                            modifier = Modifier.padding(20.dp)
                        ) {
                            Icon(Icons.Filled.Email, "")
                            Text(
                                text = email,
                                fontSize = fontSize.sp
                            )
                        }
                    }
                    if (phoneNumber.isNotEmpty()) {
                        Row(
                            modifier = Modifier.padding(20.dp)
                        ) {
                            Icon(Icons.Filled.Phone, "")
                            Text(
                                text = contactInfo.phoneNumber.removePrefix("Phone Number ").trim(),
                                fontSize = fontSize.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
