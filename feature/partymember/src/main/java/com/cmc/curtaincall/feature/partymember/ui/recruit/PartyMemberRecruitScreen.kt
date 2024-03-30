package com.cmc.curtaincall.feature.partymember.ui.recruit

import android.view.MotionEvent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.cmc.curtaincall.common.designsystem.R
import com.cmc.curtaincall.common.designsystem.component.appbars.CurtainCallSearchTitleTopAppBarWithBack
import com.cmc.curtaincall.common.designsystem.component.basic.SystemUiStatusBar
import com.cmc.curtaincall.common.designsystem.component.buttons.common.CurtainCallFilledButton
import com.cmc.curtaincall.common.designsystem.component.chips.CurtainCallBasicChip
import com.cmc.curtaincall.common.designsystem.component.sheets.bottom.CurtainCallShowSortBottomSheet
import com.cmc.curtaincall.common.designsystem.component.tooltip.CurtainCallShowSortTooltip
import com.cmc.curtaincall.common.designsystem.custom.common.CurtainCallCalendar
import com.cmc.curtaincall.common.designsystem.custom.poster.CurtainCallTitlePoster
import com.cmc.curtaincall.common.designsystem.theme.CurtainCallTheme
import com.cmc.curtaincall.common.designsystem.theme.Grey1
import com.cmc.curtaincall.common.designsystem.theme.Grey5
import com.cmc.curtaincall.common.designsystem.theme.Grey6
import com.cmc.curtaincall.common.designsystem.theme.Grey7
import com.cmc.curtaincall.common.designsystem.theme.Grey9
import com.cmc.curtaincall.common.utility.extensions.convertDefaultDate
import com.cmc.curtaincall.domain.enums.ShowGenreType
import com.cmc.curtaincall.domain.enums.ShowSortType
import com.cmc.curtaincall.domain.model.show.ShowTimeModel
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun PartyMemberRecruitScreen(
    partyMemberRecruitViewModel: PartyMemberRecruitViewModel = hiltViewModel(),
    onBack: () -> Unit = {}
) {
    val partyMemberRecruitUiState by partyMemberRecruitViewModel.uiState.collectAsStateWithLifecycle()
    SystemUiStatusBar(CurtainCallTheme.colors.background)
    Scaffold(
        topBar = {
            CurtainCallSearchTitleTopAppBarWithBack(
                title = stringResource(R.string.party_member_recruit),
                onBack = {
                    when (partyMemberRecruitUiState.phrase) {
                        1 -> onBack()
                        2 -> {
                            partyMemberRecruitViewModel.movePhrase(partyMemberRecruitUiState.phrase - 1)
                            partyMemberRecruitViewModel.clearSelection()
                        }

                        else -> {
                            partyMemberRecruitViewModel.movePhrase(partyMemberRecruitUiState.phrase - 1)
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            CurtainCallFilledButton(
                text = stringResource(R.string.next),
                modifier = Modifier
                    .padding(bottom = 30.dp)
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth()
                    .height(51.dp),
                containerColor = CurtainCallTheme.colors.primary,
                contentColor = CurtainCallTheme.colors.onPrimary,
                enabled = when (partyMemberRecruitUiState.phrase) {
                    1 -> {
                        partyMemberRecruitUiState.showId != null
                    }

                    2 -> {
                        partyMemberRecruitUiState.selectShowDate.isNotEmpty() &&
                            partyMemberRecruitUiState.selectShowTime.isNotEmpty()
                    }

                    else -> {
                        // TODO
                        false
                    }
                },
                onClick = {
                    if (partyMemberRecruitUiState.phrase < 3) {
                        partyMemberRecruitViewModel.movePhrase(partyMemberRecruitUiState.phrase + 1)
                    } else {
                        // TODO
                    }
                }
            )
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { paddingValues ->
        PartyMemberRecruitContent(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(CurtainCallTheme.colors.background),
            partyMemberRecruitUiState = partyMemberRecruitUiState
        )
    }
}

@Composable
private fun PartyMemberRecruitContent(
    modifier: Modifier = Modifier,
    partyMemberRecruitUiState: PartyMemberRecruitUiState
) {
    Column(modifier) {
        PartyMemberRecruitPhrase(
            modifier = Modifier
                .padding(top = 20.dp)
                .fillMaxWidth(),
            phrase = partyMemberRecruitUiState.phrase
        )
        when (partyMemberRecruitUiState.phrase) {
            1 -> {
                PartyMemberRecruitFirstContent(
                    sortType = partyMemberRecruitUiState.sortType,
                    genreType = partyMemberRecruitUiState.genreType,
                    isShowTooltip = partyMemberRecruitUiState.isShowTooltip
                )
            }

            else -> {
                PartyMemberRecruitSecondContent(
                    showStartDate = partyMemberRecruitUiState.showStartDate,
                    showEndDate = partyMemberRecruitUiState.showEndDate,
                    showTimes = partyMemberRecruitUiState.showTimes,
                    selectShowDate = partyMemberRecruitUiState.selectShowDate,
                    selectShowTime = partyMemberRecruitUiState.selectShowTime,
                    selectMemberNumber = partyMemberRecruitUiState.selectMemberNumber
                )
            }
        }
    }
}

@Composable
private fun ColumnScope.PartyMemberRecruitFirstContent(
    partyMemberRecruitViewModel: PartyMemberRecruitViewModel = hiltViewModel(),
    sortType: ShowSortType,
    genreType: ShowGenreType,
    isShowTooltip: Boolean
) {
    val uiState by partyMemberRecruitViewModel.uiState.collectAsStateWithLifecycle()
    val showInfoModels = uiState.showInfoModels.collectAsLazyPagingItems()
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
                onClick = { partyMemberRecruitViewModel.hideTooltip() }
            )
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 49.dp),
            contentPadding = PaddingValues(20.dp),
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
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun ColumnScope.PartyMemberRecruitSecondContent(
    partyMemberRecruitViewModel: PartyMemberRecruitViewModel = hiltViewModel(),
    showStartDate: String = "",
    showEndDate: String = "",
    showTimes: List<ShowTimeModel> = listOf(),
    selectShowDate: String = "",
    selectShowTime: String = "",
    selectMemberNumber: Int = 2,
) {
    var isDateFocused by remember { mutableStateOf(false) }
    var isTimeFocused by remember { mutableStateOf(false) }
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = stringResource(R.string.party_member_show_date_guide),
            modifier = Modifier.padding(top = 30.dp, start = 20.dp),
            style = CurtainCallTheme.typography.subTitle4
        )
        Box(
            modifier = Modifier
                .padding(top = 64.dp)
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
                .height(44.dp)
                .background(Grey9, RoundedCornerShape(10.dp))
                .then(
                    if (isDateFocused) {
                        Modifier.border(1.dp, Grey5, RoundedCornerShape(10.dp))
                    } else {
                        Modifier
                    }
                )
                .clickable {
                    isDateFocused = isDateFocused.not()
                    isTimeFocused = false
                }
                .padding(horizontal = 12.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = selectShowDate.convertDefaultDate().ifEmpty { stringResource(R.string.party_member_show_date_placeholder) },
                style = CurtainCallTheme.typography.body3.copy(
                    color = if (selectShowDate.isEmpty()) Grey6 else Grey1
                )
            )
        }
        if (isDateFocused) {
            CurtainCallCalendar(
                modifier = Modifier
                    .padding(top = 120.dp)
                    .zIndex(1f)
                    .align(Alignment.TopCenter)
                    .width(320.dp),
                onSelectDays = {
                    partyMemberRecruitViewModel.selectShowDate(it.first())
                    isDateFocused = false
                },
                startDay = if (showStartDate.isEmpty()) null else CalendarDay(LocalDate.parse(showStartDate, DateTimeFormatter.ISO_DATE), DayPosition.MonthDate),
                endDay = if (showStartDate.isEmpty()) null else CalendarDay(LocalDate.parse(showEndDate, DateTimeFormatter.ISO_DATE), DayPosition.MonthDate),
                count = 1,
                canSelectDayOfWeeks = showTimes
                    .filter { it.dayOfWeek != "HOL" }
                    .map { DayOfWeek.valueOf(it.dayOfWeek) }
            )
        }
        Text(
            text = stringResource(R.string.party_member_show_time_guide),
            modifier = Modifier.padding(top = 148.dp, start = 20.dp),
            style = CurtainCallTheme.typography.subTitle4
        )
        Box(
            modifier = Modifier
                .padding(top = 182.dp)
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
                .height(44.dp)
                .background(Grey9, RoundedCornerShape(10.dp))
                .then(
                    if (isTimeFocused) {
                        Modifier.border(1.dp, Grey5, RoundedCornerShape(10.dp))
                    } else {
                        Modifier
                    }
                )
                .clickable {
                    if (selectShowDate.isNotEmpty()) {
                        isTimeFocused = isTimeFocused.not()
                        isDateFocused = false
                    }
                }
                .padding(horizontal = 12.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = if (selectShowTime.isNotEmpty()) {
                    selectShowTime.substring(0, 5)
                } else {
                    stringResource(R.string.party_member_show_time_guide)
                },
                style = CurtainCallTheme.typography.body3.copy(
                    color = if (selectShowTime.isEmpty()) Grey6 else Grey1
                )
            )
        }
        if (isTimeFocused) {
            Card(
                modifier = Modifier
                    .zIndex(1f)
                    .padding(top = 238.dp)
                    .align(Alignment.TopCenter)
                    .width(320.dp)
                    .heightIn(max = 162.dp),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(
                    containerColor = CurtainCallTheme.colors.background
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 10.dp
                )
            ) {
                val times = showTimes.filter { it.dayOfWeek == LocalDate.parse(selectShowDate, DateTimeFormatter.ISO_DATE).dayOfWeek.name }
                var touchItem: Int by remember { mutableStateOf(Int.MIN_VALUE) }
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    itemsIndexed(times) { index, time ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp)
                                .background(
                                    if (touchItem == index) {
                                        Grey9
                                    } else {
                                        CurtainCallTheme.colors.background
                                    }
                                )
                                .pointerInteropFilter { event ->
                                    when (event.action) {
                                        MotionEvent.ACTION_DOWN -> {
                                            touchItem = index
                                        }

                                        MotionEvent.ACTION_UP -> {
                                            touchItem = Int.MIN_VALUE
                                            partyMemberRecruitViewModel.selectShowTime(time.time)
                                            isTimeFocused = false
                                        }

                                        else -> return@pointerInteropFilter false
                                    }
                                    true
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = time.time.substring(0, 5),
                                style = CurtainCallTheme.typography.subTitle4
                            )
                        }
                    }
                }
            }
        }
        Text(
            text = stringResource(R.string.party_member_show_member_number_guide),
            modifier = Modifier.padding(top = 266.dp, start = 20.dp),
            style = CurtainCallTheme.typography.subTitle4
        )
        Row(
            modifier = Modifier
                .padding(top = 300.dp)
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
                .height(44.dp)
                .clip(RoundedCornerShape(10.dp))
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(CurtainCallTheme.colors.secondary)
                    .clickable {
                        if (selectMemberNumber > 2) {
                            partyMemberRecruitViewModel.changeMemberNumber(selectMemberNumber - 1)
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_remove_member_number),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = Color.Unspecified
                )
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(Grey9),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = selectMemberNumber.toString(),
                    style = CurtainCallTheme.typography.subTitle2
                )
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(CurtainCallTheme.colors.secondary)
                    .clickable {
                        if (selectMemberNumber < 100) {
                            partyMemberRecruitViewModel.changeMemberNumber(selectMemberNumber + 1)
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_add_member_number),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = Color.Unspecified
                )
            }
        }
    }
}

