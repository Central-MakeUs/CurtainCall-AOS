package com.cmc.curtaincall.feature.show.search

import androidx.paging.PagingData
import com.cmc.curtaincall.core.base.BaseEvent
import com.cmc.curtaincall.domain.enums.ShowGenreType
import com.cmc.curtaincall.domain.enums.ShowSortType
import com.cmc.curtaincall.domain.model.show.ShowInfoModel
import com.cmc.curtaincall.domain.model.show.ShowRankModel
import com.cmc.curtaincall.domain.model.show.ShowSearchWordModel
import kotlinx.coroutines.flow.Flow

sealed class ShowSearchEvent : BaseEvent {
    object ShowTooltip : ShowSearchEvent()

    object HideTooltip : ShowSearchEvent()

    data class SelectSortType(
        val sortType: ShowSortType
    ) : ShowSearchEvent()

    data class SelectGenreType(
        val genreType: ShowGenreType
    ) : ShowSearchEvent()

    data class QueryShowSearchWords(
        val showSearchWords: List<ShowSearchWordModel>
    ) : ShowSearchEvent()

    data class GetShowInfoModels(
        val showInfoModels: Flow<PagingData<ShowInfoModel>>
    ) : ShowSearchEvent()

    data class GetPopularShowRankModels(
        val showRankModels: List<ShowRankModel>
    ) : ShowSearchEvent()
}
