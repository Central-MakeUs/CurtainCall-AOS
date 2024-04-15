package com.cmc.curtaincall.feature.show.search

import androidx.paging.PagingData
import com.cmc.curtaincall.core.base.BaseState
import com.cmc.curtaincall.domain.enums.ShowGenreType
import com.cmc.curtaincall.domain.enums.ShowSortType
import com.cmc.curtaincall.domain.model.show.ShowInfoModel
import com.cmc.curtaincall.domain.model.show.ShowRankModel
import com.cmc.curtaincall.domain.model.show.ShowSearchWordModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class ShowSearchUiState(
    val isShowTooltip: Boolean = false,
    val sortType: ShowSortType = ShowSortType.POPULAR,
    val genreType: ShowGenreType = ShowGenreType.PLAY,
    val popularShowRankModels: List<ShowRankModel> = listOf(),
    val showInfoModels: Flow<PagingData<ShowInfoModel>> = flowOf(),
    val showSearchWords: List<ShowSearchWordModel> = listOf()
) : BaseState
