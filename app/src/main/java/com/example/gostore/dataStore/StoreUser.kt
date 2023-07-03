package com.example.gostore.dataStore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class StoreUser(private val context: Context) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("User_Data")
        val userId = intPreferencesKey("User_Id")
    }

    val getUserId: Flow<Int?> = context.dataStore.data
        .map { preferences ->
            preferences[userId] ?: 0
        }

    suspend  fun setUserId(id: Int) {
        context.dataStore.edit { preferences ->
            preferences[userId] = id
        }
    }

}

