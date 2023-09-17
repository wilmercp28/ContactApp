package com.example.contactapp

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import java.io.ByteArrayOutputStream



class SaveData(private val dataStore: DataStore<Preferences>) {
    private val key = stringPreferencesKey("Contacts")

    suspend fun saveContactList(contactList: MutableList<Contact>) {
        val gson = Gson()
        val contactListJson = gson.toJson(contactList)
        dataStore.edit { preferences ->
            preferences[key] = contactListJson
        }
    }

    suspend fun loadContactList(): MutableList<Contact> {
        val gson = Gson()
        val jsonString = dataStore.data.map { it[key] }.firstOrNull()

        return if (jsonString != null) {
            gson.fromJson(jsonString, object : TypeToken<MutableList<Contact>>() {}.type)
        } else {
            mutableListOf()
        }
    }
    suspend fun saveContactImage(bitmap: Bitmap) {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        val encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT)
        dataStore.edit { preferences ->
            preferences[imageKey] = encodedImage
        }
    }

    fun loadContactImage(): Bitmap? {
        val encodedImage = dataStore.data.map { it[imageKey] }.firstOrNull()
        return if (encodedImage != null) {
            val byteArray = Base64.decode(encodedImage, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        } else {
            null
        }
    }
}















