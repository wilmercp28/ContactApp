package com.example.contactapp

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.launch

@Composable
fun RemoveConfirmation(
    showDialog: MutableState<Boolean>,
    contactsList: MutableList<Contact>,
    showingContact: Int,
    dataStore: DataStore<Preferences>,
    openContact: MutableState<Boolean>
){
    val name = contactsList[showingContact].name.removePrefix("Name ").trim()
    val lastName = contactsList[showingContact].lastName.removePrefix("Last Name ").trim()
    val contactNameAndLastName = "$name $lastName"
    val scope = rememberCoroutineScope()
    AlertDialog(
        onDismissRequest = {
            showDialog.value = false
        },
        title = {
            Text("Confirm Deletion")
        },
        text = {
            Text("Are you sure you want to delete $contactNameAndLastName?")
        },
        confirmButton = {
            Button(
                onClick = {
                    showDialog.value = false
                    contactsList.removeAt(showingContact)
                    scope.launch {
                        SaveData(dataStore).saveContactListWithImage(contactsList)
                    }
                    openContact.value = false
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    showDialog.value = false
                }
            ) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun Alert(
    showDialog: MutableState<Boolean>,
    title: String,
    text:String,
){
    AlertDialog(
        onDismissRequest = {
            showDialog.value = false
        },
        title = {
            Text(title)
        },
        text = {
            Text(text)
        },
        confirmButton = {
            Button(
                onClick = {
                    showDialog.value = false
                }
            ) {
                Text("Confirm")
            }
        }
    )
}
