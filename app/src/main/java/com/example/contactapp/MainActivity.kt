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
                    val contactsList = rememberSaveable{ mutableListOf<Contact>()}
                    val selectedScreen = remember { mutableStateOf("UI") }
                    when(selectedScreen.value) {
                        "UI" -> UI(selectedScreen,contactsList)
                        "AddContact" -> AddContact(selectedScreen,contactsList)
                    }

                }
            }
        }
    }
}
