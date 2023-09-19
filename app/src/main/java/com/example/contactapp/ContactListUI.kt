package com.example.contactapp

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import androidx.activity.result.ActivityResultLauncher
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UI(
    selectedScreen: MutableState<String>,
    contactsList: MutableList<Contact>,
    dataStore: DataStore<Preferences>,
    phoneCallPermissionLauncher: ActivityResultLauncher<String>
) {
    val editingMode = remember { mutableStateOf(false) }
    val searchQuery = remember { mutableStateOf("") }
    val selectedContact = remember { mutableStateOf(0) }
    val openContact = remember { mutableStateOf(false) }
    if (openContact.value) {
        ShowContactDetails(
            contactsList,
            selectedContact.value,
            openContact,
            dataStore,
            phoneCallPermissionLauncher,
            editingMode
        )
    } else {
        Surface(
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier.fillMaxSize()
        ) {
            val hideKeyboard = remember { mutableStateOf(false) }
            Column(
                modifier = Modifier
                    .clickable { hideKeyboard.value = true }
            ) {
                Scaffold(
                    topBar = {
                        Spacer(modifier = Modifier.height(20.dp))
                        SearchBar(
                            "Search Contact",
                            searchQuery,
                            hideKeyboard.value,
                            { hideKeyboard.value = false })
                    },
                    floatingActionButton = {
                        FloatingActionButton(onClick = {
                            selectedScreen.value = "AddContact"
                        }
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "AddContact")
                        }
                    }
                ) {
                    ContactListShow(contactsList, it, openContact, selectedContact, searchQuery)
                }
            }
        }
    }
}

@SuppressLint("SuspiciousIndentation")
@Composable
fun ContactListShow(
    contactsList: MutableList<Contact>,
    paddingValues: PaddingValues,
    openContact: MutableState<Boolean>,
    selectedContact: MutableState<Int>,
    searchQuery: MutableState<String>
) {
    val initialIconSize = 50.dp
    val fontSize = 20
    val filteredContacts = contactsList.filter { contact ->
        contact.name.contains(searchQuery.value, ignoreCase = true) ||
                contact.lastName.contains(searchQuery.value, ignoreCase = true) ||
                contact.phoneNumber.contains(searchQuery.value, ignoreCase = true) ||
                contact.email.contains(searchQuery.value, ignoreCase = true)
    }
    LazyColumn(
        modifier = Modifier
            .padding(paddingValues),

        ) {
        items(filteredContacts.size) { contact ->
            val contactInfo = filteredContacts[contact]
            val highlightedName = highlightText(filteredContacts[contact].name, searchQuery.value)
            val highlightedLastName =
                highlightText(filteredContacts[contact].lastName, searchQuery.value)
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
                            text = contactInfo.name,
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
                                    text = initialLetter(contactInfo),
                                    fontSize = fontSize.sp
                                )
                            }
                        } else {
                            Text(
                                text = contactInfo.name.first()
                                    .toString(),
                                fontSize = fontSize.sp
                            )
                        }
                    }
                }
                Text(
                    text = "$highlightedName $highlightedLastName",
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
    openContact: MutableState<Boolean>,
    dataStore: DataStore<Preferences>,
    phoneCallPermissionLauncher: ActivityResultLauncher<String>,
    isEditing: MutableState<Boolean>
) {
    val UI = remember { mutableStateOf("UI") }
    if (isEditing.value) {
        AddContact(UI, contactsList, dataStore, true,showingContact,isEditing)
    } else {
        Surface(
            modifier = Modifier
                .fillMaxSize()
        ) {
            val context = LocalContext.current
            val showDialog = remember { mutableStateOf(false) }
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
                        },
                        actions = {
                            IconButton(
                                onClick = {
                                isEditing.value = true
                                }
                            ) {
                                Icon(Icons.Filled.Edit,"Edit")

                            }
                            IconButton(
                                onClick = {
                                    showDialog.value = true
                                }
                            ) {
                                Icon(Icons.Filled.Delete, contentDescription = "Delete")
                            }
                        }
                    )
                }
            ) {
                if (showDialog.value) {
                    RemoveConfirmation(
                        showDialog,
                        contactsList,
                        showingContact,
                        dataStore,
                        openContact
                    )
                }
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
                                        text = initialLetter(contactInfo),
                                        fontSize = iconFontSize.sp,
                                        textAlign = TextAlign.Center,

                                        )
                                }
                            } else {
                                Text(
                                    text = initialLetter(contactInfo),
                                    fontSize = iconFontSize.sp,
                                    textAlign = TextAlign.Center,

                                    )
                            }
                        } else {
                            Text(
                                text = initialLetter(contactInfo),
                                fontSize = iconFontSize.sp,
                                textAlign = TextAlign.Center,

                                )
                        }
                    }
                    Spacer(modifier = Modifier.size(20.dp))
                    Text(text = "$name $lastName", fontSize = (fontSize + 10).sp)
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(30.dp)
                            .background(
                                MaterialTheme.colorScheme.primaryContainer,
                                RoundedCornerShape(20)
                            )
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
                                modifier = Modifier.padding(20.dp),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Icon(Icons.Filled.Email, "")
                                Text(
                                    text = email,
                                    fontSize = fontSize.sp
                                )
                            }
                        }
                        if (phoneNumber.isNotEmpty()) {
                            // Phone Calls
                            Row(
                                modifier = Modifier
                                    .padding(20.dp)
                                    .clickable {
                                        if (hasPhoneCallPermission(context)) {
                                            val callIntent = Intent(
                                                Intent.ACTION_CALL,
                                                Uri.parse("tel:$phoneNumber")
                                            )
                                            if (callIntent.resolveActivity(context.packageManager) != null) {
                                                context.startActivity(callIntent)
                                            }
                                        } else {
                                            requestPhoneCallPermission(phoneCallPermissionLauncher)
                                        }
                                    },
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Icon(Icons.Filled.Phone, "")
                                Text(
                                    text = contactInfo.phoneNumber.removePrefix("Phone Number ")
                                        .trim(),
                                    fontSize = fontSize.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

fun initialLetter(contactInfo: Contact): String {
    return if (contactInfo.name.isNotEmpty()) {
        contactInfo.name.first().toString()
    } else if (contactInfo.lastName.isNotEmpty()) {
        contactInfo.lastName.first().toString()
    } else if (contactInfo.email.isNotEmpty()) {
        contactInfo.email.first().toString()
    } else {
        " "
    }
}


