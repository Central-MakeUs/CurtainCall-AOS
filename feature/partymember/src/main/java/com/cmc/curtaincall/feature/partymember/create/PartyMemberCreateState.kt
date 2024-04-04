package com.cmc.curtaincall.feature.partymember.create

import androidx.paging.PagingData
import com.cmc.curtaincall.core.base.BaseState
import com.cmc.curtaincall.domain.enums.ShowSortType
import com.cmc.curtaincall.domain.model.show.ShowDetailModel
import com.cmc.curtaincall.domain.model.show.ShowInfoModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class PartyMemberCreateState(
    val showDetailModel: ShowDetailModel = ShowDetailModel(),
    val sortType: ShowSortType = ShowSortType.REVIEW_GRADE,
    val showId: String? = null,
    val showAt: String? = null,
    val title: String = "",
    val content: String = "",
    val maxMemberNum: Int = 0,
    val category: String = "",
    val playItems: Flow<PagingData<ShowInfoModel>> = flowOf(),
    val musicalItems: Flow<PagingData<ShowInfoModel>> = flowOf()
) : BaseState
