package com.cmc.curtaincall.feature.partymember

import androidx.paging.PagingData
import com.cmc.curtaincall.common.designsystem.component.card.PartyType
import com.cmc.curtaincall.core.base.BaseState
import com.cmc.curtaincall.domain.model.party.PartyModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class PartyMemberUiState(
    val partyType: PartyType = PartyType.PERFORMANCE,
    val isActiveSearch: Boolean = false,
    val isDoneSearch: Boolean = false,
    val queryString: String = "",
    val partySearchItems: Flow<PagingData<PartyModel>> = flowOf(),
    val watchingItems: Flow<PagingData<PartyModel>> = flowOf(),
    val foodCafeItems: Flow<PagingData<PartyModel>> = flowOf(),
    val etcItems: Flow<PagingData<PartyModel>> = flowOf()
) : BaseState
