package com.cmc.curtaincall.feature.partymember.create.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cmc.curtaincall.common.designsystem.R
import com.cmc.curtaincall.common.designsystem.component.basic.CurtainCallBorderText
import com.cmc.curtaincall.common.designsystem.extensions.toSp
import com.cmc.curtaincall.common.designsystem.theme.Arsenic
import com.cmc.curtaincall.common.designsystem.theme.Black_Pearl
import com.cmc.curtaincall.common.designsystem.theme.Chinese_Black
import com.cmc.curtaincall.common.designsystem.theme.Cultured
import com.cmc.curtaincall.common.designsystem.theme.Dark_Slate_Grey
import com.cmc.curtaincall.common.designsystem.theme.Eerie_Black
import com.cmc.curtaincall.common.designsystem.theme.Me_Pink
import com.cmc.curtaincall.common.designsystem.theme.Nero
import com.cmc.curtaincall.common.designsystem.theme.Roman_Silver
import com.cmc.curtaincall.common.designsystem.theme.Silver_Sand
import com.cmc.curtaincall.common.designsystem.theme.White
import com.cmc.curtaincall.common.designsystem.theme.spoqahansanseeo
import com.cmc.curtaincall.common.utility.extensions.getTimes
import com.cmc.curtaincall.feature.partymember.create.PartyMemberCreateViewModel

fun LazyGridScope.showPerformanceSecondStep(
    partyMemberCreateViewModel: PartyMemberCreateViewModel,
    modifier: Modifier = Modifier,
    selectedDate: String,
    selectedTime: String,
    personnelCount: Int,
    onSelectDate: (String) -> Unit,
    onSelectTime: (String) -> Unit,
    onChangePersonnelCount: (Int) -> Unit
) {
    item(span = { GridItemSpan(3) }) {
        val partyMemberCreateUiState by partyMemberCreateViewModel.uiState.collectAsStateWithLifecycle()
        Box(modifier = modifier.padding(top = 15.dp)) {
            var isClickedDate by remember { mutableStateOf(false) }
            var isClickedTime by remember { mutableStateOf(false) }
            SelectInput(
                modifier = Modifier.fillMaxWidth(),
                description = stringResource(R.string.partymember_create_select_performance_date),
                subDescription = stringResource(R.string.partymember_create_select_date),
                input = selectedDate,
                isClicked = isClickedDate,
                onClick = {
                    isClickedDate = isClickedDate.not()
                    if (isClickedDate) {
                        isClickedTime = false
                        onSelectTime("")
                    }
                }
            ) {
//                SelectedDateCalender(
//                    modifier = Modifier.padding(top = 10.dp),
//                    calendarDays = getCalendarDays(
//                        partyMemberCreateUiState.showDetailModel.startDate,
//                        partyMemberCreateUiState.showDetailModel.endDate
//                    ),
//                    onDateClick = {
//                        onSelectDate(
//                            String.format(
//                                "%d. %02d. %02d",
//                                it.date.year,
//                                it.date.month.value,
//                                it.date.dayOfMonth
//                            )
//                        )
//                        isClickedDate = false
//                    }
//                )
            }

            SelectInput(
                modifier = Modifier.padding(top = 115.dp),
                description = stringResource(R.string.partymember_create_select_time),
                subDescription = stringResource(R.string.partymember_create_select_time),
                input = selectedTime,
                enabled = selectedDate.isNotEmpty(),
                isClicked = isClickedTime,
                onClick = {
                    isClickedTime = isClickedTime.not()
                    if (isClickedTime) {
                        isClickedDate = false
                    }
                }
            ) {
                DropDownTime(
                    modifier = Modifier.padding(top = 10.dp),
                    times = selectedDate.getTimes(partyMemberCreateUiState.showDetailModel.showTimes),
                    onClick = {
                        onSelectTime(it)
                        isClickedTime = false
                    }
                )
            }

            SelectPersonnelButton(
                modifier = Modifier.padding(top = 232.dp),
                personnelCount = personnelCount,
                maxCount = 10,
                onClick = onChangePersonnelCount
            )
        }
    }
}

