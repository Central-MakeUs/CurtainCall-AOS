package com.cmc.curtaincall.feature.mypage.party

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.cmc.curtaincall.domain.model.member.MemberInfoModel
import com.cmc.curtaincall.domain.model.member.MyParticipationModel
import com.cmc.curtaincall.domain.model.member.MyRecruitmentModel
import com.cmc.curtaincall.domain.repository.MemberRepository
import com.cmc.curtaincall.domain.repository.PartyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

enum class MyPartyType(val value: String) {
    Participation("참여"), Recruitment("모집")
}

@HiltViewModel
class MyPagePartyViewModel @Inject constructor(
    private val memberRepository: MemberRepository,
    private val partyRepository: PartyRepository
) : ViewModel() {
    private val memberID = MutableStateFlow(Int.MIN_VALUE)
    private val _memberInfo = MutableStateFlow(MemberInfoModel())
    val memberInfo = _memberInfo.asStateFlow()

    private val _myPartyType = MutableStateFlow(MyPartyType.Participation)
    val myPartyType = _myPartyType.asStateFlow()

    private val _myRecruitmentModels = MutableStateFlow<PagingData<MyRecruitmentModel>>(PagingData.empty())
    val myRecruitmentModels = _myRecruitmentModels.asStateFlow()

    private val _myParticipationModels = MutableStateFlow<PagingData<MyParticipationModel>>(PagingData.empty())
    val myParticipationModels = _myParticipationModels.asStateFlow()

    fun checkMemberID() {
        memberRepository.getMemberId()
            .onEach {
                memberID.value = it
                fetchMyParticipations(it)
                fetchMyRecruitments(it)
            }.flatMapLatest {
                memberRepository.requestMemberInfo(it)
            }.onEach {
                _memberInfo.value = it
            }.launchIn(viewModelScope)
    }

    fun selectMyPartyType(partyType: MyPartyType) {
        _myPartyType.value = partyType
    }

    private fun fetchMyRecruitments(memberId: Int) {
        memberRepository.fetchMyRecruitments(memberId)
            .cachedIn(viewModelScope)
            .onEach {
                _myRecruitmentModels.value = it
            }.launchIn(viewModelScope)
    }

    private fun fetchMyParticipations(memberId: Int) {
        memberRepository.fetchMyParticipations(memberId)
            .cachedIn(viewModelScope)
            .onEach {
                _myParticipationModels.value = it
            }.launchIn(viewModelScope)
    }

    fun cancelParty(partyId: Int) {
        partyRepository.cancelParty(partyId)
            .onEach { check ->
                if (check) {
                    fetchMyParticipations(memberID.value)
                }
            }.launchIn(viewModelScope)
    }

    fun deleteParty(partyId: Int) {
        partyRepository.deleteParty(partyId)
            .onEach { check ->
                if (check) {
                    fetchMyRecruitments(memberID.value)
                }
            }.launchIn(viewModelScope)
    }
}
