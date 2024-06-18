package com.cmc.curtaincall.feature.auth.login.kakao

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cmc.curtaincall.common.designsystem.R
import com.cmc.curtaincall.feature.auth.login.LoginResponse
import com.cmc.curtaincall.feature.auth.login.LoginViewModel
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import timber.log.Timber
import kotlin.coroutines.resume

private const val LOGIN_KAKAO_BUTTON = "LOGIN_KAKAO_BUTTON"
private const val KAKAO_PROVIDER = "kakao"

@Composable
fun LoginKaKaoButton(
    modifier: Modifier = Modifier,
    loginViewModel: LoginViewModel = hiltViewModel(),
    onNavigateSignUpTerms: () -> Unit = {},
    onNavigateHome: () -> Unit = {}
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    Image(
        painter = painterResource(R.drawable.ic_kakao_login),
        contentDescription = LOGIN_KAKAO_BUTTON,
        modifier = modifier
            .size(50.dp)
            .clickable {
                coroutineScope.launch {
                    when (val result = loginKaKao(context)) {
                        is LoginResponse.Success -> {
                            loginViewModel.fetchLogin(KAKAO_PROVIDER, result.token)
                        }

                        is LoginResponse.Failure -> {
                            Timber.e(result.errorMsg)
                        }
                    }
                }
            }
    )
}

private suspend fun loginKaKao(context: Context): LoginResponse = suspendCancellableCoroutine { continuation ->
    if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
        UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
            if (error != null) {
                if (error is ClientError && error.reason == ClientErrorCause.Cancelled) return@loginWithKakaoTalk
                loginWithKaKaoAccount(context, continuation)
            } else if (token != null) {
                token.idToken?.let { idToken ->
                    continuation.resume(LoginResponse.Success(idToken))
                }
            }
        }
    } else {
        loginWithKaKaoAccount(context, continuation)
    }
}

private fun loginWithKaKaoAccount(
    context: Context,
    continuation: CancellableContinuation<LoginResponse>
) {
    UserApiClient.instance.loginWithKakaoAccount(context) { token, error ->
        if (error != null) {
            continuation.resume(LoginResponse.Failure(error.localizedMessage))
        } else if (token != null) {
            token.idToken?.let { idToken ->
                continuation.resume(LoginResponse.Success(idToken))
            }
        }
    }
}
