package com.cmc.curtaincall.feature.partymember

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.cmc.curtaincall.common.designsystem.R
import com.cmc.curtaincall.common.designsystem.component.appbars.CurtainCallSearchTitleTopAppBarWithCalendar
import com.cmc.curtaincall.common.designsystem.component.appbars.SearchAppBarState
import com.cmc.curtaincall.common.designsystem.component.basic.SystemUiStatusBar
import com.cmc.curtaincall.common.designsystem.component.buttons.common.CurtainCallFilledButton
import com.cmc.curtaincall.common.designsystem.component.tooltip.CurtainCallPartyTooltip
import com.cmc.curtaincall.common.designsystem.custom.common.CurtainCallCalendar
import com.cmc.curtaincall.common.designsystem.custom.party.PartyContent
import com.cmc.curtaincall.common.designsystem.custom.party.PartyEmptyContent
import com.cmc.curtaincall.common.designsystem.custom.search.SearchWordContent
import com.cmc.curtaincall.common.designsystem.custom.search.SearchWordEmptyContent
import com.cmc.curtaincall.common.designsystem.theme.CurtainCallTheme
import com.cmc.curtaincall.common.designsystem.theme.Grey6
import com.cmc.curtaincall.common.designsystem.theme.Grey8
import com.cmc.curtaincall.common.designsystem.theme.White
import com.kizitonwose.calendar.core.CalendarDay
import java.time.format.DateTimeFormatter

@Composable
fun PartyMemberScreen(
    partyMemberViewModel: PartyMemberViewModel = hiltViewModel(),
    onNavigateToDetail: (Int?, String?) -> Unit = { _, _ -> },
    onNavigateToRecruit: () -> Unit = {}
) {
    var selectedCalendarDays by remember { mutableStateOf<List<CalendarDay>>(listOf()) }
    var isShowCalendar by remember { mutableStateOf(false) }
    val searchAppBarState by partyMemberViewModel.searchAppBarState.collectAsStateWithLifecycle()

    SystemUiStatusBar(Grey8)
    Scaffold(
        topBar = {
            CurtainCallSearchTitleTopAppBarWithCalendar(
                title = stringResource(R.string.party_member),
                searchAppBarState = searchAppBarState,
                containerColor = Grey8,
                selectedCalendarDays = selectedCalendarDays,
                onCalendarClick = { isShowCalendar = isShowCalendar.not() }
            )
        },
        floatingActionButton = {
            CurtainCallFilledButton(
                modifier = Modifier
                    .padding(bottom = 14.dp)
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth()
                    .height(52.dp),
                text = stringResource(R.string.party_member_recruitment),
                textStyle = CurtainCallTheme.typography.body2.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                onClick = {
                    onNavigateToRecruit()
                }
            )
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { paddingValues ->
        if (searchAppBarState.isSearchMode.value) {
            PartyMemberSearchContent(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .background(Grey8),
                searchAppBarState = searchAppBarState,
                onNavigateToDetail = onNavigateToDetail
            )
        } else {
            PartyMemberContent(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .background(Grey8),
                isShowCalendar = isShowCalendar,
                selectedCalendarDays = selectedCalendarDays,
                onSelectCalendarDays = {
                    selectedCalendarDays = it
                    isShowCalendar = false
                },
                onNavigateToDetail = onNavigateToDetail
            )
        }
    }
}

@Composable
private fun PartyMemberSearchContent(
    modifier: Modifier = Modifier,
    partyMemberViewModel: PartyMemberViewModel = hiltViewModel(),
    searchAppBarState: SearchAppBarState,
    onNavigateToDetail: (Int?, String?) -> Unit = { _, _ -> }
) {
    val partyMemberUiState by partyMemberViewModel.uiState.collectAsStateWithLifecycle()
    val searchWords = partyMemberUiState.partySearchWords
    val partyModels = partyMemberUiState.partyModels.collectAsLazyPagingItems()

    if (searchAppBarState.isDoneSearch.value) {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 30.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(partyModels.itemCount) { index ->
                partyModels[index]?.let { partyModel ->
                    PartyContent(
                        partyModel = partyModel,
                        onClick = { onNavigateToDetail(partyModel.id, partyModel.showName) }
                    )
                }
            }
        }
    } else {
        if (searchWords.isEmpty()) {
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
                        if (searchWords.isNotEmpty()) {
                            Text(
                                text = stringResource(R.string.delete_all_search_word),
                                modifier = Modifier.clickable {
                                    partyMemberViewModel.deleteAllShowSearchWord()
                                },
                                style = CurtainCallTheme.typography.body3.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = Grey6
                                )
                            )
                        }
                    }
                }
                items(searchWords) { searchWord ->
                    SearchWordContent(
                        text = searchWord.word,
                        onClose = { partyMemberViewModel.deletePartySearchWord(searchWord) },
                        onClick = { partyMemberViewModel.searchPartyModel(searchWord) }
                    )
                }
            }
        }
    }
}

@Composable
private fun PartyMemberContent(
    modifier: Modifier = Modifier,
    partyMemberViewModel: PartyMemberViewModel = hiltViewModel(),
    isShowCalendar: Boolean = false,
    selectedCalendarDays: List<CalendarDay> = listOf(),
    onSelectCalendarDays: (List<CalendarDay>) -> Unit = {},
    onNavigateToDetail: (Int?, String?) -> Unit = { _, _ -> }
) {
    val partyMemberUiState by partyMemberViewModel.uiState.collectAsStateWithLifecycle()
    val partyModels = partyMemberUiState.partyModels.collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        partyMemberViewModel.fetchPartyList()
    }

    Box(modifier) {
        if (partyMemberUiState.isShowTooltip) {
            CurtainCallPartyTooltip(
                modifier = Modifier
                    .padding(start = 20.dp)
                    .zIndex(1f),
                text = stringResource(R.string.party_member_tooltip),
                onClick = { partyMemberViewModel.hidePartyTooltip() }
            )
        }

        if (isShowCalendar) {
            CurtainCallCalendar(
                modifier = Modifier
                    .zIndex(1f)
                    .align(Alignment.TopCenter)
                    .width(320.dp),
                prevSelectedDays = selectedCalendarDays,
                onSelectDays = {
                    onSelectCalendarDays(it)
                    partyMemberViewModel.fetchPartyList(
                        startDate = it.getOrNull(0)?.date?.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                        endDate = it.getOrNull(1)?.date?.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                    )
                },
                hasClear = true
            )
        }

        if (partyModels.itemCount == 0) {
            Column(modifier = Modifier.fillMaxSize()) {
                Spacer(Modifier.weight(200f))
                PartyEmptyContent(Modifier.align(Alignment.CenterHorizontally))
                Spacer(Modifier.weight(308f))
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 120.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(partyModels.itemCount) { index ->
                    partyModels[index]?.let { partyModel ->
                        PartyContent(
                            partyModel = partyModel,
                            onClick = { onNavigateToDetail(partyModel.id, partyModel.showName) }
                        )
                    }
                }
            }

            val brush = Brush.verticalGradient(listOf(White.copy(alpha = 0f), White.copy(alpha = 1f)))
            Canvas(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(100.dp)
                    .blur(radius = 2.dp),
                onDraw = {
                    drawRect(brush)
                }
            )
        }
    }
}
