package com.example.contactapp




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
        name = name,
        lastName = lastName,
        phoneNumber = phoneNumber,
        email = email,
        photo = photo
    )
    contactsList.add(newContact)
}

