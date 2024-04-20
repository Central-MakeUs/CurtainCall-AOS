package com.cmc.curtaincall.feature.home

import com.cmc.curtaincall.core.base.BaseState
import com.cmc.curtaincall.domain.model.member.MemberInfoModel
import com.cmc.curtaincall.domain.model.member.MyRecruitmentModel
import com.cmc.curtaincall.domain.model.show.CostEffectiveShowModel
import com.cmc.curtaincall.domain.model.show.LiveTalkShowModel
import com.cmc.curtaincall.domain.model.show.ShowInfoModel
import com.cmc.curtaincall.domain.model.show.ShowRankModel
import com.cmc.curtaincall.domain.model.show.ShowRecommendationModel
import com.cmc.curtaincall.domain.model.show.ShowSearchWordModel

data class HomeState(
    val isShowTooltip: Boolean = false,
    val nickname: String = "",
    val memberInfo: MemberInfoModel = MemberInfoModel(),
    val myRecruitments: List<MyRecruitmentModel> = listOf(),
    val liveTalks: List<LiveTalkShowModel> = listOf(),
    val showRanks: List<ShowRankModel> = listOf(),
    val openShowInfos: List<ShowInfoModel> = listOf(),
    val endShowInfos: List<ShowInfoModel> = listOf(),
    val searchWords: List<ShowSearchWordModel> = listOf(),
    val showRecommendations: List<ShowRecommendationModel> = listOf(),
    val costEffectiveShows: List<CostEffectiveShowModel> = listOf()
) : BaseState
