package com.cmc.curtaincall.splash

import androidx.lifecycle.viewModelScope
import com.cmc.curtaincall.common.utility.extensions.getTodayDate
import com.cmc.curtaincall.core.base.RootViewModel
import com.cmc.curtaincall.domain.model.auth.LoginResultModel
import com.cmc.curtaincall.domain.repository.AuthRepository
import com.cmc.curtaincall.domain.repository.ChattingRepository
import com.cmc.curtaincall.domain.repository.LaunchRepository
import com.cmc.curtaincall.domain.repository.MemberRepository
import com.cmc.curtaincall.domain.repository.TokenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.models.User
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.zip
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val tokenRepository: TokenRepository,
    private val memberRepository: MemberRepository,
    private val launchRepository: LaunchRepository,
    private val chattingRepository: ChattingRepository
) : RootViewModel<SplashSideEffect>() {

    private var refreshCount = 0

    private val _isFirstEntryOnBoarding = MutableStateFlow(false)
    val isFirstEntryOnBoarding = _isFirstEntryOnBoarding.asStateFlow()

    private val _user = MutableSharedFlow<User>()
    val user = _user.asSharedFlow()

    init {
        checkIsFirstEntryOnBoarding()
        isValidationToken()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun isValidationToken() {
        val today = getTodayDate()
        tokenRepository.getAccessToken()
            .zip(tokenRepository.getAccessTokenExpiresAt()) { accessToken, accessTokenExpiresAt ->
                LoginResultModel(
                    accessToken = accessToken,
                    accessTokenExpiresAt = accessTokenExpiresAt
                )
            }.zip(tokenRepository.getRefreshToken()) { loginResultModel, refreshToken ->
                loginResultModel.copy(
                    refreshToken = refreshToken
                )
            }.zip(tokenRepository.getRefreshTokenExpiresAt()) { loginResultModel, refreshTokenExpiresAt ->
                loginResultModel.copy(
                    refreshTokenExpiresAt = refreshTokenExpiresAt
                )
            }.zip(memberRepository.getMemberId()) { loginResultModel, memberId ->
                loginResultModel.copy(
                    memberId = memberId
                )
            }.onEach { loginResult ->
                Timber.d("isValidationToken $loginResult refreshCount $refreshCount")
                if (loginResult.accessToken.isEmpty()) {
                    sendSideEffect(SplashSideEffect.NeedLogin)
                } else {
                    if (loginResult.accessTokenExpiresAt < today) {
                        if (loginResult.refreshTokenExpiresAt < today) {
                            sendSideEffect(SplashSideEffect.NeedLogin)
                        } else {
                            if (refreshCount >= 3) {
                                sendSideEffect(SplashSideEffect.NeedLogin)
                            } else {
                                refreshToken(loginResult.refreshToken)
                            }
                        }
                    } else {
                        if (loginResult.memberId == Int.MIN_VALUE) {
                            sendSideEffect(SplashSideEffect.NeedLogin)
                        } else {
                            getCurrentUser(loginResult.memberId)
                        }
                    }
                }
            }.launchIn(viewModelScope)
    }

    private fun getCurrentUser(memberId: Int?) {
        memberId?.let { id ->
            memberRepository.requestMemberInfo(id)
                .onEach {
                    _user.emit(
                        User(
                            id = it.id.toString(),
                            name = it.nickname,
                            image = it.imageUrl.toString()
                        )
                    )
                }.launchIn(viewModelScope)
        }
    }

    fun connectChattingClient(
        chatClient: ChatClient,
        user: User
    ) {
        if (chatClient.clientState.isConnecting) {
            sendSideEffect(SplashSideEffect.AutoLogin)
        } else {
            chattingRepository.requestChattingToken()
                .onEach {
                    chatClient.connectUser(
                        user = user,
                        token = it.value
                    ).enqueue {
                        Timber.d("chatClient connect User ${it.isSuccess}")
                    }
                }.onCompletion {
                    sendSideEffect(SplashSideEffect.AutoLogin)
                }.launchIn(viewModelScope)
        }
    }

    private fun refreshToken(token: String) {
        refreshCount++
        authRepository.requestRefresh(
            refreshToken = token
        ).catch {
            sendSideEffect(SplashSideEffect.NeedLogin)
        }.onEach {
            tokenRepository.saveToken(it)
            isValidationToken()
        }.launchIn(viewModelScope)
    }

    private fun checkIsFirstEntryOnBoarding() {
        launchRepository.getIsFirstEntryOnBoarding()
            .onEach { _isFirstEntryOnBoarding.value = it }
            .launchIn(viewModelScope)
    }
}
