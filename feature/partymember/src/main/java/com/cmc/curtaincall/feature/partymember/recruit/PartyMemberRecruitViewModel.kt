package com.cmc.curtaincall.feature.partymember.recruit

import androidx.lifecycle.viewModelScope
import com.cmc.curtaincall.common.designsystem.component.appbars.SearchAppBarState
import com.cmc.curtaincall.core.base.BaseViewModel
import com.cmc.curtaincall.domain.enums.ShowGenreType
import com.cmc.curtaincall.domain.enums.ShowSortType
import com.cmc.curtaincall.domain.model.show.ShowTimeModel
import com.cmc.curtaincall.domain.repository.PartyRepository
import com.cmc.curtaincall.domain.repository.ShowRepository
import com.kizitonwose.calendar.core.CalendarDay
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class PartyMemberRecruitViewModel @Inject constructor(
    private val showRepository: ShowRepository,
    private val partyRepository: PartyRepository
) : BaseViewModel<PartyMemberRecruitUiState, PartyMemberRecruitEvent, PartyMemberRecruitSideEffect>(
    initialState = PartyMemberRecruitUiState()
) {
    private var _searchAppBarState = MutableStateFlow(SearchAppBarState())
    val searchAppBarState = _searchAppBarState.asStateFlow()

    init {
        fetchShowInfoModels()
        _searchAppBarState.value = _searchAppBarState.value.copy(
            onSearch = {
                _searchAppBarState.value.isSearchMode.value = true
                _searchAppBarState.value.isDoneSearch.value = false
                clearShowList()
            },
            onClear = {
                _searchAppBarState.value.searchText.value = ""
                _searchAppBarState.value.isDoneSearch.value = false
                clearShowList()
            },
            onCancel = {
                _searchAppBarState.value.searchText.value = ""
                _searchAppBarState.value.isSearchMode.value = false
                _searchAppBarState.value.isDoneSearch.value = false
                fetchShowInfoModels()
            },
            onDone = {
                _searchAppBarState.value.isDoneSearch.value = true
                searchShowList(_searchAppBarState.value.searchText.value)
            }
        )
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

            PartyMemberRecruitEvent.ClearShowInfoModels -> {
                currentState.copy(showInfoModels = flowOf())
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
                    selectMemberNumber = 2,
                    title = "",
                    content = ""
                )
            }

            is PartyMemberRecruitEvent.ChangeMemberNumber -> {
                currentState.copy(
                    selectMemberNumber = event.memberNumber
                )
            }

            is PartyMemberRecruitEvent.WritePartyTitle -> {
                currentState.copy(
                    title = event.title
                )
            }

            is PartyMemberRecruitEvent.WritePartyContent -> {
                currentState.copy(
                    content = event.content
                )
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

    fun changePartyTitle(title: String) {
        sendAction(
            PartyMemberRecruitEvent.WritePartyTitle(
                title = title
            )
        )
    }

    fun changePartyContent(content: String) {
        sendAction(
            PartyMemberRecruitEvent.WritePartyContent(
                content = content
            )
        )
    }

    fun createParty() {
        val state = uiState.value
        partyRepository.createParty(
            showId = state.showId,
            showAt = String.format("%sT%s", state.selectShowDate, state.selectShowTime),
            title = state.title,
            content = state.content,
            maxMemberNum = state.selectMemberNumber
        ).catch {
            sendSideEffect(PartyMemberRecruitSideEffect.FailedCreateParty)
        }.onEach {
            sendSideEffect(PartyMemberRecruitSideEffect.CreateParty)
        }.launchIn(viewModelScope)
    }

    private fun clearShowList() {
        sendAction(PartyMemberRecruitEvent.ClearShowInfoModels)
    }

    private fun searchShowList(query: String) {
        sendAction(
            PartyMemberRecruitEvent.GetShowInfoModels(
                showInfoModels = showRepository.fetchSearchShowList(query.trim())
            )
        )
    }
}
