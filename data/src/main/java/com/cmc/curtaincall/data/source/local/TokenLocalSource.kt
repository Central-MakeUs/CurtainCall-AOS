package com.cmc.curtaincall.data.source.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.cmc.curtaincall.core.local.PreferenceKeys.ACCESS_TOKEN
import com.cmc.curtaincall.core.local.PreferenceKeys.ACCESS_TOKEN_EXPIRESAT
import com.cmc.curtaincall.core.local.PreferenceKeys.REFRESH_TOKEN
import com.cmc.curtaincall.core.local.PreferenceKeys.REFRESH_TOKEN_EXPIRESAT
import com.cmc.curtaincall.core.local.qualifiers.TokenDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

class TokenLocalSource @Inject constructor(
    @TokenDataStore private val dataStore: DataStore<Preferences>
) {
    fun getAccessToken(): Flow<String> =
        dataStore.data.map { preferences ->
            preferences[ACCESS_TOKEN] ?: ""
        }

    fun getAccessTokenExpiration(): Flow<String> =
        dataStore.data.map { preferences ->
            preferences[ACCESS_TOKEN_EXPIRESAT] ?: ""
        }

    fun getRefreshToken(): Flow<String> =
        dataStore.data.map { preferences ->
            preferences[REFRESH_TOKEN] ?: ""
        }

    fun getRefreshTokenExpiresAt(): Flow<String> =
        dataStore.data.map { preferences ->
            preferences[REFRESH_TOKEN_EXPIRESAT] ?: ""
        }

    suspend fun saveToken(
        accessToken: String,
        accessTokenExpiresAt: String,
        refreshToken: String,
        refreshTokenExpiresAt: String
    ) {
        dataStore.edit { preferences ->
            Timber.d(
                "saveToken " +
                        "accessToken: $accessToken " +
                        "accessTokenExpiresAt: $accessTokenExpiresAt " +
                        "refreshToken: $refreshToken" +
                        "refreshTokenExpiresAt: $refreshTokenExpiresAt"
            )
            preferences[ACCESS_TOKEN] = accessToken
            preferences[ACCESS_TOKEN_EXPIRESAT] = accessTokenExpiresAt
            preferences[REFRESH_TOKEN] = refreshToken
            preferences[REFRESH_TOKEN_EXPIRESAT] = refreshTokenExpiresAt
        }
    }
}
