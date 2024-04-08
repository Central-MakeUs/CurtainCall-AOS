package com.cmc.curtaincall.core.network.interceptors

import android.annotation.SuppressLint
import com.cmc.curtaincall.domain.repository.AuthRepository
import com.cmc.curtaincall.domain.repository.TokenRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

private const val DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss"

class CurtainCallInterceptor @Inject constructor(
    private val tokenRepository: TokenRepository,
    private val authRepository: AuthRepository
) : Interceptor {

    @SuppressLint("SimpleDateFormat")
    override fun intercept(chain: Interceptor.Chain): Response {
        var accessToken = runBlocking { tokenRepository.getAccessToken().first() }
        val accessTokenExpiresAt = runBlocking { tokenRepository.getAccessTokenExpiresAt().first() }
        val refreshToken = runBlocking { tokenRepository.getRefreshToken().first() }
        val today = SimpleDateFormat(DATE_PATTERN, Locale.KOREA).format(Calendar.getInstance().time)

        if (accessTokenExpiresAt < today) {
            Timber.d("CurtainCallInterceptor token Expire, So refresh token")
            try {
                val result = runBlocking { authRepository.requestRefresh(refreshToken).first() }
                accessToken = result.accessToken
                runBlocking {
                    tokenRepository.saveToken(result)
                }
            } catch (e: Exception) {
                Timber.e("CurtainCallInterceptor ${e.localizedMessage}")
            }
        }

        val newRequest = chain.request()
            .newBuilder()
            .addHeader("Authorization", "Bearer $accessToken")
            .build()
        return chain.proceed(newRequest)
    }
}
