package com.cmc.curtaincall.feature.auth.login

import androidx.lifecycle.viewModelScope
import com.cmc.curtaincall.core.base.RootViewModel
import com.cmc.curtaincall.domain.repository.AuthRepository
import com.cmc.curtaincall.domain.repository.ChattingRepository
import com.cmc.curtaincall.domain.repository.MemberRepository
import com.cmc.curtaincall.domain.repository.TokenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.models.User
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val tokenRepository: TokenRepository,
    private val memberRepository: MemberRepository,
    private val chattingRepository: ChattingRepository
) : RootViewModel<LoginSideEffect>() {

    private val _user = MutableSharedFlow<User>()
    val user = _user.asSharedFlow()

    fun fetchLogin(
        provider: String,
        token: String
    ) {
        authRepository.requestLogin(provider, token)
            .onEach { resultModel ->
                tokenRepository.saveToken(resultModel)
                resultModel.memberId?.let {
                    memberRepository.saveMemberId(it)
                    getCurrentUser(it)
                } ?: kotlin.run {
                    sendSideEffect(LoginSideEffect.SuccessLogin)
                }
            }.launchIn(viewModelScope)
    }

    private fun getCurrentUser(memberId: Int) {
        memberRepository.requestMemberInfo(memberId)
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

    fun connectChattingClient(
        chatClient: ChatClient,
        user: User
    ) {
        if (chatClient.clientState.isInitialized) {
            sendSideEffect(LoginSideEffect.ExistMember)
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
                    sendSideEffect(LoginSideEffect.ExistMember)
                }.launchIn(viewModelScope)
        }
    }
}