@Composable
private fun PartyMemberRecruitPhrase(
    modifier: Modifier = Modifier,
    phrase: Int
) {
    Column(modifier) {
        Row(
            modifier = Modifier
                .padding(start = 30.dp, end = 39.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(
                    if (phrase > 1) R.drawable.ic_number_check else R.drawable.ic_first
                ),
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = Color.Unspecified
            )
            Spacer(
                modifier = Modifier
                    .size(120.dp, 1.dp)
                    .background(if (phrase > 1) CurtainCallTheme.colors.primary else Grey7)
            )
            Icon(
                painter = painterResource(
                    if (phrase > 2) {
                        R.drawable.ic_number_check
                    } else if (phrase == 2) {
                        R.drawable.ic_second
                    } else {
                        R.drawable.ic_second_unhightlighted
                    }
                ),
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = Color.Unspecified
            )
            Spacer(
                modifier = Modifier
                    .size(111.dp, 1.dp)
                    .background(if (phrase > 2) CurtainCallTheme.colors.primary else Grey7)
            )
            Icon(
                painter = painterResource(
                    if (phrase >= 3) R.drawable.ic_third else R.drawable.ic_third_unhighlighted
                ),
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = Color.Unspecified
            )
        }
        Row(
            modifier = Modifier
                .padding(top = 4.dp)
                .padding(start = 28.dp, end = 24.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.party_member_first_step),
                style = CurtainCallTheme.typography.body4.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = CurtainCallTheme.colors.primary
                )
            )
            Text(
                text = stringResource(R.string.party_member_second_step),
                modifier = Modifier.padding(start = 104.dp),
                style = CurtainCallTheme.typography.body4.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = if (phrase >= 2) CurtainCallTheme.colors.primary else Grey7
                )
            )
            Text(
                text = stringResource(R.string.party_member_third_step),
                modifier = Modifier.padding(start = 81.dp),
                style = CurtainCallTheme.typography.body4.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = if (phrase >= 3) CurtainCallTheme.colors.primary else Grey7
                )
            )
        }
    }
}
