package com.cmc.curtaincall.feature.partymember.recruit.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.cmc.curtaincall.common.designsystem.component.appbars.SearchAppBarState
import com.cmc.curtaincall.common.designsystem.component.chips.CurtainCallBasicChip
import com.cmc.curtaincall.common.designsystem.component.sheets.bottom.CurtainCallShowSortBottomSheet
import com.cmc.curtaincall.common.designsystem.component.tooltip.CurtainCallShowSortTooltip
import com.cmc.curtaincall.common.designsystem.custom.poster.CurtainCallTitlePoster
import com.cmc.curtaincall.common.designsystem.theme.CurtainCallTheme
import com.cmc.curtaincall.domain.enums.ShowGenreType
import com.cmc.curtaincall.domain.enums.ShowSortType
import com.cmc.curtaincall.feature.partymember.recruit.PartyMemberRecruitViewModel

@Composable
fun ColumnScope.PartyMemberRecruitFirstScreen(
    partyMemberRecruitViewModel: PartyMemberRecruitViewModel = hiltViewModel(),
    searchAppBarState: SearchAppBarState = SearchAppBarState(),
    sortType: ShowSortType,
    genreType: ShowGenreType,
    isShowTooltip: Boolean
) {
    val uiState by partyMemberRecruitViewModel.uiState.collectAsStateWithLifecycle()
    val showInfoModels = uiState.showInfoModels.collectAsLazyPagingItems()
    val popularShowRankModels = uiState.popularShowRankModels
    var isShowSortBottomSheet by remember { mutableStateOf(false) }
    if (isShowSortBottomSheet) {
        CurtainCallShowSortBottomSheet(
            showSortType = sortType,
            onSelectSortType = {
                partyMemberRecruitViewModel.changeShowSortType(it)
                isShowSortBottomSheet = false
            },
            onDismissRequest = { isShowSortBottomSheet = false }
        )
    }

    Text(
        text = stringResource(R.string.party_member_recruit_first_step_title),
        modifier = Modifier.padding(start = 20.dp, top = 30.dp),
        style = CurtainCallTheme.typography.subTitle4
    )

    if (searchAppBarState.isSearchMode.value) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .padding(top = 32.dp)
                .fillMaxSize(),
            contentPadding = PaddingValues(top = 20.dp, start = 20.dp, end = 20.dp, bottom = 120.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(showInfoModels.itemCount) { index ->
                showInfoModels[index]?.let { showInfoModel ->
                    CurtainCallTitlePoster(
                        model = showInfoModel.poster,
                        title = showInfoModel.name,
                        isSelected = uiState.showId == showInfoModel.id,
                        onSelect = {
                            partyMemberRecruitViewModel.selectShowPoster(
                                showId = showInfoModel.id,
                                showStartDate = showInfoModel.startDate,
                                showEndDate = showInfoModel.endDate,
                                showTimes = showInfoModel.showTimes
                            )
                        }
                    )
                }
            }
        }
    } else {
        Box(
            modifier = Modifier
                .padding(top = 10.dp)
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CurtainCallBasicChip(
                    text = ShowGenreType.PLAY.value,
                    textStyle = CurtainCallTheme.typography.body2.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = CurtainCallTheme.colors.primary
                    ),
                    isSelect = genreType == ShowGenreType.PLAY,
                    onClick = { partyMemberRecruitViewModel.changeShowGenreType(ShowGenreType.PLAY) }
                )
                CurtainCallBasicChip(
                    modifier = Modifier.padding(start = 8.dp),
                    text = ShowGenreType.MUSICAL.value,
                    textStyle = CurtainCallTheme.typography.body2.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = CurtainCallTheme.colors.primary
                    ),
                    isSelect = genreType == ShowGenreType.MUSICAL,
                    onClick = { partyMemberRecruitViewModel.changeShowGenreType(ShowGenreType.MUSICAL) }
                )
                Spacer(Modifier.weight(1f))
                Row(
                    modifier = Modifier.clickable { isShowSortBottomSheet = true },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = sortType.value,
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
            if (isShowTooltip && sortType == ShowSortType.POPULAR) {
                CurtainCallShowSortTooltip(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 31.dp, end = 20.dp)
                        .zIndex(1f),
                    text = stringResource(R.string.show_coach_mark),
                    onClick = { partyMemberRecruitViewModel.hideShowTooltip() }
                )
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 49.dp),
                contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 120.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (uiState.sortType == ShowSortType.POPULAR) {
                    items(popularShowRankModels) { showRankModel ->
                        CurtainCallTitlePoster(
                            model = showRankModel.poster,
                            title = showRankModel.name,
                            isSelected = uiState.showId == showRankModel.id,
                            onSelect = {
                                partyMemberRecruitViewModel.selectShowPoster(
                                    showId = showRankModel.id,
                                    showStartDate = showRankModel.startDate,
                                    showEndDate = showRankModel.endDate,
                                    showTimes = showRankModel.showTimes
                                )
                            }
                        )
                    }
                } else {
                    items(showInfoModels.itemCount) { index ->
                        showInfoModels[index]?.let { showInfoModel ->
                            CurtainCallTitlePoster(
                                model = showInfoModel.poster,
                                title = showInfoModel.name,
                                isSelected = uiState.showId == showInfoModel.id,
                                onSelect = {
                                    partyMemberRecruitViewModel.selectShowPoster(
                                        showId = showInfoModel.id,
                                        showStartDate = showInfoModel.startDate,
                                        showEndDate = showInfoModel.endDate,
                                        showTimes = showInfoModel.showTimes
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
