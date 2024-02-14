package com.cmc.curtaincall.feature.show.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cmc.curtaincall.common.designsystem.R
import com.cmc.curtaincall.common.designsystem.component.appbars.CurtainCallCenterTopAppBarWithBack
import com.cmc.curtaincall.common.designsystem.component.basic.SystemUiStatusBar
import com.cmc.curtaincall.common.designsystem.component.canvas.CurtainCallShowLiveTalkCoachMark
import com.cmc.curtaincall.common.designsystem.component.cards.ShowDetailCard
import com.cmc.curtaincall.common.designsystem.component.divider.HorizontalDivider
import com.cmc.curtaincall.common.designsystem.theme.CurtainCallTheme
import com.cmc.curtaincall.common.designsystem.theme.Grey8
import com.cmc.curtaincall.domain.enums.MenuTabType
import com.cmc.curtaincall.domain.model.show.ShowDetailModel
import com.cmc.curtaincall.feature.show.detail.menu.LostPropertyTabContent
import com.cmc.curtaincall.feature.show.detail.menu.ShowDetailMenuTabContent
import com.cmc.curtaincall.feature.show.detail.menu.ShowReviewTabContent

@Composable
internal fun ShowDetailScreen(
    showDetailViewModel: ShowDetailViewModel = hiltViewModel(),
    showId: String?,
    onNavigateReview: (String) -> Unit = {},
    onNavigateLostProperty: (String, String) -> Unit = { _, _ -> },
    onBack: () -> Unit = {}
) {
    requireNotNull(showId)

    val scrollState = rememberScrollState()
    val showDetailUiState by showDetailViewModel.uiState.collectAsStateWithLifecycle()
    val ticketPrices = showDetailUiState.showDetailModel.ticketPrice.split(", ")

    LaunchedEffect(Unit) {
        showDetailViewModel.requestShowDetail(showId)
        showDetailViewModel.checkFavoriteShow(showId)
    }

    SystemUiStatusBar(CurtainCallTheme.colors.primary)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        ShowDetailContent(
            modifier = Modifier
                .fillMaxWidth()
                .height(668.dp)
                .background(CurtainCallTheme.colors.background),
            showDetailModel = showDetailUiState.showDetailModel,
            isFavorite = showDetailUiState.isFavorite,
            isShowCoachMark = showDetailUiState.isShowCoachMark,
            onBack = onBack
        )
        ShowDetailMenuTab(
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
private fun ShowDetailMenuTab(
    modifier: Modifier = Modifier,
    showDetailViewModel: ShowDetailViewModel = hiltViewModel()
) {
    val showDetailUiState by showDetailViewModel.uiState.collectAsStateWithLifecycle()
    Column(modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
        ) {
            MenuTabType.values().forEach { menuTabType ->
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                        .background(CurtainCallTheme.colors.background)
                        .clickable { showDetailViewModel.changeMenuTabType(menuTabType) },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = menuTabType.label,
                            style = CurtainCallTheme.typography.subTitle4.copy(
                                color = CurtainCallTheme.colors.primary
                            )
                        )
                    }
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        height = 3.dp,
                        background = if (showDetailUiState.menuTabType == menuTabType) {
                            CurtainCallTheme.colors.primary
                        } else {
                            Grey8
                        }
                    )
                }
            }
        }
        when (showDetailUiState.menuTabType) {
            MenuTabType.DETAIL -> {
                ShowDetailMenuTabContent(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(CurtainCallTheme.colors.background),
                    showDetailModel = showDetailUiState.showDetailModel,
                    facilityDetailModel = showDetailUiState.facilityDetailModel
                )
            }

            MenuTabType.SHOW_REVIEW -> {
                ShowReviewTabContent(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(CurtainCallTheme.colors.background),
                    showReviews = showDetailUiState.showReviews,
                    reviewCount = showDetailUiState.showDetailModel.reviewCount
                )
            }

            MenuTabType.LOST_PROPERTY -> {
                LostPropertyTabContent(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(CurtainCallTheme.colors.background),
                    lostProperties = showDetailUiState.lostProperties
                )
            }
        }
    }
}

@Composable
private fun ShowDetailContent(
    modifier: Modifier = Modifier,
    showDetailViewModel: ShowDetailViewModel = hiltViewModel(),
    showDetailModel: ShowDetailModel = ShowDetailModel(),
    isFavorite: Boolean = false,
    isShowCoachMark: Boolean = false,
    onBack: () -> Unit = {}
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(276.dp)
                .background(CurtainCallTheme.colors.primary, RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp))
        ) {
            CurtainCallCenterTopAppBarWithBack(
                title = showDetailModel.name,
                containerColor = CurtainCallTheme.colors.primary,
                contentColor = CurtainCallTheme.colors.onPrimary,
                onBack = onBack
            )
        }
        ShowDetailCard(
            modifier = Modifier
                .padding(top = 76.dp)
                .padding(horizontal = 20.dp),
            showDetailModel = showDetailModel,
            isFavorite = isFavorite,
            onLikeClick = {
                showDetailViewModel.selectFavoriteShow(
                    showId = showDetailModel.id,
                    isFavorite = isFavorite.not()
                )
            }
        )
        if (isShowCoachMark) {
            CurtainCallShowLiveTalkCoachMark(
                modifier = Modifier.padding(top = 620.dp),
                text = stringResource(R.string.livetalk_coach_mark),
                onClick = { showDetailViewModel.closeCoachMark() }
            )
        }
    }
}
