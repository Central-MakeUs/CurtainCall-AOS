package com.cmc.curtaincall.feature.mypage.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cmc.curtaincall.domain.model.auth.LoginResultModel
import com.cmc.curtaincall.domain.repository.MemberRepository
import com.cmc.curtaincall.domain.repository.TokenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

enum class DeleteReason(val value: String) {
    RECORD_DELETION("기록을 삭제하기 위해"),
    INCONVENIENCE_FREQUENT_ERROR("이용이 불편하고 장애가 잦아서"),
    BETTER_OTHER_SERVICE("타 서비스가 더 좋아서"),
    LOW_USAGE_FREQUENCY("사용빈도가 낮아서"),
    NOT_USEFUL("앱 기능이 유용하지 않아서"),
    ETC("기타"),
    NONE("")
}

@HiltViewModel
class MyPageDeleteMemberViewModel @Inject constructor(
    private val tokenRepository: TokenRepository,
    private val memberRepository: MemberRepository
) : ViewModel() {
    private var _deleteReason = MutableStateFlow<DeleteReason>(DeleteReason.NONE)
    val deleteReason = _deleteReason.asStateFlow()

    private var _content = MutableStateFlow("")
    val content = _content.asStateFlow()

    private var _deleteComplete = MutableSharedFlow<Boolean>()
    val deleteComplete = _deleteComplete.asSharedFlow()

    fun changeDeleteReason(deleteReason: DeleteReason) {
        _deleteReason.value = deleteReason
        if (deleteReason != DeleteReason.ETC) _content.value = ""
    }

    fun changeContent(content: String) {
        _content.value = content
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun deleteMember() {
        tokenRepository.getAccessToken()
            .flatMapLatest { accessToken ->
                memberRepository.deleteMember(
                    authorization = accessToken,
                    reason = deleteReason.value.name,
                    content = content.value
                )
            }.onEach { check ->
                _deleteComplete.emit(check)
                if (check) tokenRepository.saveToken(LoginResultModel())
            }.launchIn(viewModelScope)
    }
}
