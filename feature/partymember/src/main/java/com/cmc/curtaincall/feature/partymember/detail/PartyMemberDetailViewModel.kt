package com.cmc.curtaincall.feature.partymember.detail

import androidx.lifecycle.viewModelScope
import com.cmc.curtaincall.core.base.RootViewModel
import com.cmc.curtaincall.domain.model.party.PartyDetailModel
import com.cmc.curtaincall.domain.repository.MemberRepository
import com.cmc.curtaincall.domain.repository.PartyRepository
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

@HiltViewModel
class PartyMemberDetailViewModel @Inject constructor(
    private val partyRepository: PartyRepository,
    private val memberRepository: MemberRepository
) : RootViewModel<Nothing>() {

    private val _partyDetailModel = MutableStateFlow<PartyDetailModel>(PartyDetailModel())
    val partyDetailModel = _partyDetailModel.asStateFlow()

    private val _memberID = MutableStateFlow(Int.MIN_VALUE)
    val memberID = _memberID.asStateFlow()

    private val _isMyWriting = MutableStateFlow(false)
    val isMyWriting = _isMyWriting.asStateFlow()

    private val _isParticipated = MutableStateFlow(false)
    val isParticipated = _isParticipated.asStateFlow()

    private val _isSuccessDelete = MutableSharedFlow<Boolean>()
    val isSuccessDelete = _isSuccessDelete.asSharedFlow()

    init {
        checkMemberId()
    }

    private fun checkMemberId() {
        memberRepository.getMemberId()
            .onEach { _memberID.value = it }
            .launchIn(viewModelScope)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun requestPartyDetail(partyId: Int) {
        partyRepository.requestPartyDetail(
            partyId = partyId
        ).onEach {
            _partyDetailModel.value = it
            _isMyWriting.value = it.creatorId == memberID.value
        }.flatMapLatest {
            partyRepository.checkParty(it.id)
        }.onEach {
            _isParticipated.value = it.participated
        }.launchIn(viewModelScope)
    }

    fun participateParty(partyId: Int) {
        partyRepository.participateParty(partyId)
            .onEach { check ->
                if (check) {
                    requestPartyDetail(partyId)
                }
            }.launchIn(viewModelScope)
    }

    fun cancelParty(partyId: Int) {
        partyRepository.cancelParty(partyId)
            .onEach { check ->
                if (check) {
                    requestPartyDetail(partyId)
                }
            }.launchIn(viewModelScope)
    }

    fun deleteParty(partyId: Int) {
        partyRepository.deleteParty(partyId)
            .onEach { check ->
                _isSuccessDelete.emit(check)
            }.launchIn(viewModelScope)
    }
}
