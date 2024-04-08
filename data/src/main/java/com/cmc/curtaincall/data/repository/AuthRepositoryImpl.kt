package com.cmc.curtaincall.data.repository

import com.cmc.curtaincall.data.source.remote.AuthRemoteSource
import com.cmc.curtaincall.domain.model.auth.LoginResultModel
import com.cmc.curtaincall.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authRemoteSource: AuthRemoteSource
) : AuthRepository {
    override fun requestLogin(provider: String, token: String): Flow<LoginResultModel> =
        authRemoteSource.requestLogin(provider, token).map { it.toModel() }

    override fun requestRefresh(refreshToken: String): Flow<LoginResultModel> =
        authRemoteSource.requestRefresh(refreshToken).map { it.toModel() }

    override fun requestLogout(accessToken: String): Flow<Boolean> =
        authRemoteSource.requestLogout(accessToken)
}