@Composable
private fun DropDownTime(
    modifier: Modifier = Modifier,
    times: List<String> = listOf(),
    onClick: (String) -> Unit = {}
) {
    val scrollState = rememberScrollState()
    Card(
        modifier = modifier.padding(bottom = 10.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 20.dp)
    ) {
        Column(
            modifier = Modifier
                .heightIn(max = 232.dp)
                .verticalScroll(scrollState)
        ) {
            times.sorted().forEach { time ->
                Text(
                    text = time,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 24.dp)
                        .padding(vertical = 18.dp)
                        .clickable { onClick(time) },
                    color = Dark_Slate_Grey,
                    fontSize = 16.dp.toSp(),
                    fontWeight = FontWeight.Medium,
                    fontFamily = spoqahansanseeo
                )
            }
        }
    }
}

@Composable
private fun SelectInput(
    modifier: Modifier = Modifier,
    description: String,
    subDescription: String,
    input: String,
    enabled: Boolean = true,
    isClicked: Boolean,
    onClick: () -> Unit = {},
    content: @Composable () -> Unit = {}
) {
    Column(modifier = modifier.zIndex(if (isClicked) 1f else 0f)) {
        Row {
            Text(
                text = description,
                modifier = Modifier.weight(1f),
                color = Black_Pearl,
                fontSize = 18.dp.toSp(),
                fontWeight = FontWeight.Bold,
                fontFamily = spoqahansanseeo
            )
            CurtainCallBorderText(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                text = stringResource(R.string.partymember_create_essential),
                borderColor = Me_Pink,
                contentColor = Me_Pink,
                fontSize = 13.dp.toSp(),
                radiusSize = 20.dp
            )
        }
        Box(
            modifier = Modifier
                .padding(top = 10.dp)
                .fillMaxWidth()
                .background(Cultured, RoundedCornerShape(10.dp))
                .border(1.dp, if (isClicked) Roman_Silver else Color.Transparent, RoundedCornerShape(10.dp))
                .clickable { if (enabled) onClick() }
                .padding(horizontal = 18.dp)
                .padding(vertical = 10.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = input.ifEmpty { subDescription },
                color = if (input.isEmpty()) Silver_Sand else Nero,
                fontSize = 15.dp.toSp(),
                fontWeight = FontWeight.Medium,
                fontFamily = spoqahansanseeo
            )
        }
        if (isClicked) {
            content()
        }
    }
}

@Composable
private fun PersonnelButton(
    modifier: Modifier = Modifier
) {
    var personnel by remember { mutableStateOf(1) }
    Column(modifier = modifier) {
        Row {
            Text(
                text = stringResource(R.string.partymember_create_select_personnel),
                color = Eerie_Black,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = spoqahansanseeo
            )
            Spacer(Modifier.weight(1f))
            Surface(
                modifier = Modifier.size(38.dp, 20.dp),
                shape = RoundedCornerShape(20.dp),
                color = White,
                border = BorderStroke(1.dp, Me_Pink)
            ) {
                Text(
                    text = stringResource(R.string.partymember_create_essential),
                    modifier = Modifier.padding(vertical = 2.dp, horizontal = 8.dp),
                    color = Me_Pink,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = spoqahansanseeo
                )
            }
        }

        Row(
            modifier = Modifier
                .padding(top = 16.dp)
                .height(40.dp)
        ) {
            IconButton(
                onClick = { personnel-- },
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(topStart = 10.dp, bottomStart = 10.dp)),
                enabled = personnel > 1,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Cultured,
                    disabledContainerColor = Cultured
                )
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_minus),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .padding(horizontal = 42.dp),
                    tint = Arsenic
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = personnel.toString(),
                    color = Chinese_Black,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = spoqahansanseeo
                )
            }

            IconButton(
                onClick = { personnel++ },
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(topEnd = 10.dp, bottomEnd = 10.dp)),
                enabled = personnel < Int.MAX_VALUE,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Cultured,
                    disabledContainerColor = Cultured
                )
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_plus),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .padding(horizontal = 42.dp),
                    tint = Arsenic
                )
            }
        }
    }
}
