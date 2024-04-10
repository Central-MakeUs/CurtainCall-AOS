package com.cmc.curtaincall.feature.home.report

import com.cmc.curtaincall.core.base.BaseEvent

sealed class HomeReportEvent : BaseEvent {

    data class MovePhrase(
        val phrase: Int
    ) : HomeReportEvent()

    data class SelectReportReason(
        val reportReason: HomeReportReason
    ) : HomeReportEvent()

    data class SetReportContent(
        val content: String
    ) : HomeReportEvent()
}
