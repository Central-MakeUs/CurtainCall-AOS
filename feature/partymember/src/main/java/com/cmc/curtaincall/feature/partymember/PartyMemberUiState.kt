package com.cmc.curtaincall.feature.partymember

import androidx.paging.PagingData
import com.cmc.curtaincall.core.base.BaseState
import com.cmc.curtaincall.domain.model.party.PartyModel
import com.cmc.curtaincall.domain.model.party.PartySearchWordModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class PartyMemberUiState(
    val isShowTooltip: Boolean = false,
    val partyModels: Flow<PagingData<PartyModel>> = flowOf(),
    val partySearchWords: List<PartySearchWordModel> = listOf()
) : BaseState
