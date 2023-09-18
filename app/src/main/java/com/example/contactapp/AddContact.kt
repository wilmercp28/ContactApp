package com.example.contactapp

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
    dataStore: DataStore<Preferences>
) {
    val showAlert = remember { mutableStateOf(false) }
    val validInputList = remember { mutableListOf(false) }
    val scope = rememberCoroutineScope()
    val name = remember { mutableStateOf("") }
    val lastName = remember { mutableStateOf("") }
    val phoneNumber = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val photo = remember { mutableStateOf<Bitmap?>(null) }
    val hideKeyboard = remember { mutableStateOf(false) }
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
                                if (validInputList.all { it }) {
                                    val photoString = encodeBitmapToBase64(photo.value)
                                    addContact(contactsList, photoString.toString(), name.value, lastName.value, phoneNumber.value, email.value)
                                    scope.launch {
                                        SaveData(dataStore).saveContactListWithImage(contactsList)
                                        selectedScreen.value = "UI"
                                    }
                                } else {
                                    showAlert.value = true
                                }
                            },
                            modifier = Modifier
                                .background(
                                    MaterialTheme.colorScheme.primary,
                                    RoundedCornerShape(50)
                                )
                                .width(90.dp)
                        ){
                            Text(
                                text = "Save",
                            textAlign = TextAlign.Center)
                        }
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                selectedScreen.value = "UI"
                            }
                        ) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Back" )
                        }
                    }
                )
            }
        ) {
            if (showAlert.value)
            {
                Alert(showAlert,"Missing required fields","" )
            }
            LazyColumn(
                modifier = Modifier
                    .padding(it),
                content = {
                    item {
                        AddPhotoContact(hideKeyboard,photo)
                    }
                    item {
                        AddContactTextFields("Name", false, Icons.Filled.Face,true,validInputList,hideKeyboard.value) { lostFocusString ->
                            name.value = lostFocusString
                        }
                    }
                    item {
                        AddContactTextFields("Last Name", false, null,false,validInputList,hideKeyboard.value) { lostFocusString ->
                            lastName.value = lostFocusString
                        }
                    }
                    item {
                        AddContactTextFields("Phone Number", true, Icons.Filled.Phone,false,validInputList,hideKeyboard.value) { lostFocusString ->
                            phoneNumber.value = lostFocusString
                        }
                    }
                    item {
                        AddContactTextFields("Email", false, Icons.Filled.Email,false,validInputList,hideKeyboard.value) { lostFocusString ->
                            email.value = lostFocusString
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun AddPhotoContact(
    hideKeyboard: MutableState<Boolean>,
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
    IconButton(
        onClick = {
            hideKeyboard.value = true
            galleryLauncher.launch("image/*")
        },
        modifier = Modifier
            .fillMaxWidth()
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
            Icon(
                imageVector = Icons.Filled.Face,
                contentDescription = "PhotoPlaceHolder",
                modifier = Modifier
                    .fillMaxSize()
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
