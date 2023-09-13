package com.example.contactapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddContact(selectedScreen: MutableState<String>) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = "Create Contact") },
                    actions = {
                        IconButton(
                            onClick = {

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
                content ={
                    item { AddPhotoContact() }
                    item {  ContactTextFields(textFieldName = "Name") }
                    item {  ContactTextFields(textFieldName = "Last Name") }
                    item {  ContactTextFields(textFieldName = "Phone Number") }
                    item {  ContactTextFields(textFieldName = "Email") }

                }

            )
        }
    }
}
@Composable
fun AddPhotoContact() {
    IconButton(
        onClick = { /*TODO*/ },
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactTextFields(
    textFieldName: String
){
    val textField = remember{ mutableStateOf("") }
    OutlinedTextField(
        value = textField.value,
        onValueChange = {textField.value = it},
        label = { Text(text = textFieldName) },
        modifier = Modifier
    )

}