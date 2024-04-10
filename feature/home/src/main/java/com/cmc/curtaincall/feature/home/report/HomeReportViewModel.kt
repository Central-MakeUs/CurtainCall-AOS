package com.cmc.curtaincall.feature.home.report

import androidx.lifecycle.viewModelScope
import com.cmc.curtaincall.core.base.BaseViewModel
import com.cmc.curtaincall.domain.repository.ReportRepository
import com.cmc.curtaincall.domain.type.ReportType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class HomeReportViewModel @Inject constructor(
    private val reportRepository: ReportRepository
) : BaseViewModel<HomeReportUiState, HomeReportEvent, Nothing>(
    initialState = HomeReportUiState()
) {
    private val _reportSuccess = MutableSharedFlow<Boolean>()
    val reportSuccess = _reportSuccess.asSharedFlow()
    override fun reduceState(currentState: HomeReportUiState, event: HomeReportEvent): HomeReportUiState =
        when (event) {
            is HomeReportEvent.MovePhrase -> currentState.copy(
                phrase = event.phrase
            )

            is HomeReportEvent.SetReportContent -> currentState.copy(
                content = event.content
            )

            is HomeReportEvent.SelectReportReason -> currentState.copy(
                reportReason = event.reportReason
            )
        }

    fun movePhrase(phrase: Int) {
        sendAction(
            HomeReportEvent.MovePhrase(
                phrase = phrase
            )
        )
    }

    fun changeReportReason(reportReason: HomeReportReason) {
        sendAction(HomeReportEvent.SelectReportReason(reportReason))
    }

    fun setContent(content: String) {
        sendAction(
            HomeReportEvent.SetReportContent(
                content = content
            )
        )
    }

    fun requestReport(
        idToReport: Int,
        type: ReportType
    ) {
        reportRepository.requestReport(
            idToReport = idToReport,
            type = type.name,
            reason = uiState.value.reportReason.name,
            content = uiState.value.content
        ).onEach { check ->
            _reportSuccess.emit(check)
        }.launchIn(viewModelScope)
    }
}
