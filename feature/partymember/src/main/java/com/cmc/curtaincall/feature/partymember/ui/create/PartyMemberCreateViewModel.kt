package com.cmc.curtaincall.feature.partymember.ui.create

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.cmc.curtaincall.common.designsystem.component.card.PartyType
import com.cmc.curtaincall.common.utility.extensions.changeShowAt
import com.cmc.curtaincall.core.base.BaseViewModel
import com.cmc.curtaincall.domain.enums.ShowSortType
import com.cmc.curtaincall.domain.repository.PartyRepository
import com.cmc.curtaincall.domain.repository.ShowRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class PartyMemberCreateViewModel @Inject constructor(
    private val showRepository: ShowRepository,
    private val partyRepository: PartyRepository
) : BaseViewModel<PartyMemberCreateState, PartyMemberCreateEvent, PartyMemberCreateSideEffect>(
    initialState = PartyMemberCreateState()
) {
    override fun reduceState(currentState: PartyMemberCreateState, event: PartyMemberCreateEvent): PartyMemberCreateState =
        when (event) {
            is PartyMemberCreateEvent.SetPartyCategory -> {
                currentState.copy(category = event.category)
            }

            is PartyMemberCreateEvent.SetShowId -> {
                currentState.copy(showId = event.showId)
            }

            is PartyMemberCreateEvent.RequestShowDetail -> {
                currentState.copy(showDetailModel = event.showDetailModel)
            }

            is PartyMemberCreateEvent.SetPartyInfo -> {
                currentState.copy(
                    showAt = event.showAt,
                    maxMemberNum = event.maxMemberNum
                )
            }

            is PartyMemberCreateEvent.SetPartyDescription -> {
                currentState.copy(
                    title = event.title,
                    content = event.content
                )
            }

            is PartyMemberCreateEvent.SetSortType -> {
                currentState.copy(
                    sortType = event.sortType
                )
            }

            is PartyMemberCreateEvent.LoadPlayItems -> {
                currentState.copy(
                    playItems = event.playItems
                )
            }

            is PartyMemberCreateEvent.LoadMusicalItems -> {
                currentState.copy(
                    musicalItems = event.musicalItems
                )
            }
        }

    fun setSortType(sortType: ShowSortType) {
        sendAction(PartyMemberCreateEvent.SetSortType(sortType))
        loadPlayItems()
        loadMusicalItems()
    }

    fun loadPlayItems() {
        sendAction(
            PartyMemberCreateEvent.LoadPlayItems(
                playItems = showRepository
                    .fetchShowList(
                        genre = "PLAY",
                        sort = uiState.value.sortType.code
                    ).cachedIn(viewModelScope)
            )
        )
    }

    fun loadMusicalItems() {
        sendAction(
            PartyMemberCreateEvent.LoadMusicalItems(
                musicalItems = showRepository
                    .fetchShowList(
                        "MUSICAL",
                        sort = uiState.value.sortType.code
                    ).cachedIn(viewModelScope)
            )
        )
    }

    fun setPartyCategory(partyType: PartyType) {
        sendAction(PartyMemberCreateEvent.SetPartyCategory(partyType.category))
    }

    fun setShowId(showId: String) {
        sendAction(PartyMemberCreateEvent.SetShowId(showId))
        showRepository.requestShowDetail(showId)
            .onEach { sendAction(PartyMemberCreateEvent.RequestShowDetail(it)) }
            .launchIn(viewModelScope)
    }

    fun setPartyInfo(date: String?, time: String, maxMemberNum: Int) {
        sendAction(
            PartyMemberCreateEvent.SetPartyInfo(
                showAt = date?.let {
                    changeShowAt(it, time)
                },
                maxMemberNum = maxMemberNum
            )
        )
    }

    fun setPartyDescription(title: String, content: String) {
        sendAction(
            PartyMemberCreateEvent.SetPartyDescription(
                title = title,
                content = content
            )
        )
    }

    fun createParty() {
        partyRepository.createParty(
            showId = uiState.value.showId,
            showAt = uiState.value.showAt,
            title = uiState.value.title,
            content = uiState.value.content,
            maxMemberNum = uiState.value.maxMemberNum
        ).onEach {
            sendSideEffect(PartyMemberCreateSideEffect.SuccessUpload)
        }.launchIn(viewModelScope)
    }
}
