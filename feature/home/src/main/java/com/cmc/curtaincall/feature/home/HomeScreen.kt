package com.cmc.curtaincall.feature.home

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.cmc.curtaincall.common.designsystem.R
import com.cmc.curtaincall.common.designsystem.component.appbars.CurtainCallTitleTopAppBar
import com.cmc.curtaincall.common.designsystem.component.basic.SystemUiStatusBar
import com.cmc.curtaincall.common.designsystem.component.tooltip.CurtainCallCostEffectiveShowTooltip
import com.cmc.curtaincall.common.designsystem.custom.poster.CurtainCallCostEffectiveShowPoster
import com.cmc.curtaincall.common.designsystem.custom.poster.CurtainCallEndShowPoster
import com.cmc.curtaincall.common.designsystem.custom.poster.CurtainCallOpenShowPoster
import com.cmc.curtaincall.common.designsystem.custom.poster.CurtainCallPopularPoster
import com.cmc.curtaincall.common.designsystem.dimension.Paddings
import com.cmc.curtaincall.common.designsystem.theme.*
import com.cmc.curtaincall.common.utility.extensions.convertDDay
import com.cmc.curtaincall.common.utility.extensions.convertSimpleDate
import com.cmc.curtaincall.common.utility.extensions.toChangeDate
import com.cmc.curtaincall.domain.enums.translateShowGenreType
import com.cmc.curtaincall.domain.model.show.ShowRecommendationModel
import io.getstream.chat.android.client.ChatClient

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel(),
    chatClient: ChatClient,
    onNavigateToPerformanceDetail: (String) -> Unit,
    onNavigateLiveTalk: () -> Unit = {},
    onNavigatePartyMember: () -> Unit = {},
    onNavigateMyPage: () -> Unit = {}
) {
    LaunchedEffect(Unit) {
        homeViewModel.connectChattingClient(chatClient)
    }

    SystemUiStatusBar(Grey8)
    Scaffold(
        topBar = {
            CurtainCallTitleTopAppBar(containerColor = Grey8)
        }
    ) { paddingValues ->
        HomeContent(
            homeViewModel = homeViewModel,
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(CurtainCallTheme.colors.background),
            onNavigateToPerformanceDetail = onNavigateToPerformanceDetail
        )
    }
}

