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
    suspend fun saveContactListWithImage(contactList: MutableList<Contact>) {
        val gson = Gson()
        val contactListJson = gson.toJson(contactList)

        dataStore.edit { preferences ->
            preferences[contactListKey] = contactListJson
        }
    }

    suspend fun loadContactListWithImage(): MutableList<Contact>? {
        val gson = Gson()
        val jsonString = dataStore.data.map { it[contactListKey] }.firstOrNull()

        val contactListType = object : TypeToken<MutableList<Contact>>() {}.type

        return if (jsonString != null) {
            gson.fromJson<MutableList<Contact>>(jsonString, contactListType)
        } else {
            null
        }
    }
}















