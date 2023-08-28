package com.cmc.curtaincall.feature.partymember.ui

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.cmc.curtaincall.common.design.component.content.card.PartyType
import com.cmc.curtaincall.core.base.BaseViewModel
import com.cmc.curtaincall.domain.model.party.PartyModel
import com.cmc.curtaincall.domain.model.party.PartySearchWordModel
import com.cmc.curtaincall.domain.repository.MemberRepository
import com.cmc.curtaincall.domain.repository.PartyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PartyMemberViewModel @Inject constructor(
    private val partyRepository: PartyRepository,
    private val memberRepository: MemberRepository
) : BaseViewModel<PartyMemberUiState, PartyMemberEvent, Nothing>(
    initialState = PartyMemberUiState()
) {

    private val _memberId = MutableStateFlow(0)
    val memberId: StateFlow<Int> = _memberId.asStateFlow()

    private val _searchWords = MutableStateFlow<List<PartySearchWordModel>>(listOf())
    val searchWords = _searchWords.asStateFlow()

    val watchingItems: Flow<PagingData<PartyModel>> = partyRepository
        .fetchPartyList("WATCHING")
        .cachedIn(viewModelScope)

    val foodCafeItems: Flow<PagingData<PartyModel>> = partyRepository
        .fetchPartyList("FOOD_CAFE")
        .cachedIn(viewModelScope)

    val etcItems: Flow<PagingData<PartyModel>> = partyRepository
        .fetchPartyList("ETC")
        .cachedIn(viewModelScope)

    init {
        getMemberId()
        requestPartySearchWords()
    }

    override fun reduceState(currentState: PartyMemberUiState, event: PartyMemberEvent): PartyMemberUiState =
        when (event) {
            is PartyMemberEvent.ChangePartyType -> {
                currentState.copy(partyType = event.partyType)
            }

            is PartyMemberEvent.ChangeActiveSearch -> {
                currentState.copy(isActiveSearch = event.isActiveSearch)
            }

            is PartyMemberEvent.ChangeDoneSearch -> {
                currentState.copy(isDoneSearch = event.isDoneSearch)
            }

            is PartyMemberEvent.SetQueryString -> {
                currentState.copy(queryString = event.queryString)
            }

            is PartyMemberEvent.SearchPartyList -> {
                currentState.copy(partySearchItems = event.partySearchItems)
            }
        }

    private fun getMemberId() {
        memberRepository.getMemberId()
            .onEach { _memberId.value = it }
            .launchIn(viewModelScope)
    }

    fun changePartType(partyType: PartyType) {
        sendAction(PartyMemberEvent.ChangePartyType(partyType))
    }

    fun changeActiveSearch(isActiveSearch: Boolean) {
        sendAction(PartyMemberEvent.ChangeActiveSearch(isActiveSearch))
    }

    fun changeDoneSearch(isDoneSearch: Boolean) {
        sendAction(PartyMemberEvent.ChangeDoneSearch(isDoneSearch))
    }

    fun setQueryString(query: String) {
        sendAction(PartyMemberEvent.SetQueryString(query))
    }

    fun searchPartyList(query: String) {
        sendAction(
            PartyMemberEvent.SearchPartyList(
                partySearchItems = partyRepository
                    .fetchSearchPartyList(
                        category = uiState.value.partyType.category,
                        keyword = query
                    ).cachedIn(viewModelScope)
            )
        )
    }

    private fun requestPartySearchWords() {
        partyRepository.getPartySearchWordList()
            .onEach { _searchWords.value = it }
            .launchIn(viewModelScope)
    }

    fun insertPartySearchWord() {
        viewModelScope.launch {
            partyRepository.insertPartySearchWord(
                partySearchWordModel = PartySearchWordModel(
                    uiState.value.queryString,
                    System.currentTimeMillis()
                )
            )
            requestPartySearchWords()
        }
    }

    fun deletePartySearchWord(partySearchWordModel: PartySearchWordModel) {
        viewModelScope.launch {
            partyRepository.deletePartySearchWord(partySearchWordModel)
            requestPartySearchWords()
        }
    }

    fun deletePartySearchWordList() {
        viewModelScope.launch {
            partyRepository.deletePartySearchWordList()
        }
    }
}
