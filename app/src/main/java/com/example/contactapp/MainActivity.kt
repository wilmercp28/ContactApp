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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.datastore.preferences.preferencesDataStore
import com.android.tools.build.jetifier.core.utils.Log


private val Context.dataStore by preferencesDataStore(name = "Contacts")
class MainActivity : ComponentActivity() {
    private val phoneCallPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {

            } else {

            }
        }
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContent {

            ContactAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var loading by remember { mutableStateOf(true) }
                    val contactsList = rememberSaveable { mutableListOf<Contact>() }

                    LaunchedEffect(contactsList) {
                        val loadedContacts = SaveData(dataStore).loadContactListWithImage()
                        contactsList.addAll(loadedContacts ?: emptyList())
                        Log.d("ContactList",contactsList.toString())
                        loading = false
                    }
                    val selectedScreen = rememberSaveable { mutableStateOf("UI") }
                    if (loading) {
                        CircularProgressIndicator()
                    } else {
                        // Screen Selector
                        when (selectedScreen.value) {
                            "UI" -> { UI(selectedScreen, contactsList, dataStore,phoneCallPermissionLauncher) }
                            "AddContact" -> AddContact(selectedScreen,contactsList,dataStore,false)
                        }
                    }

                }
            }
        }
    }

}





