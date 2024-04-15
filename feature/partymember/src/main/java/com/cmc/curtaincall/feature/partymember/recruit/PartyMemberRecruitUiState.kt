package com.cmc.curtaincall.feature.partymember.recruit

import androidx.paging.PagingData
import com.cmc.curtaincall.core.base.BaseState
import com.cmc.curtaincall.domain.enums.ShowGenreType
import com.cmc.curtaincall.domain.enums.ShowSortType
import com.cmc.curtaincall.domain.model.show.ShowInfoModel
import com.cmc.curtaincall.domain.model.show.ShowRankModel
import com.cmc.curtaincall.domain.model.show.ShowTimeModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class PartyMemberRecruitUiState(
    val phrase: Int = 1,
    val sortType: ShowSortType = ShowSortType.POPULAR,
    val genreType: ShowGenreType = ShowGenreType.PLAY,
    val popularShowRankModels: List<ShowRankModel> = listOf(),
    val showInfoModels: Flow<PagingData<ShowInfoModel>> = flowOf(),
    val isShowTooltip: Boolean = false,
    val showId: String? = null,
    val showStartDate: String = "",
    val showEndDate: String = "",
    val showTimes: List<ShowTimeModel> = listOf(),
    val selectShowDate: String = "",
    val selectShowTime: String = "",
    val selectMemberNumber: Int = 2,
    val title: String = "",
    val content: String = ""
) : BaseState
