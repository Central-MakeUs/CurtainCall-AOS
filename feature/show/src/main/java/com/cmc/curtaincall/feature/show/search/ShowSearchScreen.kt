package com.cmc.curtaincall.feature.show.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.cmc.curtaincall.common.designsystem.R
import com.cmc.curtaincall.common.designsystem.component.appbars.CurtainCallSearchTitleTopAppBar
import com.cmc.curtaincall.common.designsystem.component.appbars.SearchAppBarState
import com.cmc.curtaincall.common.designsystem.component.basic.SystemUiStatusBar
import com.cmc.curtaincall.common.designsystem.component.chips.CurtainCallBasicChip
import com.cmc.curtaincall.common.designsystem.component.sheets.bottom.CurtainCallShowSortBottomSheet
import com.cmc.curtaincall.common.designsystem.component.tooltip.CurtainCallShowSortTooltip
import com.cmc.curtaincall.common.designsystem.custom.poster.CurtainCallShowPoster
import com.cmc.curtaincall.common.designsystem.custom.search.SearchWordContent
import com.cmc.curtaincall.common.designsystem.custom.search.SearchWordEmptyContent
import com.cmc.curtaincall.common.designsystem.dimension.Paddings
import com.cmc.curtaincall.common.designsystem.theme.*
import com.cmc.curtaincall.domain.enums.ShowGenreType
import com.cmc.curtaincall.domain.enums.ShowSortType
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowSearchScreen(
    showSearchViewModel: ShowSearchViewModel = hiltViewModel(),
    onNavigateDetail: (String) -> Unit
) {
    val showSearchUiState by showSearchViewModel.uiState.collectAsStateWithLifecycle()
    val searchAppBarState by showSearchViewModel.searchAppBarState.collectAsStateWithLifecycle()

    SystemUiStatusBar(White)
    Scaffold(
        topBar = {
            CurtainCallSearchTitleTopAppBar(
                title = stringResource(R.string.show),
                searchAppBarState = searchAppBarState
            )
        }
    ) { paddingValues ->
        if (searchAppBarState.isSearchMode.value) {
            ShowSearchContent(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .background(CurtainCallTheme.colors.background),
                searchAppBarState = searchAppBarState,
                onNavigateDetail = onNavigateDetail
            )
        } else {
            ShowListContent(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .background(CurtainCallTheme.colors.background),
                onNavigateDetail = onNavigateDetail
            )
        }
    }
}

