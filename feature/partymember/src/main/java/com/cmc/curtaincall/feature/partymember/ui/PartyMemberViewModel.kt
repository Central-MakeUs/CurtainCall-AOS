package com.cmc.curtaincall.feature.partymember.ui

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.cmc.curtaincall.common.designsystem.component.card.PartyType
import com.cmc.curtaincall.core.base.BaseViewModel
import com.cmc.curtaincall.domain.model.party.PartyModel
import com.cmc.curtaincall.domain.model.party.PartySearchWordModel
import com.cmc.curtaincall.domain.repository.LaunchRepository
import com.cmc.curtaincall.domain.repository.MemberRepository
import com.cmc.curtaincall.domain.repository.PartyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
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
    private val memberRepository: MemberRepository,
    private val launchRepository: LaunchRepository
) : BaseViewModel<PartyMemberUiState, PartyMemberEvent, Nothing>(
    initialState = PartyMemberUiState()
) {
    private val _partyModels = MutableStateFlow<PagingData<PartyModel>>(PagingData.empty())
    val partyModel = _partyModels.asStateFlow()

    private val _isShowTooltip = MutableStateFlow(false)
    val isShowTooltip = _isShowTooltip.asStateFlow()

    // /

    private val _memberId = MutableStateFlow(0)
    val memberId: StateFlow<Int> = _memberId.asStateFlow()

    private val _searchWords = MutableStateFlow<List<PartySearchWordModel>>(listOf())
    val searchWords = _searchWords.asStateFlow()

    init {
        checkPartyTooltip()
        fetchPartyList()
//        getMemberId()
//        requestPartySearchWords()
    }

    fun fetchPartyList(
        startDate: String? = null,
        endDate: String? = null
    ) {
        partyRepository.fetchPartyList(
            startDate = startDate,
            endDate = endDate
        ).onEach {
            _partyModels.value = it
        }.launchIn(viewModelScope)
    }

    private fun checkPartyTooltip() {
        launchRepository.isShowPartyTooltip()
            .onEach { _isShowTooltip.value = it }
            .launchIn(viewModelScope)
    }

    fun stopPartyTooltip() {
        viewModelScope.launch {
            launchRepository.stopShowPartyTooltip()
        }
    }

    // //

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

            is PartyMemberEvent.LoadWatchingItems -> {
                currentState.copy(watchingItems = event.watchingItems)
            }

            is PartyMemberEvent.LoadFoodCafeItems -> {
                currentState.copy(foodCafeItems = event.foodCafeItems)
            }

            is PartyMemberEvent.LoadEtcItems -> {
                currentState.copy(etcItems = event.etcItems)
            }

            else -> currentState
        }

    fun loadWatchingItems() {
//        sendAction(
// //            PartyMemberEvent.LoadWatchingItems(
// //                watchingItems = partyRepository.fetchPartyList("WATCHING")
// //            )
//        )
    }

    fun loadFoodCafeItems() {
//        sendAction(
// //            PartyMemberEvent.LoadFoodCafeItems(
// //                foodCafeItems = partyRepository.fetchPartyList("FOOD_CAFE")
// //            )
//        )
    }

    fun loadEtcItems() {
//        sendAction(
// //            PartyMemberEvent.LoadEtcItems(
// //                etcItems = partyRepository.fetchPartyList("ETC")
// //            )
//        )
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
