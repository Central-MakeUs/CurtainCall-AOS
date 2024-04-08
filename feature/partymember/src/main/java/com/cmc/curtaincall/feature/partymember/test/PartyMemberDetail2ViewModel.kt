package com.cmc.curtaincall.feature.partymember.test

import androidx.lifecycle.viewModelScope
import com.cmc.curtaincall.core.base.BaseViewModel
import com.cmc.curtaincall.domain.repository.ChattingRepository
import com.cmc.curtaincall.domain.repository.MemberRepository
import com.cmc.curtaincall.domain.repository.PartyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.chat.android.client.models.User
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class PartyMemberDetail2ViewModel @Inject constructor(
    private val partyRepository: PartyRepository,
    private val chattingRepository: ChattingRepository,
    private val memberRepository: MemberRepository
) : BaseViewModel<PartyMemberDetail2State, PartyMemberDetai2lEvent, PartyMemberDetail2SideEffect>(
    initialState = PartyMemberDetail2State()
) {

    init {
        getMemberInfo()
    }

    override fun reduceState(currentState: PartyMemberDetail2State, event: PartyMemberDetai2lEvent): PartyMemberDetail2State = when (event) {
        is PartyMemberDetai2lEvent.RequestPartyDetail -> {
            currentState.copy(partyDetailModel = event.partyDetailModel)
        }

        is PartyMemberDetai2lEvent.GetMemberInfo -> {
            currentState.copy(user = event.user)
        }

        is PartyMemberDetai2lEvent.ChangeParticipation -> {
            currentState.copy(isParticipation = event.isParticipation)
        }

        else -> currentState
    }

    fun requestPartyDetail(partyId: Int) {
        partyRepository.requestPartyDetail(partyId)
            .onEach { sendAction(PartyMemberDetai2lEvent.RequestPartyDetail(it)) }
            .launchIn(viewModelScope)
    }

    fun deleteParty(partyId: Int) {
        partyRepository.deleteParty(partyId)
            .onEach { sendSideEffect(PartyMemberDetail2SideEffect.SuccessDelete) }
            .launchIn(viewModelScope)
    }

    fun participateParty(partyId: Int) {
        partyRepository.participateParty(partyId)
            .onEach { sendSideEffect(PartyMemberDetail2SideEffect.SuccessParticipation) }
            .launchIn(viewModelScope)
    }

    fun checkParty(partyId: Int) {
        partyRepository.checkParty(partyId)
            .onEach { sendAction(PartyMemberDetai2lEvent.ChangeParticipation(it.participated)) }
            .launchIn(viewModelScope)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getMemberInfo() {
        memberRepository.getMemberId().flatMapLatest {
            memberRepository.requestMemberInfo(it)
        }.onEach {
            sendAction(
                PartyMemberDetai2lEvent.GetMemberInfo(
                    user = User(
                        id = it.id.toString(),
                        name = it.nickname,
                        image = it.imageUrl.toString()
                    )
                )
            )
        }.launchIn(viewModelScope)
    }
}
