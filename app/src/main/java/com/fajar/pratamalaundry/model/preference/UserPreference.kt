package com.fajar.pratamalaundry.model.preference

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import androidx.datastore.preferences.core.*
import com.fajar.pratamalaundry.model.user.UserModel

class UserPreference private constructor(private val dataStore: DataStore<Preferences>) {
    fun getUser(): Flow<UserModel> {
        return dataStore.data.map { preferences ->
            UserModel(
                preferences[ID_KEY] ?: 0,
                preferences[EMAIL_KEY] ?: "",
                preferences[ADDRESS_KEY] ?: "",
                preferences[NOHP_KEY] ?: "",
                preferences[NAME_KEY] ?: "",
                preferences[STATE_TOKEN] ?: "",
                preferences[STATE_KEY] ?: false,
            )
        }
    }


    fun getToken(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[STATE_TOKEN] ?: ""
        }
    }

    suspend fun saveToken(token: String) {
        dataStore.edit { preferences ->
            preferences[STATE_TOKEN] = token
        }
    }

    fun getIsLogin(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[STATE_KEY] ?: false
        }
    }

    suspend fun saveUser(user: UserModel) {
        dataStore.edit { preferences ->
            preferences[ID_KEY] = user.localid
            preferences[EMAIL_KEY] = user.email
            preferences[NAME_KEY] = user.nama
            preferences[NOHP_KEY] = user.nohp
            preferences[ADDRESS_KEY] = user.alamat
            preferences[STATE_TOKEN] = user.token
            preferences[STATE_KEY] = user.isLogin
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences[ID_KEY]
            preferences[EMAIL_KEY] = ""
            preferences[STATE_KEY] = false
            preferences[STATE_TOKEN] = ""
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null

        private val ID_KEY = intPreferencesKey("id")
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val NAME_KEY = stringPreferencesKey("nama")
        private val NOHP_KEY = stringPreferencesKey("nohp")
        private val ADDRESS_KEY = stringPreferencesKey("alamat")
        private val STATE_KEY = booleanPreferencesKey("state")
        private val STATE_TOKEN = stringPreferencesKey("token")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}