package com.example.contactapp

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map


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
}















