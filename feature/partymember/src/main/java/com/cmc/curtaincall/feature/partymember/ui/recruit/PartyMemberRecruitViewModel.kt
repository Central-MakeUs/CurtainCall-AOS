package com.cmc.curtaincall.feature.partymember.ui.recruit

import com.cmc.curtaincall.core.base.BaseViewModel
import com.cmc.curtaincall.domain.enums.ShowGenreType
import com.cmc.curtaincall.domain.enums.ShowSortType
import com.cmc.curtaincall.domain.model.show.ShowTimeModel
import com.cmc.curtaincall.domain.repository.PartyRepository
import com.cmc.curtaincall.domain.repository.ShowRepository
import com.kizitonwose.calendar.core.CalendarDay
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class PartyMemberRecruitViewModel @Inject constructor(
    private val showRepository: ShowRepository,
    private val partyRepository: PartyRepository
) : BaseViewModel<PartyMemberRecruitUiState, PartyMemberRecruitEvent, Nothing>(
    initialState = PartyMemberRecruitUiState()
) {
    init {
        fetchShowInfoModels()
    }

    override fun reduceState(currentState: PartyMemberRecruitUiState, event: PartyMemberRecruitEvent): PartyMemberRecruitUiState =
        when (event) {
            is PartyMemberRecruitEvent.MovePhrase -> {
                currentState.copy(phrase = event.phrase)
            }

            is PartyMemberRecruitEvent.ChangeShowGenreType -> {
                currentState.copy(genreType = event.genreType)
            }

            is PartyMemberRecruitEvent.ChangeShowSortType -> {
                currentState.copy(sortType = event.sortType)
            }

            is PartyMemberRecruitEvent.GetShowInfoModels -> {
                currentState.copy(showInfoModels = event.showInfoModels)
            }

            PartyMemberRecruitEvent.ShowTooltip -> {
                currentState.copy(isShowTooltip = true)
            }

            PartyMemberRecruitEvent.HideTooltip -> {
                currentState.copy(isShowTooltip = false)
            }

            is PartyMemberRecruitEvent.SelectShowPoster -> {
                currentState.copy(
                    showId = event.showId,
                    showStartDate = event.showStartDate,
                    showEndDate = event.showEndDate,
                    showTimes = event.showTimes
                )
            }

            PartyMemberRecruitEvent.UnSelectShowPoster -> {
                currentState.copy(
                    showId = null,
                    showStartDate = "",
                    showEndDate = "",
                    showTimes = listOf()
                )
            }

            is PartyMemberRecruitEvent.SelectShowDate -> {
                currentState.copy(
                    selectShowDate = event.selectShowDate
                )
            }

            is PartyMemberRecruitEvent.SelectShowTime -> {
                currentState.copy(
                    selectShowTime = event.selectShowTime
                )
            }

            PartyMemberRecruitEvent.ClearSelection -> {
                currentState.copy(
                    selectShowDate = "",
                    selectShowTime = "",
                    selectMemberNumber = 2
                )
            }

            is PartyMemberRecruitEvent.ChangeMemberNumber -> {
                currentState.copy(
                    selectMemberNumber = event.memberNumber
                )
            }

            else -> {
                currentState
            }
        }

    fun changeShowGenreType(genreType: ShowGenreType) {
        sendAction(
            PartyMemberRecruitEvent.ChangeShowGenreType(
                genreType = genreType
            )
        )
        fetchShowInfoModels()
    }

    fun changeShowSortType(sortType: ShowSortType) {
        sendAction(
            PartyMemberRecruitEvent.ChangeShowSortType(
                sortType = sortType
            )
        )
        fetchShowInfoModels()
    }

    fun showTooltip() {
        sendAction(PartyMemberRecruitEvent.ShowTooltip)
    }

    fun hideTooltip() {
        sendAction(PartyMemberRecruitEvent.HideTooltip)
    }

    private fun fetchShowInfoModels() {
        sendAction(
            PartyMemberRecruitEvent.GetShowInfoModels(
                showInfoModels = showRepository.fetchShowList(
                    genre = uiState.value.genreType.name,
                    sort = uiState.value.sortType.code
                )
            )
        )
    }

    fun selectShowPoster(
        showId: String,
        showStartDate: String,
        showEndDate: String,
        showTimes: List<ShowTimeModel>
    ) {
        sendAction(
            if (uiState.value.showId == null) {
                PartyMemberRecruitEvent.SelectShowPoster(
                    showId = showId,
                    showStartDate = showStartDate,
                    showEndDate = showEndDate,
                    showTimes = showTimes
                )
            } else {
                if (uiState.value.showId == showId) {
                    PartyMemberRecruitEvent.UnSelectShowPoster
                } else {
                    PartyMemberRecruitEvent.SelectShowPoster(
                        showId = showId,
                        showStartDate = showStartDate,
                        showEndDate = showEndDate,
                        showTimes = showTimes
                    )
                }
            }
        )
    }

    fun movePhrase(phrase: Int) {
        sendAction(
            PartyMemberRecruitEvent.MovePhrase(
                phrase = phrase
            )
        )
    }

    fun selectShowDate(calendarDay: CalendarDay) {
        sendAction(
            PartyMemberRecruitEvent.SelectShowDate(
                selectShowDate = calendarDay.date.format(DateTimeFormatter.ISO_DATE)
            )
        )
    }

    fun selectShowTime(selectShowTime: String) {
        sendAction(
            PartyMemberRecruitEvent.SelectShowTime(
                selectShowTime = selectShowTime
            )
        )
    }

    fun clearSelection() {
        sendAction(PartyMemberRecruitEvent.ClearSelection)
    }

    fun changeMemberNumber(memberNumber: Int) {
        sendAction(
            PartyMemberRecruitEvent.ChangeMemberNumber(
                memberNumber = memberNumber
            )
        )
    }
}
