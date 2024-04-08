package com.cmc.curtaincall.domain.repository

import com.cmc.curtaincall.domain.model.auth.LoginResultModel
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun requestLogin(provider: String, token: String): Flow<LoginResultModel>
    fun requestRefresh(refreshToken: String): Flow<LoginResultModel>
    fun requestLogout(accessToken: String): Flow<Boolean>
}
