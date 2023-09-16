package com.example.contactapp

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun ContactListShow(
    contactsList: MutableList<Contact>,
    paddingValues: PaddingValues,
    openContact: MutableState<Boolean>,
    selectedContact: MutableState<Int>
) {
    val initialIconSize = 50.dp
    val fontSize = 20.sp

        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
        ) {
            items(contactsList.size) { contact ->
                val contactInfo = contactsList[contact]
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                        .clickable {
                            selectedContact.value = contact
                            openContact.value = true
                        },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(initialIconSize)
                            .background(
                                MaterialTheme.colorScheme.primary,
                                RoundedCornerShape(initialIconSize)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = contactInfo.name.removePrefix("Name ").first().toString(),
                            fontSize = fontSize
                        )
                    }
                    Text(
                        text = "${contactInfo.name.removePrefix("Name ")} ${
                            contactInfo.lastName.removePrefix(
                                "Last Name "
                            )
                        }",
                        fontSize = fontSize
                    )
                }

            }
        }
    }
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowContactDetails(
    contactsList: MutableList<Contact>,
    showingContact: Int,
    openContact: MutableState<Boolean>
){
    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val contactInfo = contactsList[showingContact]
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
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Back" )
                        }
                    }

                )
            }
        ) {
            Column(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ContactPhoto(150.dp,contactInfo)
                Text(text = contactInfo.name.removePrefix("Name "))
                Text(text = contactInfo.lastName.removePrefix("Last Name "))
                Text(text = contactInfo.email.removePrefix("Email "))
                Text(text = contactInfo.phoneNumber.removePrefix("Phone Number "))
            }

        }
        
    }
    
}