package com.example.contactapp

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddContact(
    selectedScreen: MutableState<String>,
    contactsList: MutableList<Contact>,
    dataStore: DataStore<Preferences>,
    isEditing: Boolean,
    contactListIndex: Int = 0,
    editingMode: MutableState<Boolean> = mutableStateOf(false)
) {
    val showBackAlert = remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val name =
        remember { mutableStateOf(if (editingMode.value) contactsList[contactListIndex].name else "") }
    val lastName =
        remember { mutableStateOf(if (editingMode.value) contactsList[contactListIndex].lastName else "") }
    val phoneNumber =
        remember { mutableStateOf(if (editingMode.value) contactsList[contactListIndex].phoneNumber else "") }
    val email =
        remember { mutableStateOf(if (editingMode.value) contactsList[contactListIndex].email else "") }
    val photo = remember { mutableStateOf<Bitmap?>(null) }
    val hideKeyboard = remember { mutableStateOf(false) }
    if (isEditing) {
        val imageBytes = Base64.decode(contactsList[contactListIndex].photo, Base64.DEFAULT)
        if (imageBytes != null) {
            val bitmapPhoto: Bitmap? = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            photo.value = bitmapPhoto
        }
    }
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .clickable { hideKeyboard.value = true }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = "Create Contact") },
                    actions = {
                        IconButton(
                            onClick = {
                                if (!editingMode.value) {
                                    val photoString = encodeBitmapToBase64(photo.value)
                                    addContact(
                                        contactsList,
                                        photoString.toString(),
                                        name.value,
                                        lastName.value,
                                        phoneNumber.value,
                                        email.value
                                    )
                                    name.value = ""
                                    lastName.value = ""
                                    phoneNumber.value = ""
                                    email.value = ""
                                    photo.value = null
                                } else {
                                    val photoString = encodeBitmapToBase64(photo.value)
                                    val newContact = Contact(
                                        id = contactsList[contactListIndex].id,
                                        name = name.value,
                                        lastName = lastName.value,
                                        phoneNumber = phoneNumber.value,
                                        email = email.value,
                                        photo = photoString.toString()
                                    )
                                    changeContact(contactsList, newContact, contactListIndex)
                                    name.value = ""
                                    lastName.value = ""
                                    phoneNumber.value = ""
                                    email.value = ""
                                    photo.value = null
                                }
                                scope.launch {
                                    SaveData(dataStore).saveContactListWithImage(contactsList)
                                    selectedScreen.value = "UI"
                                    editingMode.value = false
                                }
                            },
                            modifier = Modifier
                                .background(
                                    MaterialTheme.colorScheme.primary,
                                    RoundedCornerShape(100)
                                )
                                .width(100.dp)
                        ) {
                            Text(
                                text = "Save",
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                if (name.value.isNotEmpty() || lastName.value.isNotEmpty() || phoneNumber.value.isNotEmpty() || email.value.isNotEmpty()) {
                                    showBackAlert.value = true
                                } else {
                                    selectedScreen.value = "UI"
                                    editingMode.value = false
                                }
                            }
                        ) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                )
            }
        ) {
            if (showBackAlert.value) {
                ConfirmBeforeBacking(
                    "Are You Sure?",
                    "All Unsaved data will be remove"
                ) { boolean ->
                    showBackAlert.value = boolean
                    if (boolean && editingMode.value) {
                        editingMode.value = false
                        selectedScreen.value = "UI"
                    }
                }
            }
            LazyColumn(
                modifier = Modifier
                    .padding(it),
                content = {
                    item {
                        AddPhotoContact(photo)
                    }
                    item {
                        AddContactTextFields(
                            "Name",
                            name,
                            false,
                            Icons.Filled.Face,
                            hideKeyboard.value
                        )
                    }
                    item {
                        AddContactTextFields(
                            "Last Name",
                            lastName,
                            false,
                            null,
                            hideKeyboard.value
                        )
                    }
                    item {
                        AddContactTextFields(
                            "Phone Number",
                            phoneNumber,
                            true,
                            Icons.Filled.Phone,
                            hideKeyboard.value
                        )
                    }
                    item {
                        AddContactTextFields(
                            "Email",
                            email,
                            false,
                            Icons.Filled.Email,
                            hideKeyboard.value
                        )
                    }
                }
            )
        }
    }
}


@Composable
fun AddPhotoContact(
    photo: MutableState<Bitmap?>
) {
    var imageUri by rememberSaveable { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    imageUri?.let {
        photo.value = loadImageFromUri(context.contentResolver, it)
    }
    val galleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
            imageUri = it

        }
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton(
            onClick = {
                galleryLauncher.launch("image/*")
            },
            modifier = Modifier
                .size(100.dp)

        ) {
            if (photo.value != null) {
                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clip(CircleShape)
                ) {
                    Image(
                        bitmap = photo.value!!.asImageBitmap(),
                        contentDescription = "SelectedPhoto",
                        modifier = Modifier
                            .fillMaxSize(),
                        contentScale = ContentScale.FillBounds,
                        alignment = Alignment.Center

                    )
                }
            } else {
                Image(
                    painter = painterResource(id = R.drawable.add_photo_alternate_fill0_wght400_grad0_opsz24),
                    modifier = Modifier
                        .fillMaxSize(),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
                    contentDescription = "Add photo"
                )
            }
        }
        Text(
            text = "Add photo",
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}
