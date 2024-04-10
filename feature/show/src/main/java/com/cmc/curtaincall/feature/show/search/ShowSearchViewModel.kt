package com.cmc.curtaincall.feature.show.search

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.cmc.curtaincall.common.designsystem.component.appbars.SearchAppBarState
import com.cmc.curtaincall.core.base.BaseViewModel
import com.cmc.curtaincall.domain.model.show.ShowInfoModel
import com.cmc.curtaincall.domain.model.show.ShowSearchWordModel
import com.cmc.curtaincall.domain.repository.FavoriteRepository
import com.cmc.curtaincall.domain.repository.LaunchRepository
import com.cmc.curtaincall.domain.repository.ShowRepository
import com.cmc.curtaincall.domain.enums.ShowGenreType
import com.cmc.curtaincall.domain.enums.ShowSortType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShowSearchViewModel @Inject constructor(
    private val showRepository: ShowRepository,
    private val favoriteRepository: FavoriteRepository,
    private val launchRepository: LaunchRepository
) : BaseViewModel<ShowSearchUiState, ShowSearchEvent, Nothing>(
    initialState = ShowSearchUiState()
) {
    private val _sortType = MutableStateFlow(ShowSortType.POPULAR)
    val sortType = _sortType.asStateFlow()

    private val _genreType = MutableStateFlow(ShowGenreType.PLAY)
    val genreType = _genreType.asStateFlow()

    private var _showInfoModels = MutableStateFlow<PagingData<ShowInfoModel>>(PagingData.empty())
    val showInfoModels = _showInfoModels.asStateFlow()

    // PagingData 갱신 여부
    private val _isRefresh = MutableSharedFlow<Boolean>()
    val isRefresh = _isRefresh.asSharedFlow()

    // 최초 작품 진입 여부
    private val _isFirstEntry = MutableStateFlow(false)
    val isFirstEntry = _isFirstEntry.asStateFlow()

    private val _isChange = MutableSharedFlow<Boolean>()
    val isChange = _isChange.asSharedFlow()

    private val _showSearchWords = MutableStateFlow<List<ShowSearchWordModel>>(listOf())
    val showSearchWords = _showSearchWords.asStateFlow()

    private val _searchShowInfoModels = MutableStateFlow<PagingData<ShowInfoModel>>(PagingData.empty())
    val searchShowInfoModels = _searchShowInfoModels.asStateFlow()

    private val _searchAppBarState = MutableStateFlow(SearchAppBarState())
    val searchAppBarState = _searchAppBarState.asStateFlow()

    // ///////////////////////////////////////////////////////////////////////////////

    private var _searchWords = MutableStateFlow<List<ShowSearchWordModel>>(listOf())
    val searchWords = _searchWords.asStateFlow()

    private var _showSearchItems = MutableStateFlow<PagingData<ShowInfoModel>>(PagingData.empty())
    val showSearchItems = _showSearchItems.asStateFlow()

    init {
        checkIsFirstEntry()
        queryShowSearchWords()
        fetchShowList()
        _searchAppBarState.value = _searchAppBarState.value.copy(
            onDone = {
                _searchAppBarState.value.isDoneSearch.value = true
                insertShowSearchWord(it)
            }
        )
    }

    private fun checkIsFirstEntry() {
        launchRepository.getIsFirstEntryShowList()
            .onEach { _isFirstEntry.value = it }
            .launchIn(viewModelScope)
    }

    fun setFirstEntry() {
        viewModelScope.launch {
            launchRepository.setIsFirstEntryShowList()
        }
    }

    fun selectSortType(sort: ShowSortType) {
        if (_sortType.value != sort) {
            _sortType.value = sort
            fetchShowList()
        }
    }

    fun selectGenreType(genre: ShowGenreType) {
        if (_genreType.value != genre) {
            _genreType.value = genre
            fetchShowList()
        }
    }

    fun checkShowLike(
        showId: String,
        isLike: Boolean
    ) {
        if (isLike) {
            favoriteRepository.requestFavoriteShow(showId)
                .onEach { _isRefresh.emit(true) }
                .launchIn(viewModelScope)
        } else {
            favoriteRepository.deleteFavoriteShow(showId)
                .onEach { _isRefresh.emit(true) }
                .launchIn(viewModelScope)
        }
    }
    private fun fetchShowList() {
        showRepository.fetchShowList(genreType.value.name, sortType.value.code)
            .distinctUntilChanged()
            .cachedIn(viewModelScope)
            .onEach { _showInfoModels.value = it }
            .launchIn(viewModelScope)

        viewModelScope.launch {
            _isChange.emit(true)
        }
    }

    private fun queryShowSearchWords() {
        showRepository.getShowSearchWordList()
            .distinctUntilChanged()
            .onEach { _showSearchWords.value = it }
            .launchIn(viewModelScope)
    }

    private fun insertShowSearchWord(query: String) {
        viewModelScope.launch {
            showRepository.insertShowSearchWord(ShowSearchWordModel(query, System.currentTimeMillis()))
            requestShowSearchWords()
            fetchSearchShowList(query)
        }
    }

    fun deleteShowSearchWord(searchWordModel: ShowSearchWordModel) {
        viewModelScope.launch {
            showRepository.deleteShowSearchWord(searchWordModel)
            requestShowSearchWords()
        }
    }

    fun deleteAllShowSearchWord() {
        viewModelScope.launch {
            showRepository.deleteShowSearchWordList()
            requestShowSearchWords()
        }
    }

    private fun fetchSearchShowList(query: String) {
        showRepository.fetchSearchShowList(query.trim())
            .cachedIn(viewModelScope)
            .onEach { _searchShowInfoModels.value = it }
            .launchIn(viewModelScope)
    }

    fun searchRecentlyWord(searchWordModel: ShowSearchWordModel) {
        _searchAppBarState.value.searchText.value = searchWordModel.word
        _searchAppBarState.value.onDone(searchWordModel.word)
    }

    // //////////////////////////////////////////////////////////////////////////////////

    override fun reduceState(currentState: ShowSearchUiState, event: ShowSearchEvent): ShowSearchUiState =
        when (event) {
            ShowSearchEvent.ActivateSearch -> currentState.copy(isActiveSearch = true)
            ShowSearchEvent.DeActivateSearch -> currentState.copy(isActiveSearch = false)
            ShowSearchEvent.Searching -> currentState.copy(isDoneSearch = false)
            ShowSearchEvent.DoneSearch -> currentState.copy(isDoneSearch = true)
            is ShowSearchEvent.SetQueryString -> currentState.copy(queryString = event.query)
            is ShowSearchEvent.ChangeGenre -> currentState.copy(genre = event.genre)
            is ShowSearchEvent.ChangeSort -> currentState.copy(sortType = event.sortType)
        }

    fun searchShowList(query: String) {
        showRepository.fetchSearchShowList(query)
            .cachedIn(viewModelScope)
            .onEach { _showSearchItems.value = it }
            .launchIn(viewModelScope)
    }

    fun onActiveSearch() {
        sendAction(ShowSearchEvent.ActivateSearch)
    }

    fun onDeActiveSearch() {
        sendAction(ShowSearchEvent.DeActivateSearch)
    }

    fun onDoneSearch() {
        sendAction(ShowSearchEvent.DoneSearch)
    }

    fun requestFavoriteShow(showId: String) {
        favoriteRepository.requestFavoriteShow(showId).launchIn(viewModelScope)
    }

    fun deleteFavoriteShow(showId: String) {
        favoriteRepository.deleteFavoriteShow(showId).launchIn(viewModelScope)
    }

    private fun requestShowSearchWords() {
        showRepository.getShowSearchWordList()
            .onEach { _searchWords.value = it }
            .launchIn(viewModelScope)
    }

    fun deleteShowSearchWordList() {
        viewModelScope.launch {
            showRepository.deleteShowSearchWordList()
        }
    }
}
