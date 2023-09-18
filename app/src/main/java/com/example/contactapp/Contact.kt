package com.example.contactapp

import com.android.tools.build.jetifier.core.utils.Log


data class Contact(

    val id: Int,
    val photo: String?,
    val name: String,
    val lastName: String,
    val phoneNumber: String,
    val email: String,
)

fun addContact(
    contactsList: MutableList<Contact>,
    photo: String?,
    name: String,
    lastName: String,
    phoneNumber: String,
    email: String
) {
    val newContactId = contactsList.size + 1
    val newContact = Contact(
        id = newContactId,
        name = name.removePrefix("Name "),
        lastName = lastName.removePrefix("Last Name "),
        phoneNumber = phoneNumber.removePrefix("Phone Number "),
        email = email.removePrefix("Email "),
        photo = photo
    )
    Log.d("New Contact", newContact.toString())
    contactsList.add(newContact)
}

