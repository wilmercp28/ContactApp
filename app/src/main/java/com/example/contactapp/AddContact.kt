package com.example.contactapp

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddContact(
    selectedScreen: MutableState<String>,
    contactsList: MutableList<Contact>,
    dataStore: DataStore<Preferences>
) {
    val scope = rememberCoroutineScope()
    val name = remember { mutableStateOf("") }
    val lastName = remember { mutableStateOf("") }
    val phoneNumber = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
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
                                addContact(contactsList,name.value,lastName.value,phoneNumber.value,email.value)
                                scope.launch {
                                    SaveData(dataStore).saveContactList(contactsList)
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
            LazyColumn(
                modifier = Modifier
                    .padding(it),
                content = {
                    item { AddPhotoContact(hideKeyboard) }
                    item {
                        AddContactTextFields("Name", false, Icons.Filled.Face, hideKeyboard.value) { lostFocusString ->
                            name.value = lostFocusString
                        }
                    }
                    item {
                        AddContactTextFields("Last Name", false, null, hideKeyboard.value) { lostFocusString ->
                            lastName.value = lostFocusString
                        }
                    }
                    item {
                        AddContactTextFields("Phone Number", true, Icons.Filled.Phone, hideKeyboard.value) { lostFocusString ->
                            phoneNumber.value = lostFocusString
                        }
                    }
                    item {
                        AddContactTextFields("Email", false, Icons.Filled.Email, hideKeyboard.value) { lostFocusString ->
                            email.value = lostFocusString
                        }
                    }
                }
            )
        }
    }
}

fun saveContact(

){

}
@Composable
fun AddPhotoContact(
    hideKeyboard: MutableState<Boolean>
) {
    IconButton(
        onClick = {
                  hideKeyboard.value = true
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.Face,
            contentDescription ="PhotoPlaceHolder",
        modifier = Modifier
            .fillMaxSize()
            )
    }
    Text(
        text = "Add photo",
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
    )
}