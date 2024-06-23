package com.cmc.curtaincall.feature.partymember

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.cmc.curtaincall.common.designsystem.component.appbars.SearchAppBarState
import com.cmc.curtaincall.core.base.BaseViewModel
import com.cmc.curtaincall.domain.model.party.PartySearchWordModel
import com.cmc.curtaincall.domain.repository.LaunchRepository
import com.cmc.curtaincall.domain.repository.PartyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class PartyMemberViewModel @Inject constructor(
    private val partyRepository: PartyRepository,
    private val launchRepository: LaunchRepository
) : BaseViewModel<PartyMemberUiState, PartyMemberEvent, Nothing>(
    initialState = PartyMemberUiState()
) {
    private val _searchAppBarState = MutableStateFlow(SearchAppBarState())
    val searchAppBarState = _searchAppBarState.asStateFlow()

    init {
        checkPartyTooltip()
        querySearchWords()

        _searchAppBarState.value = _searchAppBarState.value.copy(
            onDone = {
                _searchAppBarState.value.isDoneSearch.value = true
                insertPartySearchWord(it)
                fetchSearchPartyList(it)
            }
        )
    }

    override fun reduceState(currentState: PartyMemberUiState, event: PartyMemberEvent): PartyMemberUiState =
        when (event) {
            is PartyMemberEvent.FetchPartyMember -> {
                currentState.copy(
                    partyModels = event.partyModels
                )
            }

            PartyMemberEvent.ShowTooltip -> {
                currentState.copy(
                    isShowTooltip = true
                )
            }

            PartyMemberEvent.HideTooltip -> {
                currentState.copy(
                    isShowTooltip = false
                )
            }

            is PartyMemberEvent.QueryPartySearchWord -> {
                currentState.copy(
                    partySearchWords = event.partySearchWords
                )
            }

            else -> currentState
        }

    fun fetchPartyList(
        startDate: String? = null,
        endDate: String? = null
    ) {
        sendAction(
            PartyMemberEvent.FetchPartyMember(
                partyModels = partyRepository.fetchPartyList(
                    startDate = startDate,
                    endDate = endDate
                ).cachedIn(viewModelScope)
            )
//            PartyMemberEvent.FetchPartyMember(
//                partyModels = flow {
//                    PagingData.empty<PartyModel>()
//                }
//            )
        )
    }

    fun fetchSearchPartyList(keyword: String) {
        sendAction(
            PartyMemberEvent.FetchPartyMember(
                partyModels = partyRepository.fetchSearchPartyList(
                    keyword = keyword
                )
            )
        )
    }

    private fun checkPartyTooltip() {
        launchRepository.isShowPartyTooltip()
            .onEach { check ->
                sendAction(
                    if (check) {
                        PartyMemberEvent.ShowTooltip
                    } else {
                        PartyMemberEvent.HideTooltip
                    }
                )
            }
            .launchIn(viewModelScope)
    }

    fun hidePartyTooltip() {
        viewModelScope.launch {
            launchRepository.stopShowPartyTooltip()
        }
        sendAction(PartyMemberEvent.HideTooltip)
    }

    private fun querySearchWords() {
        partyRepository.getPartySearchWordList()
            .distinctUntilChanged()
            .onEach {
                sendAction(
                    PartyMemberEvent.QueryPartySearchWord(
                        partySearchWords = it
                    )
                )
            }
            .launchIn(viewModelScope)
    }

    private fun insertPartySearchWord(word: String) {
        viewModelScope.launch {
            partyRepository.insertPartySearchWord(
                partySearchWordModel = PartySearchWordModel(
                    word = word,
                    searchAt = System.currentTimeMillis()
                )
            )
            querySearchWords()
        }
    }

    fun searchPartyModel(partySearchWordModel: PartySearchWordModel) {
        _searchAppBarState.value.searchText.value = partySearchWordModel.word
        _searchAppBarState.value.onDone(partySearchWordModel.word)
    }

    fun deletePartySearchWord(partySearchWordModel: PartySearchWordModel) {
        viewModelScope.launch {
            partyRepository.deletePartySearchWord(partySearchWordModel)
            querySearchWords()
        }
    }

    fun deleteAllShowSearchWord() {
        viewModelScope.launch {
            partyRepository.deletePartySearchWordList()
            querySearchWords()
        }
    }
}
