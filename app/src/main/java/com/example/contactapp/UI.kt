package com.example.contactapp

import android.graphics.Bitmap
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UI(
    selectedScreen: MutableState<String>,
    contactsList: MutableList<Contact>
) {
    val selectedContact = remember { mutableStateOf(0) }
    val openContact = remember { mutableStateOf(false) }
    if (openContact.value) {
        ShowContactDetails(contactsList, selectedContact.value,openContact)
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
                    ContactListShow(contactsList, it,openContact,selectedContact)
                }
            }
        }
    }
}


