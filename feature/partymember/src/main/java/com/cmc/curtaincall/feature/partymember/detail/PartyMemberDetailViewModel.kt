package com.cmc.curtaincall.feature.partymember.detail

import androidx.lifecycle.viewModelScope
import com.cmc.curtaincall.core.base.RootViewModel
import com.cmc.curtaincall.domain.model.party.PartyDetailModel
import com.cmc.curtaincall.domain.repository.PartyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class PartyMemberDetailViewModel @Inject constructor(
    private val partyRepository: PartyRepository
) : RootViewModel<Nothing>() {

    private val _partyDetailModel = MutableStateFlow<PartyDetailModel>(PartyDetailModel())
    val partyDetailModel = _partyDetailModel.asStateFlow()

    fun requestPartyDetail(partyId: Int) {
        partyRepository.requestPartyDetail(
            partyId = partyId
        ).onEach {
            _partyDetailModel.value = it
        }.launchIn(viewModelScope)
    }
}
