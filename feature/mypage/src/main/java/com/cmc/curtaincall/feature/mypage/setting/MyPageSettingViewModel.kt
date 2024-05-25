package com.cmc.curtaincall.feature.mypage.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cmc.curtaincall.domain.model.auth.LoginResultModel
import com.cmc.curtaincall.domain.repository.ChattingRepository
import com.cmc.curtaincall.domain.repository.MemberRepository
import com.cmc.curtaincall.domain.repository.TokenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.chat.android.client.ChatClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MyPageSettingViewModel @Inject constructor(
    private val tokenRepository: TokenRepository,
    private val chattingRepository: ChattingRepository,
    private val memberRepository: MemberRepository
) : ViewModel() {

    private var _logoutComplete = MutableStateFlow(false)
    val logoutComplete = _logoutComplete.asStateFlow()

    fun memberLogout(chatClient: ChatClient) {
        if (chatClient.clientState.isInitialized) {
            Timber.d("chatClient is disconnectting")
            chatClient.disconnect(flushPersistence = false).enqueue {
                if (it.isSuccess) {
                    Timber.d("chatClient disconnect user")
                    viewModelScope.launch {
                        tokenRepository.saveToken(LoginResultModel())
                        _logoutComplete.value = true
                    }
                }
            }
        }
    }

    private fun disconnectChatClient(chatClient: ChatClient) {
    }
}
