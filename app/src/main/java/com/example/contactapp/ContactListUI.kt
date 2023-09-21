package com.example.contactapp

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
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
    AnimatedVisibility(
        visible = openContact.value,
        enter = scaleIn(),
        exit = scaleOut()
    ) {
        ShowContactDetails(
            contactsList,
            selectedContact.value,
            openContact,
            dataStore,
            phoneCallPermissionLauncher,
            editingMode
        )
    }
    AnimatedVisibility(
        visible = !openContact.value,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
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

@OptIn(ExperimentalFoundationApi::class)
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
    val sortedContacts = filteredContacts.sortedWith(
        compareBy {
            if (it.name.isNotEmpty()) it.name
            else if (it.lastName.isNotEmpty()) it.lastName
            else if (it.email.isNotEmpty()) it.email
            else it.phoneNumber
        }
    )
    val favoriteContacts = sortedContacts.filter { contact -> contact.favorite.toBoolean() }
    val nonFavoriteContacts = sortedContacts.filterNot { contact -> contact.favorite.toBoolean() }
    val groupedContacts = mapOf(
        "Favorites" to favoriteContacts
    ) + nonFavoriteContacts.groupBy { contact ->
        val key = if (contact.name.isNotEmpty()) {
            contact.name.firstOrNull()?.uppercase() ?: "#"
        } else if (contact.lastName.isNotEmpty()) {
            contact.lastName.firstOrNull()?.uppercase() ?: "#"
        } else if (contact.email.isNotEmpty()) {
            contact.email.firstOrNull()?.uppercase() ?: "#"
        } else {
            "#"
        }
        key
    }
    LazyColumn(
        modifier = Modifier
            .padding(paddingValues),
    ) {
        groupedContacts.forEach { (letter, contacts) ->
            stickyHeader {
                Text(
                    text = letter ?: "#",
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = fontSize.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                )
            }
            items(contacts) { contact ->
                val highlightedName = highlightText(contact.name, searchQuery.value)
                val highlightedLastName = highlightText(contact.lastName, searchQuery.value)
                val highlightedEmail = highlightText(contact.email, searchQuery.value)
                val highlightedPhoneNumber = highlightText(contact.phoneNumber, searchQuery.value)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                        .clickable {
                            selectedContact.value = contactsList.indexOf(contact)
                            openContact.value = true
                        },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Box(modifier = Modifier.size(50.dp), contentAlignment = Alignment.Center) {
                        if (contact.favorite.toBoolean()) {
                            Icon(
                                Icons.Filled.Star,
                                contentDescription = "Favorite",
                                tint = Color.Yellow
                            )
                        }
                    }
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
                        if (contact.photo.isNullOrEmpty()) {
                            Text(
                                text = contact.name,
                                fontSize = fontSize.sp
                            )
                        } else {
                            val imageBytes = Base64.decode(contact.photo, Base64.DEFAULT)
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
                                        text = initialLetter(contact),
                                        fontSize = fontSize.sp
                                    )
                                }
                            } else {
                                Text(
                                    text = contact.name.first()
                                        .toString(),
                                    fontSize = fontSize.sp
                                )
                            }
                        }
                    }
                    if (highlightedName.isNotEmpty() || highlightedLastName.isNotEmpty()) {
                        Text(
                            text = highlightedName,
                            fontSize = fontSize.sp
                        )
                        Text(
                            text = highlightedLastName,
                            fontSize = fontSize.sp
                        )
                    } else if (highlightedEmail.isNotEmpty()) {
                        Text(
                            text = highlightedEmail,
                            fontSize = fontSize.sp
                        )
                    } else Text(text = highlightedPhoneNumber, fontSize = fontSize.sp)
                }

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun ShowContactDetails(
    contactsList: MutableList<Contact>,
    showingContact: Int,
    openContact: MutableState<Boolean>,
    dataStore: DataStore<Preferences>,
    phoneCallPermissionLauncher: ActivityResultLauncher<String>,
    isEditing: MutableState<Boolean>
) {
    val UI = rememberSaveable { mutableStateOf("UI") }
    AnimatedVisibility(
        visible = isEditing.value,
        enter = scaleIn(),
        exit = scaleOut()
    ) {
        AddContact(UI, contactsList, dataStore, true, showingContact, isEditing)
    }
    AnimatedVisibility(
        visible = !isEditing.value,
        enter = scaleIn(),
        exit = scaleOut()
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
        ) {
            val scope = rememberCoroutineScope()
            val context = LocalContext.current
            val showDialog = remember { mutableStateOf(false) }
            val iconFontSize = 100
            val fontSize = 20
            val contactInfo = contactsList[showingContact]
            val id = contactInfo.id
            val photo = contactInfo.photo
            val name = contactInfo.name.removePrefix("Name ").trim()
            val lastName = contactInfo.lastName.removePrefix("Last Name ").trim()
            val email = contactInfo.email.removePrefix("Email ").trim()
            val phoneNumber = contactInfo.phoneNumber.removePrefix("Phone Number ").trim()
            val favorite = rememberSaveable { mutableStateOf(contactInfo.favorite.toBoolean()) }
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
                                    favorite.value = !favorite.value
                                    val newContact = Contact(
                                        id = id,
                                        name = name,
                                        lastName = lastName,
                                        phoneNumber = phoneNumber,
                                        email = email,
                                        photo = photo,
                                        favorite = favorite.value.toString()
                                    )
                                    changeContact(contactsList, newContact, showingContact)
                                    scope.launch {
                                        SaveData(dataStore).saveContactListWithImage(contactsList)
                                    }
                                }
                            ) {
                                if (favorite.value) {
                                    Icon(Icons.Filled.Star, "Favorite", tint = Color.Yellow)
                                } else {
                                    Icon(Icons.Filled.Star, "No Favorite")
                                }
                            }
                            IconButton(
                                onClick = {
                                    isEditing.value = true
                                }
                            ) {
                                Icon(Icons.Filled.Edit, "Edit")

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
                    if (name.isNotEmpty() || lastName.isNotEmpty()) {
                        Text(text = "$name $lastName", fontSize = (fontSize + 10).sp)
                    } else if (email.isNotEmpty()) {
                        Text(text = email, fontSize = (fontSize + 10).sp)
                    } else Text(text = phoneNumber, fontSize = (fontSize + 10).sp)
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(30.dp)
                            .background(
                                MaterialTheme.colorScheme.primaryContainer,
                                RoundedCornerShape(20)
                            )
                    ) {
                        Spacer(modifier = Modifier.size(20.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .padding(5.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            IconButton(onClick = {
                                if (hasPhoneCallPermission(context)) {
                                    try {
                                        val phoneNumber = contactInfo.phoneNumber
                                        if (!phoneNumber.isNullOrEmpty()) {
                                            val callIntent = Intent(
                                                Intent.ACTION_CALL,
                                                Uri.parse("tel:$phoneNumber")
                                            )
                                                context.startActivity(callIntent)
                                        }
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                } else {
                                    requestPhoneCallPermission(phoneCallPermissionLauncher)
                                }
                            }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.call_fill0_wght400_grad0_opsz24),
                                    contentDescription = "MakeCall",
                                    modifier = Modifier
                                        .size(100.dp)
                                )
                            }
                            IconButton(onClick = {
                                val smsUri = Uri.parse("smsto:$phoneNumber")
                                val smsIntent = Intent(Intent.ACTION_SENDTO, smsUri)
                                smsIntent.setPackage("com.google.android.apps.messaging")
                                context.startActivity(smsIntent)
                            }
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.chat_fill0_wght400_grad0_opsz24),
                                    contentDescription = "SendSms",
                                    modifier = Modifier
                                        .size(100.dp)
                                )

                            }
                        }
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


