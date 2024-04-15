package com.cmc.curtaincall.feature.show.search

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.cmc.curtaincall.common.designsystem.component.appbars.SearchAppBarState
import com.cmc.curtaincall.core.base.BaseViewModel
import com.cmc.curtaincall.domain.enums.ShowGenreType
import com.cmc.curtaincall.domain.enums.ShowSortType
import com.cmc.curtaincall.domain.model.show.ShowSearchWordModel
import com.cmc.curtaincall.domain.repository.FavoriteRepository
import com.cmc.curtaincall.domain.repository.LaunchRepository
import com.cmc.curtaincall.domain.repository.ShowRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class ShowSearchViewModel @Inject constructor(
    private val showRepository: ShowRepository,
    private val favoriteRepository: FavoriteRepository,
    private val launchRepository: LaunchRepository
) : BaseViewModel<ShowSearchUiState, ShowSearchEvent, ShowSearchEffect>(
    initialState = ShowSearchUiState()
) {
    private val _searchAppBarState = MutableStateFlow(SearchAppBarState())
    val searchAppBarState = _searchAppBarState.asStateFlow()

    init {
        checkIsFirstEntry()
        queryShowSearchWords()
        fetchShowList()
        _searchAppBarState.value = _searchAppBarState.value.copy(
            onCancel = {
                _searchAppBarState.value.searchText.value = ""
                _searchAppBarState.value.isSearchMode.value = false
                _searchAppBarState.value.isDoneSearch.value = false
                fetchShowList()
            },
            onDone = {
                _searchAppBarState.value.isDoneSearch.value = true
                insertShowSearchWord(it)
            }
        )
    }

    override fun reduceState(currentState: ShowSearchUiState, event: ShowSearchEvent): ShowSearchUiState =
        when (event) {
            ShowSearchEvent.ShowTooltip -> {
                currentState.copy(
                    isShowTooltip = true
                )
            }

            ShowSearchEvent.HideTooltip -> {
                currentState.copy(
                    isShowTooltip = false
                )
            }

            is ShowSearchEvent.SelectSortType -> {
                currentState.copy(
                    sortType = event.sortType
                )
            }

            is ShowSearchEvent.SelectGenreType -> {
                currentState.copy(
                    genreType = event.genreType
                )
            }

            is ShowSearchEvent.QueryShowSearchWords -> {
                currentState.copy(
                    showSearchWords = event.showSearchWords
                )
            }

            is ShowSearchEvent.GetShowInfoModels -> {
                currentState.copy(
                    showInfoModels = event.showInfoModels
                )
            }

            is ShowSearchEvent.GetPopularShowRankModels -> {
                currentState.copy(
                    popularShowRankModels = event.showRankModels
                )
            }
        }

    private fun checkIsFirstEntry() {
        launchRepository.getIsFirstEntryShowList()
            .onEach { isShow ->
                sendAction(
                    if (isShow) {
                        ShowSearchEvent.ShowTooltip
                    } else {
                        ShowSearchEvent.HideTooltip
                    }
                )
            }
            .launchIn(viewModelScope)
    }

    fun setFirstEntry() {
        viewModelScope.launch {
            launchRepository.setIsFirstEntryShowList()
            sendAction(ShowSearchEvent.HideTooltip)
        }
    }

    private fun queryShowSearchWords() {
        showRepository.getShowSearchWordList()
            .distinctUntilChanged()
            .onEach {
                sendAction(
                    ShowSearchEvent.QueryShowSearchWords(
                        showSearchWords = it
                    )
                )
            }
            .launchIn(viewModelScope)
    }

    fun selectSortType(sortType: ShowSortType) {
        if (uiState.value.sortType != sortType) {
            sendAction(
                ShowSearchEvent.SelectSortType(
                    sortType = sortType
                )
            )
            fetchShowList()
        }
    }

    fun selectGenreType(genreType: ShowGenreType) {
        if (uiState.value.genreType != genreType) {
            sendAction(
                ShowSearchEvent.SelectGenreType(
                    genreType = genreType
                )
            )
            fetchShowList()
        }
    }

    private fun fetchShowList() {
        if (uiState.value.sortType == ShowSortType.POPULAR) {
            fetchPopularShowList()
        } else {
            sendAction(
                ShowSearchEvent.GetShowInfoModels(
                    showInfoModels = showRepository.fetchShowList(
                        genre = uiState.value.genreType.name,
                        sort = uiState.value.sortType.code
                    ).distinctUntilChanged()
                        .cachedIn(viewModelScope)
                )
            )
            sendSideEffect(ShowSearchEffect.ScrollFirstInList)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun fetchPopularShowList() {
        val type = "DAY"
        val baseDate = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now().minusDays(1))
        showRepository.requestPopularShowList(
            type = type,
            genre = uiState.value.genreType.name,
            baseDate = baseDate
        ).onEach {
            sendAction(
                ShowSearchEvent.GetPopularShowRankModels(
                    showRankModels = it
                )
            )
        }.flatMapLatest { showRankModels ->
            favoriteRepository.checkFavoriteShows(showRankModels.map { it.id })
        }.onEach { checkFavoriteShows ->
            sendAction(
                ShowSearchEvent.GetPopularShowRankModels(
                    showRankModels = uiState.value.popularShowRankModels.map { showRankModel ->
                        showRankModel.copy(
                            favorite = checkFavoriteShows.find {
                                it.showId == showRankModel.id
                            }?.favorite ?: false
                        )
                    }
                )
            )
        }.launchIn(viewModelScope)
    }

    fun checkShowLike(
        showId: String,
        isLike: Boolean
    ) {
        if (isLike) {
            favoriteRepository.requestFavoriteShow(showId)
                .onEach { sendSideEffect(ShowSearchEffect.RefreshShowList) }
                .launchIn(viewModelScope)
        } else {
            favoriteRepository.deleteFavoriteShow(showId)
                .onEach { sendSideEffect(ShowSearchEffect.RefreshShowList) }
                .launchIn(viewModelScope)
        }
    }

    private fun insertShowSearchWord(query: String) {
        viewModelScope.launch {
            showRepository.insertShowSearchWord(ShowSearchWordModel(query, System.currentTimeMillis()))
            fetchSearchShowList(query)
        }
    }

    private fun fetchSearchShowList(query: String) {
        sendAction(
            ShowSearchEvent.GetShowInfoModels(
                showInfoModels = showRepository
                    .fetchSearchShowList(query.trim())
                    .cachedIn(viewModelScope)
            )
        )
    }

    fun deleteShowSearchWord(searchWordModel: ShowSearchWordModel) {
        viewModelScope.launch {
            showRepository.deleteShowSearchWord(searchWordModel)
        }
    }

    fun deleteAllShowSearchWord() {
        viewModelScope.launch {
            showRepository.deleteShowSearchWordList()
        }
    }

    fun searchRecentlyWord(searchWordModel: ShowSearchWordModel) {
        _searchAppBarState.value.searchText.value = searchWordModel.word
        _searchAppBarState.value.onDone(searchWordModel.word)
    }
}
