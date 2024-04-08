package com.cmc.curtaincall.feature.partymember.recruit.screen

import android.view.MotionEvent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.cmc.curtaincall.common.designsystem.R
import com.cmc.curtaincall.common.designsystem.custom.common.CurtainCallCalendar
import com.cmc.curtaincall.common.designsystem.theme.CurtainCallTheme
import com.cmc.curtaincall.common.designsystem.theme.Grey1
import com.cmc.curtaincall.common.designsystem.theme.Grey5
import com.cmc.curtaincall.common.designsystem.theme.Grey6
import com.cmc.curtaincall.common.designsystem.theme.Grey9
import com.cmc.curtaincall.common.utility.extensions.convertDefaultDate
import com.cmc.curtaincall.domain.model.show.ShowTimeModel
import com.cmc.curtaincall.feature.partymember.recruit.PartyMemberRecruitViewModel
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ColumnScope.PartyMemberRecruitSecondScreen(
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
