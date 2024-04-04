package com.cmc.curtaincall.feature.partymember.recruit

import androidx.paging.PagingData
import com.cmc.curtaincall.core.base.BaseEvent
import com.cmc.curtaincall.domain.enums.ShowGenreType
import com.cmc.curtaincall.domain.enums.ShowSortType
import com.cmc.curtaincall.domain.model.show.ShowInfoModel
import com.cmc.curtaincall.domain.model.show.ShowTimeModel
import kotlinx.coroutines.flow.Flow

sealed class PartyMemberRecruitEvent : BaseEvent {
    data class MovePhrase(
        val phrase: Int
    ) : PartyMemberRecruitEvent()

    data class ChangeShowGenreType(
        val genreType: ShowGenreType
    ) : PartyMemberRecruitEvent()

    data class ChangeShowSortType(
        val sortType: ShowSortType
    ) : PartyMemberRecruitEvent()

    data class GetShowInfoModels(
        val showInfoModels: Flow<PagingData<ShowInfoModel>>
    ) : PartyMemberRecruitEvent()

    object ClearShowInfoModels : PartyMemberRecruitEvent()

    object ShowTooltip : PartyMemberRecruitEvent()

    object HideTooltip : PartyMemberRecruitEvent()

    data class SelectShowPoster(
        val showId: String,
        val showStartDate: String,
        val showEndDate: String,
        val showTimes: List<ShowTimeModel>
    ) : PartyMemberRecruitEvent()

    object UnSelectShowPoster : PartyMemberRecruitEvent()

    data class SelectShowDate(
        val selectShowDate: String
    ) : PartyMemberRecruitEvent()

    data class SelectShowTime(
        val selectShowTime: String
    ) : PartyMemberRecruitEvent()

    object ClearSelection : PartyMemberRecruitEvent()

    data class ChangeMemberNumber(
        val memberNumber: Int
    ) : PartyMemberRecruitEvent()

    data class WritePartyTitle(
        val title: String
    ) : PartyMemberRecruitEvent()

    data class WritePartyContent(
        val content: String
    ) : PartyMemberRecruitEvent()
}
