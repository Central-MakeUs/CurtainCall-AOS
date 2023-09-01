package com.cmc.curtaincall.feature.mypage

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.cmc.curtaincall.common.design.component.content.card.PartyType
import com.cmc.curtaincall.core.base.BaseViewModel
import com.cmc.curtaincall.domain.repository.FavoriteRepository
import com.cmc.curtaincall.domain.repository.MemberRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val memberRepository: MemberRepository,
    private val favoriteRepository: FavoriteRepository
) : BaseViewModel<MyPageUiState, MyPageEvent, Nothing>(
    initialState = MyPageUiState()
) {

    private var _memberId = MutableStateFlow(0)
    val memberId: StateFlow<Int> = _memberId.asStateFlow()

    private var _myRecruitmentPartyType = MutableStateFlow(PartyType.PERFORMANCE)
    val myRecruitmentPartType: StateFlow<PartyType> = _myRecruitmentPartyType.asStateFlow()

    private var _myParticipationPartyType = MutableStateFlow(PartyType.PERFORMANCE)
    val myParticipationPartyType: StateFlow<PartyType> = _myParticipationPartyType.asStateFlow()

    var watchingRecruitmentItems = memberRepository.fetchMyRecruitments(
        memberId = memberId.value,
        category = PartyType.PERFORMANCE.category
    ).cachedIn(viewModelScope)

    var foodRecruitmentItems = memberRepository.fetchMyRecruitments(
        memberId = memberId.value,
        category = PartyType.MEAL.category
    ).cachedIn(viewModelScope)

    var etcRecruitmentItems = memberRepository.fetchMyRecruitments(
        memberId = memberId.value,
        category = PartyType.ETC.category
    ).cachedIn(viewModelScope)

    var watchingParticipationItems = memberRepository.fetchMyParticipations(
        memberId = memberId.value,
        category = PartyType.PERFORMANCE.category
    ).cachedIn(viewModelScope)

    var foodParticipationItems = memberRepository.fetchMyParticipations(
        memberId = memberId.value,
        category = PartyType.MEAL.category
    ).cachedIn(viewModelScope)

    var etcParticipationItems = memberRepository.fetchMyParticipations(
        memberId = memberId.value,
        category = PartyType.ETC.category
    ).cachedIn(viewModelScope)

    var favoriteShowItems = favoriteRepository.fetchFavoriteShows(
        memberId = memberId.value
    ).cachedIn(viewModelScope)

    override fun reduceState(currentState: MyPageUiState, event: MyPageEvent): MyPageUiState =
        when (event) {
            is MyPageEvent.LoadMemberInfo -> {
                currentState.copy(memberInfoModel = event.memberInfoModel)
            }
        }

    fun getMemberId() {
        memberRepository.getMemberId()
            .onEach {
                _memberId.value = it
                watchingRecruitmentItems = memberRepository.fetchMyRecruitments(
                    memberId = it,
                    category = PartyType.PERFORMANCE.category
                ).cachedIn(viewModelScope)
                foodRecruitmentItems = memberRepository.fetchMyRecruitments(
                    memberId = it,
                    category = PartyType.MEAL.category
                ).cachedIn(viewModelScope)
                etcRecruitmentItems = memberRepository.fetchMyRecruitments(
                    memberId = it,
                    category = PartyType.ETC.category
                ).cachedIn(viewModelScope)
                watchingParticipationItems = memberRepository.fetchMyParticipations(
                    memberId = it,
                    category = PartyType.PERFORMANCE.category
                ).cachedIn(viewModelScope)
                foodParticipationItems = memberRepository.fetchMyParticipations(
                    memberId = it,
                    category = PartyType.MEAL.category
                ).cachedIn(viewModelScope)
                etcParticipationItems = memberRepository.fetchMyParticipations(
                    memberId = it,
                    category = PartyType.ETC.category
                ).cachedIn(viewModelScope)
            }.flatMapLatest { memberRepository.requestMemberInfo(it) }
            .onEach { sendAction(MyPageEvent.LoadMemberInfo(it)) }
            .launchIn(viewModelScope)
    }

    fun setRecruitmentPartyType(partyType: PartyType) {
        _myRecruitmentPartyType.value = partyType
    }

    fun setParticipationPartyType(partyType: PartyType) {
        _myParticipationPartyType.value = partyType
    }
}
