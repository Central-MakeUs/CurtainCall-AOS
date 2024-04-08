package com.cmc.curtaincall.domain.repository

import com.cmc.curtaincall.domain.model.auth.LoginResultModel
import kotlinx.coroutines.flow.Flow

interface TokenRepository {
    fun getAccessToken(): Flow<String>

    fun getAccessTokenExpiresAt(): Flow<String>

    fun getRefreshToken(): Flow<String>

    fun getRefreshTokenExpiresAt(): Flow<String>

    suspend fun saveToken(loginResultModel: LoginResultModel)
}
