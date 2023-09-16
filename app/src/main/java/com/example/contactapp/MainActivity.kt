package com.example.contactapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier

import com.example.contactapp.ui.theme.ContactAppTheme
import android.content.Context
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.datastore.preferences.preferencesDataStore


private val Context.dataStore by preferencesDataStore(name = "Contacts")
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContent {

            ContactAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var loading by remember { mutableStateOf(true) }
                    val contactsList = rememberSaveable{ mutableListOf<Contact>()}
                    LaunchedEffect(contactsList) {
                        val loadedContacts = SaveData(dataStore).loadContactList()
                        contactsList.addAll(loadedContacts)
                        loading = false
                    }
                    val selectedScreen = remember { mutableStateOf("UI") }
                    if (loading) {
                        // Show a loading indicator or placeholder
                        CircularProgressIndicator()
                    } else {
                        // Render your UI components here
                        UI(selectedScreen, contactsList)
                        when (selectedScreen.value) {
                            "UI" -> UI(selectedScreen, contactsList)
                            "AddContact" -> AddContact(selectedScreen, contactsList, dataStore)
                        }
                    }

                }
            }
        }
    }
}


