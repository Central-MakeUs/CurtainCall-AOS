package com.cmc.curtaincall.feature.partymember.create

import androidx.paging.PagingData
import com.cmc.curtaincall.core.base.BaseEvent
import com.cmc.curtaincall.domain.enums.ShowSortType
import com.cmc.curtaincall.domain.model.show.ShowDetailModel
import com.cmc.curtaincall.domain.model.show.ShowInfoModel
import kotlinx.coroutines.flow.Flow

sealed class PartyMemberCreateEvent : BaseEvent {
    data class SetPartyCategory(
        val category: String
    ) : PartyMemberCreateEvent()

    data class SetShowId(
        val showId: String
    ) : PartyMemberCreateEvent()

    data class RequestShowDetail(
        val showDetailModel: ShowDetailModel
    ) : PartyMemberCreateEvent()

    data class SetPartyInfo(
        val showAt: String?,
        val maxMemberNum: Int
    ) : PartyMemberCreateEvent()

    data class SetPartyDescription(
        val title: String,
        val content: String
    ) : PartyMemberCreateEvent()

    data class SetSortType(
        val sortType: ShowSortType
    ) : PartyMemberCreateEvent()

    data class LoadPlayItems(
        val playItems: Flow<PagingData<ShowInfoModel>>
    ) : PartyMemberCreateEvent()

    data class LoadMusicalItems(
        val musicalItems: Flow<PagingData<ShowInfoModel>>
    ) : PartyMemberCreateEvent()
}
