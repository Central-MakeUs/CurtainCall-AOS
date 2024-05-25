package com.cmc.curtaincall.feature.auth.signup.input

import androidx.lifecycle.viewModelScope
import com.cmc.curtaincall.core.base.RootViewModel
import com.cmc.curtaincall.domain.repository.ChattingRepository
import com.cmc.curtaincall.domain.repository.MemberRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.models.User
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

enum class ValidationCheckState {
    None, Validate, Duplicate
}

@HiltViewModel
class SignUpInputViewModel @Inject constructor(
    private val memberRepository: MemberRepository,
    private val chattingRepository: ChattingRepository
) : RootViewModel<Nothing>() {

    private val _validationCheckState = MutableStateFlow(ValidationCheckState.None)
    val validationCheckState = _validationCheckState.asStateFlow()

    private val _user = MutableSharedFlow<User>()
    val user = _user.asSharedFlow()

    private val _isComplete = MutableSharedFlow<Boolean>()
    val isComplete = _isComplete.asSharedFlow()

    fun clearValidationState() {
        _validationCheckState.value = ValidationCheckState.None
    }

    fun checkDuplicateNickname(nickname: String) {
        memberRepository.checkDuplicateNickname(nickname)
            .onEach { isDuplicate ->
                _validationCheckState.value = if (isDuplicate) {
                    ValidationCheckState.Duplicate
                } else {
                    ValidationCheckState.Validate
                }
            }.launchIn(viewModelScope)
    }

    fun createMember(nickname: String) {
        memberRepository.createMember(nickname)
            .onEach { memberId ->
                memberRepository.saveMemberId(memberId)
                memberRepository.saveMemberNickname(nickname)
                // _isComplete.emit(true)
                _user.emit(
                    User(
                        id = memberId.toString(),
                        name = nickname
                    )
                )
            }.launchIn(viewModelScope)
    }

    fun connectChattingClient(
        chatClient: ChatClient,
        user: User
    ) {
        if (chatClient.clientState.isInitialized) {
            viewModelScope.launch {
                _isComplete.emit(true)
            }
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
                    _isComplete.emit(true)
                }.launchIn(viewModelScope)
        }
    }
}