@Composable
private fun ShowSearchContent(
    modifier: Modifier = Modifier,
    showSearchViewModel: ShowSearchViewModel = hiltViewModel(),
    searchAppBarState: SearchAppBarState = SearchAppBarState(),
    onNavigateDetail: (String) -> Unit = {}
) {
    val showSearchUiState by showSearchViewModel.uiState.collectAsStateWithLifecycle()
    val showSearchWords = showSearchUiState.showSearchWords
    val searchShowInfoModels = showSearchUiState.showInfoModels.collectAsLazyPagingItems()

    if (searchAppBarState.isDoneSearch.value) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = modifier
                .padding(top = 30.dp)
                .padding(horizontal = 20.dp)
                .fillMaxSize(),
            contentPadding = PaddingValues(bottom = 26.dp),
            verticalArrangement = Arrangement.spacedBy(26.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(searchShowInfoModels.itemCount) { index ->
                searchShowInfoModels[index]?.let { searchShowInfoModel ->
                    CurtainCallShowPoster(
                        model = searchShowInfoModel.poster,
                        text = searchShowInfoModel.name,
                        isLike = searchShowInfoModel.favorite,
                        onLikeClick = {
                            showSearchViewModel.checkShowLike(
                                showId = searchShowInfoModel.id,
                                isLike = !searchShowInfoModel.favorite
                            )
                        },
                        onClick = { onNavigateDetail(searchShowInfoModel.id) }
                    )
                }
            }
        }
    } else {
        if (showSearchWords.isEmpty()) {
            Column(
                modifier = modifier
                    .padding(top = 30.dp)
                    .padding(horizontal = 20.dp)
            ) {
                Text(
                    text = stringResource(R.string.recently_search_word),
                    style = CurtainCallTheme.typography.body2.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )
                Spacer(Modifier.weight(190f))
                SearchWordEmptyContent(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally)
                )
                Spacer(Modifier.weight(339f))
            }
        } else {
            LazyColumn(
                modifier = modifier
                    .padding(top = 30.dp)
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.recently_search_word),
                            modifier = Modifier.weight(1f),
                            style = CurtainCallTheme.typography.body2.copy(
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                        if (showSearchWords.isNotEmpty()) {
                            Text(
                                text = stringResource(R.string.delete_all_search_word),
                                modifier = Modifier.clickable { showSearchViewModel.deleteAllShowSearchWord() },
                                style = CurtainCallTheme.typography.body3.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = Grey6
                                )
                            )
                        }
                    }
                }
                items(showSearchWords) { showSearchWord ->
                    SearchWordContent(
                        text = showSearchWord.word,
                        onClose = { showSearchViewModel.deleteShowSearchWord(showSearchWord) },
                        onClick = { showSearchViewModel.searchRecentlyWord(showSearchWord) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ShowListContent(
    modifier: Modifier = Modifier,
    showSearchViewModel: ShowSearchViewModel = hiltViewModel(),
    onNavigateDetail: (String) -> Unit
) {
    val showSearchUiState by showSearchViewModel.uiState.collectAsStateWithLifecycle()
    val showInfoModels = showSearchUiState.showInfoModels.collectAsLazyPagingItems()
    val popularShowInfoModels = showSearchUiState.popularShowRankModels
    var isShowBottomSheet by remember { mutableStateOf(false) }
    val lazyGridState = rememberLazyGridState()

    LaunchedEffect(showSearchViewModel) {
        showSearchViewModel.effects.collectLatest { effect ->
            when (effect) {
                ShowSearchEffect.RefreshShowList -> {
                    if (showSearchUiState.sortType == ShowSortType.POPULAR) {
                        showSearchViewModel.fetchPopularShowList()
                    } else {
                        showInfoModels.refresh()
                    }
                }

                ShowSearchEffect.ScrollFirstInList -> {
                    lazyGridState.animateScrollToItem(0)
                }
            }
        }
    }

    if (isShowBottomSheet) {
        CurtainCallShowSortBottomSheet(
            showSortType = showSearchUiState.sortType,
            onSelectSortType = {
                showSearchViewModel.selectSortType(it)
                isShowBottomSheet = false
            },
            onDismissRequest = { isShowBottomSheet = false }
        )
    }

    Box(
        modifier = modifier
            .padding(horizontal = 20.dp)
            .fillMaxSize()
    ) {
        Column {
            Row(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CurtainCallBasicChip(
                    text = ShowGenreType.PLAY.value,
                    textStyle = CurtainCallTheme.typography.body2,
                    isSelect = showSearchUiState.genreType == ShowGenreType.PLAY,
                    onClick = { showSearchViewModel.selectGenreType(ShowGenreType.PLAY) }
                )
                CurtainCallBasicChip(
                    modifier = Modifier.padding(start = Paddings.medium),
                    text = ShowGenreType.MUSICAL.value,
                    textStyle = CurtainCallTheme.typography.body2,
                    isSelect = showSearchUiState.genreType == ShowGenreType.MUSICAL,
                    onClick = { showSearchViewModel.selectGenreType(ShowGenreType.MUSICAL) }
                )
                Spacer(Modifier.weight(1f))
                Row(
                    modifier = Modifier.clickable { isShowBottomSheet = true },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = showSearchUiState.sortType.value,
                        style = CurtainCallTheme.typography.body3
                    )
                    Icon(
                        painter = painterResource(R.drawable.ic_down),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(start = 2.dp)
                            .size(12.dp),
                        tint = Color.Unspecified
                    )
                }
            }
        }
        if (showSearchUiState.isShowTooltip && showSearchUiState.sortType == ShowSortType.POPULAR) {
            CurtainCallShowSortTooltip(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 51.dp)
                    .zIndex(1f),
                text = stringResource(R.string.show_coach_mark),
                onClick = { showSearchViewModel.setFirstEntry() }
            )
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .padding(top = 67.dp)
                .fillMaxSize(),
            state = lazyGridState,
            contentPadding = PaddingValues(bottom = 26.dp),
            verticalArrangement = Arrangement.spacedBy(26.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            if (showSearchUiState.sortType == ShowSortType.POPULAR) {
                items(popularShowInfoModels) { showRankModel ->
                    CurtainCallShowPoster(
                        model = showRankModel.poster,
                        text = showRankModel.name,
                        isLike = showRankModel.favorite,
                        onLikeClick = {
                            showSearchViewModel.checkShowLike(
                                showId = showRankModel.id,
                                isLike = !showRankModel.favorite
                            )
                        },
                        onClick = { onNavigateDetail(showRankModel.id) }
                    )
                }
            } else {
                items(count = showInfoModels.itemCount) { index ->
                    showInfoModels[index]?.let { showItem ->
                        CurtainCallShowPoster(
                            model = showItem.poster,
                            text = showItem.name,
                            isLike = showItem.favorite,
                            onLikeClick = {
                                showSearchViewModel.checkShowLike(
                                    showId = showItem.id,
                                    isLike = !showItem.favorite
                                )
                            },
                            onClick = { onNavigateDetail(showItem.id) }
                        )
                    }
                }
            }
        }
    }
}
