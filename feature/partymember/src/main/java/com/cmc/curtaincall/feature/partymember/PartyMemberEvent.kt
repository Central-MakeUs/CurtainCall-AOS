package com.cmc.curtaincall.feature.partymember

import androidx.paging.PagingData
import com.cmc.curtaincall.core.base.BaseEvent
import com.cmc.curtaincall.domain.model.party.PartyModel
import com.cmc.curtaincall.domain.model.party.PartySearchWordModel
import kotlinx.coroutines.flow.Flow

sealed class PartyMemberEvent : BaseEvent {
    data class FetchPartyMember(
        val partyModels: Flow<PagingData<PartyModel>>
    ) : PartyMemberEvent()

    object ShowTooltip : PartyMemberEvent()

    object HideTooltip : PartyMemberEvent()

    data class QueryPartySearchWord(
        val partySearchWords: List<PartySearchWordModel>
    ) : PartyMemberEvent()
}