@Composable
private fun HomeContent(
    homeViewModel: HomeViewModel,
    modifier: Modifier = Modifier,
    onNavigateToPerformanceDetail: (String) -> Unit
) {
    val scrollState = rememberScrollState()
    val homeUiState by homeViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(homeViewModel) {
        homeViewModel.requestShowRecommendations()
        homeViewModel.requestPopularShowList()
        homeViewModel.requestOpenShowList()
        homeViewModel.requestEndShowList()
        homeViewModel.requestCostEffectiveShows()
    }

    Column(modifier.verticalScroll(scrollState)) {
        HomeBannerScreen(
            modifier = Modifier
                .fillMaxWidth()
                .height(366.dp)
                .background(Grey8),
            showRecommendations = homeUiState.showRecommendations,
            onNavigateToPerformanceDetail = onNavigateToPerformanceDetail
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(White)
        ) {
            if (homeUiState.showRanks.isNotEmpty()) {
                HomeContentsLazyRow(
                    modifier = Modifier
                        .padding(top = 40.dp)
                        .fillMaxWidth(),
                    text = stringResource(R.string.home_contents_popular_show)
                ) {
                    itemsIndexed(homeUiState.showRanks) { index, showRank ->
                        CurtainCallPopularPoster(
                            model = showRank.poster,
                            text = showRank.name,
                            rank = index + 1,
                            genreType = translateShowGenreType(showRank.genre),
                            onClick = { onNavigateToPerformanceDetail(showRank.id) }
                        )
                    }
                }
            }
            if (homeUiState.openShowInfos.isNotEmpty()) {
                HomeContentsLazyRow(
                    modifier = Modifier
                        .padding(top = 50.dp)
                        .fillMaxWidth(),
                    text = stringResource(R.string.home_contents_open_show)
                ) {
                    itemsIndexed(homeUiState.openShowInfos) { index, openShowInfo ->
                        CurtainCallOpenShowPoster(
                            model = openShowInfo.poster,
                            text = openShowInfo.name,
                            dDay = if (openShowInfo.startDate.convertDDay() == 0L) "D-Day" else "D${openShowInfo.startDate.convertDDay()}",
                            openDate = openShowInfo.startDate.convertSimpleDate(),
                            genreType = translateShowGenreType(openShowInfo.genre),
                            onClick = { onNavigateToPerformanceDetail(openShowInfo.id) }
                        )
                    }
                }
            }
            if (homeUiState.endShowInfos.isNotEmpty()) {
                HomeContentsLazyRow(
                    modifier = Modifier
                        .padding(top = 50.dp)
                        .fillMaxWidth(),
                    text = stringResource(R.string.home_contents_end_show)
                ) {
                    itemsIndexed(homeUiState.endShowInfos) { index, endShowInfo ->
                        CurtainCallEndShowPoster(
                            model = endShowInfo.poster,
                            text = endShowInfo.name,
                            dDay = if (endShowInfo.endDate.convertDDay() == 0L) "D-Day" else "D${endShowInfo.endDate.convertDDay()}",
                            endDate = endShowInfo.endDate.convertSimpleDate(),
                            genreType = translateShowGenreType(endShowInfo.genre),
                            onClick = { onNavigateToPerformanceDetail(endShowInfo.id) }
                        )
                    }
                }
            }
            if (homeUiState.costEffectiveShows.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .padding(top = 50.dp)
                        .fillMaxWidth()
                ) {
                    HomeContentsLazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.home_contents_cost_effective)
                    ) {
                        items(homeUiState.costEffectiveShows) { costEffectiveShow ->
                            CurtainCallCostEffectiveShowPoster(
                                model = costEffectiveShow.poster,
                                name = costEffectiveShow.name,
                                minPrice = costEffectiveShow.minTicketPrice,
                                genreType = translateShowGenreType(costEffectiveShow.genre),
                                onClick = { onNavigateToPerformanceDetail(costEffectiveShow.id) }
                            )
                        }
                    }

                    if (homeUiState.isShowTooltip) {
                        CurtainCallCostEffectiveShowTooltip(
                            modifier = Modifier.padding(start = 20.dp, top = 28.dp),
                            text = stringResource(R.string.home_cost_effective_tooltip),
                            onClick = { homeViewModel.hideCostEffectiveTooltip() }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(42.dp))
        }
    }
}

