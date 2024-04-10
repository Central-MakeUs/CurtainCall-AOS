package com.cmc.curtaincall.feature.home.report

import com.cmc.curtaincall.core.base.BaseState

data class HomeReportUiState(
    val phrase: Int = 1,
    val content: String = "",
    val reportReason: HomeReportReason = HomeReportReason.NONE
) : BaseState
