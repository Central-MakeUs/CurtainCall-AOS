package com.cmc.curtaincall.feature.partymember

import androidx.paging.PagingData
import com.cmc.curtaincall.common.designsystem.component.card.PartyType
import com.cmc.curtaincall.core.base.BaseEvent
import com.cmc.curtaincall.domain.model.party.PartyModel
import kotlinx.coroutines.flow.Flow

sealed class PartyMemberEvent : BaseEvent {

    data class ChangePartyType(
        val partyType: PartyType
    ) : PartyMemberEvent()

    data class ChangeActiveSearch(
        val isActiveSearch: Boolean
    ) : PartyMemberEvent()

    data class ChangeDoneSearch(
        val isDoneSearch: Boolean
    ) : PartyMemberEvent()

    data class SetQueryString(
        val queryString: String
    ) : PartyMemberEvent()

    data class SearchPartyList(
        val partySearchItems: Flow<PagingData<PartyModel>>
    ) : PartyMemberEvent()

    data class LoadWatchingItems(
        val watchingItems: Flow<PagingData<PartyModel>>
    ) : PartyMemberEvent()

    data class LoadFoodCafeItems(
        val foodCafeItems: Flow<PagingData<PartyModel>>
    ) : PartyMemberEvent()

    data class LoadEtcItems(
        val etcItems: Flow<PagingData<PartyModel>>
    ) : PartyMemberEvent()
}
