package com.smartsurveillance_mobile.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class DataStorage(
    private val dataStore: DataStore<Preferences>,
) {

    // Gettery - Flow a suspend
    val textFlow: Flow<String?> = dataStore.data
        .map { preferences -> preferences[TextDataKey] }

    suspend fun isChecked(): Boolean = dataStore.data
        .map { preferences -> preferences[CheckDataKey] }.first() ?: false

    suspend fun setText(value: String) = dataStore.edit { preferences ->
        preferences[TextDataKey] = value
    }

    suspend fun setChecked(value: Boolean) = dataStore.edit { preferences ->
        preferences[CheckDataKey] = value
    }

    companion object {
        private val TextDataKey = stringPreferencesKey("TextData")
        private val CheckDataKey = booleanPreferencesKey("CheckData")
    }
}