@Composable
private fun HomeContentsLazyRow(
    modifier: Modifier = Modifier,
    text: String,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(12.dp),
    content: LazyListScope.() -> Unit = {}
) {
    Column(modifier) {
        Text(
            text = text,
            modifier = Modifier.padding(start = 20.dp),
            style = CurtainCallTheme.typography.subTitle3
        )
        LazyRow(
            modifier = Modifier
                .padding(top = 12.dp)
                .fillMaxWidth(),
            horizontalArrangement = horizontalArrangement,
            contentPadding = PaddingValues(horizontal = 20.dp)
        ) {
            content()
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun HomeBannerScreen(
    modifier: Modifier = Modifier,
    showRecommendations: List<ShowRecommendationModel>,
    onNavigateToPerformanceDetail: (String) -> Unit = {}
) {
    val pagerState = rememberPagerState { showRecommendations.size + 1 }
    HorizontalPager(
        state = pagerState,
        modifier = modifier
    ) { position ->
        if (position == 0) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 20.dp)
                    .fillMaxSize()
                    .clip(RoundedCornerShape(14.dp))
                    .background(CurtainCallTheme.colors.primary)
                    .padding(top = 20.dp, bottom = 30.dp, start = 24.dp, end = 20.dp)
            ) {
                Row(Modifier.fillMaxWidth()) {
                    Image(
                        painter = painterResource(R.drawable.ic_logo),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(top = 10.dp)
                            .size(56.dp)
                    )
                    Spacer(Modifier.weight(1f))
                    Box(
                        modifier = Modifier
                            .background(CurtainCallTheme.colors.background.copy(alpha = 0.2f), RoundedCornerShape(20.dp))
                            .padding(vertical = 2.dp, horizontal = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = String.format("%d/%d", 1, pagerState.pageCount),
                            style = CurtainCallTheme.typography.body5.copy(
                                color = White
                            )
                        )
                    }
                }
                Spacer(Modifier.weight(1f))
                Text(
                    text = stringResource(R.string.home_banner_renewal_description),
                    style = CurtainCallTheme.typography.body4.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = White
                    )
                )
                Text(
                    text = stringResource(R.string.home_banner_renewal),
                    modifier = Modifier.padding(top = 20.dp),
                    style = CurtainCallTheme.typography.h2.copy(
                        color = CurtainCallTheme.colors.secondary
                    )
                )
            }
        } else {
            val brush = Brush.verticalGradient(listOf(Black.copy(alpha = 0f), Black.copy(alpha = 0.2f)))
            val showRecommendation = showRecommendations[position - 1]
            Box(
                modifier = Modifier
                    .clickable { onNavigateToPerformanceDetail(showRecommendations[position - 1].showId) }
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 20.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .fillMaxSize()
            ) {
                AsyncImage(
                    model = showRecommendation.poster,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .blur(80.dp),
                    contentScale = ContentScale.FillBounds
                )
                Canvas(
                    modifier = Modifier.fillMaxSize(),
                    onDraw = {
                        drawRect(brush)
                    }
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 20.dp, end = 20.dp)
                        .background(CurtainCallTheme.colors.background.copy(alpha = 0.2f), RoundedCornerShape(20.dp))
                        .padding(vertical = 2.dp, horizontal = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = String.format("%d/%d", position + 1, pagerState.pageCount),
                        style = CurtainCallTheme.typography.body5.copy(
                            color = White
                        )
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AsyncImage(
                        model = showRecommendation.poster,
                        contentDescription = null,
                        modifier = Modifier
                            .size(137.dp, 182.dp)
                            .clip(RoundedCornerShape(10.dp)),
                        contentScale = ContentScale.FillBounds
                    )
                    Row(
                        modifier = Modifier.padding(top = 16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .background(CurtainCallTheme.colors.secondary, RoundedCornerShape(20.dp))
                                .padding(vertical = 2.dp, horizontal = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(R.string.home_banner_recommendation),
                                style = CurtainCallTheme.typography.caption.copy(
                                    color = CurtainCallTheme.colors.primary
                                )
                            )
                        }
                        Box(
                            modifier = Modifier
                                .padding(start = 4.dp)
                                .background(CurtainCallTheme.colors.primary, RoundedCornerShape(20.dp))
                                .padding(vertical = 2.dp, horizontal = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = translateShowGenreType(showRecommendation.genre).value,
                                style = CurtainCallTheme.typography.caption.copy(
                                    color = CurtainCallTheme.colors.onPrimary
                                )
                            )
                        }
                    }
                    Text(
                        text = showRecommendation.description,
                        modifier = Modifier.padding(top = Paddings.xlarge),
                        style = CurtainCallTheme.typography.body5.copy(
                            color = White
                        ),
                        maxLines = 1
                    )
                    Text(
                        text = showRecommendation.name,
                        modifier = Modifier.padding(top = Paddings.xsmall),
                        style = CurtainCallTheme.typography.subTitle2.copy(
                            color = White
                        ),
                        maxLines = 1
                    )
                    Spacer(Modifier.weight(1f))
                    Text(
                        text = String.format("%s - %s", showRecommendation.startDate.toChangeDate(), showRecommendation.endDate.toChangeDate()),
                        style = CurtainCallTheme.typography.caption.copy(
                            color = White
                        ),
                        maxLines = 1
                    )
                }
            }
        }
    }
}
