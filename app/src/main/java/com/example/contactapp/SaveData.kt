package com.example.contactapp

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.runtime.MutableState
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import java.io.ByteArrayOutputStream



class SaveData(private val dataStore: DataStore<Preferences>) {
    private val contactListKey = stringPreferencesKey("Contacts")
    private val imageKey = stringPreferencesKey("Image")

    suspend fun saveContactListWithImage(contactList: MutableList<Contact>, image: MutableState<Bitmap?>) {
        val gson = Gson()
        val contactListJson = gson.toJson(contactList)
        val imageBytes = convertBitmapToByteArray(image)

        dataStore.edit { preferences ->
            preferences[contactListKey] = contactListJson
            preferences[imageKey] = Base64.encodeToString(imageBytes, Base64.DEFAULT)
        }
    }

    suspend fun loadContactListWithImage(): Pair<MutableList<Contact>, Bitmap?> {
        val gson = Gson()
        val jsonString = dataStore.data.map { it[contactListKey] }.firstOrNull()
        val imageString = dataStore.data.map { it[imageKey] }.firstOrNull()

        val contactListType = object : TypeToken<MutableList<Contact>>() {}.type

        val contactList = if (jsonString != null) {
            gson.fromJson<MutableList<Contact>>(jsonString, contactListType)
        } else {
            mutableListOf()
        }

        val image = if (imageString != null) {
            convertStringToBitmap(imageString)
        } else {
            null
        }

        return Pair(contactList, image)
    }
    private fun convertBitmapToByteArray(bitmap: Bitmap): ByteArray {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return outputStream.toByteArray()
    }

    private fun convertStringToBitmap(imageString: String): Bitmap? {
        val imageBytes = Base64.decode(imageString, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }

}















