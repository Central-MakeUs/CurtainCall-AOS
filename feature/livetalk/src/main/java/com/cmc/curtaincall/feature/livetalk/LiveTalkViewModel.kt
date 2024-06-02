package com.cmc.curtaincall.feature.livetalk

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cmc.curtaincall.domain.repository.MemberRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class LiveTalkViewModel @Inject constructor(
    private val memberRepository: MemberRepository
) : ViewModel() {

    private val _memberName = MutableStateFlow("")
    val memberName = _memberName.asStateFlow()

    init {
        getMemberName()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun getMemberName() {
        memberRepository.getMemberId()
            .flatMapLatest {
                memberRepository.requestMemberInfo(it)
            }.onEach {
                _memberName.value = it.nickname
            }.launchIn(viewModelScope)
    }
}
