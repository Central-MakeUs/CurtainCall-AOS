package com.cmc.curtaincall.feature.partymember.create.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import com.cmc.curtaincall.common.designsystem.R
import com.cmc.curtaincall.common.designsystem.component.basic.CurtainCallBorderText
import com.cmc.curtaincall.common.designsystem.component.basic.CurtainCallRoundedText
import com.cmc.curtaincall.common.designsystem.component.basic.CurtainCallSelectTypeButton
import com.cmc.curtaincall.common.designsystem.component.card.PerformanceSimpleCard
import com.cmc.curtaincall.common.designsystem.component.row.SortTypeRow
import com.cmc.curtaincall.common.designsystem.component.custom.SelectSortTypeBottomSheet
import com.cmc.curtaincall.common.designsystem.component.custom.SelectedDateCalender
import com.cmc.curtaincall.common.designsystem.extensions.toSp
import com.cmc.curtaincall.common.designsystem.theme.Arsenic
import com.cmc.curtaincall.common.designsystem.theme.Black_Pearl
import com.cmc.curtaincall.common.designsystem.theme.Chinese_Black
import com.cmc.curtaincall.common.designsystem.theme.Cultured
import com.cmc.curtaincall.common.designsystem.theme.Me_Pink
import com.cmc.curtaincall.common.designsystem.theme.Nero
import com.cmc.curtaincall.common.designsystem.theme.Roman_Silver
import com.cmc.curtaincall.common.designsystem.theme.Silver_Sand
import com.cmc.curtaincall.common.designsystem.theme.White
import com.cmc.curtaincall.common.designsystem.theme.spoqahansanseeo
import com.cmc.curtaincall.domain.model.show.ShowInfoModel
import com.cmc.curtaincall.feature.partymember.create.PartyMemberCreateViewModel

fun LazyGridScope.showPerformanceFirstStep(
    partyMemberCreateViewModel: PartyMemberCreateViewModel,
    isCheckFirstType: Boolean,
    onTypeChange: (Boolean) -> Unit,
    playItems: LazyPagingItems<ShowInfoModel>,
    musicalItems: LazyPagingItems<ShowInfoModel>,
    selectedIndex: Int,
    onChangeSelect: (Int) -> Unit
) {
    item(span = { GridItemSpan(3) }) {
        val partyMemberCreateUiState by partyMemberCreateViewModel.uiState.collectAsStateWithLifecycle()
        var showDialog by remember { mutableStateOf(false) }

        if (showDialog) {
            SelectSortTypeBottomSheet(
                sortType = partyMemberCreateUiState.sortType,
                onSelectSortType = {
                    partyMemberCreateViewModel.setSortType(it)
                    onChangeSelect(-1)
                    showDialog = false
                },
                onDismissRequest = { showDialog = false }
            )
        }

        Column {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp)
            ) {
                Text(
                    text = stringResource(R.string.partymember_create_classification),
                    color = Black_Pearl,
                    fontSize = 18.dp.toSp(),
                    fontWeight = FontWeight.Bold,
                    fontFamily = spoqahansanseeo
                )
                CurtainCallSelectTypeButton(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth()
                        .height(45.dp),
                    firstType = stringResource(R.string.partymember_create_classification_theater),
                    lastType = stringResource(R.string.partymember_create_classification_musical),
                    isCheckFirstType = isCheckFirstType,
                    onTypeChange = onTypeChange
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(R.string.partymember_create_select_performance),
                        modifier = Modifier.weight(1f),
                        color = Black_Pearl,
                        fontSize = 18.dp.toSp(),
                        fontWeight = FontWeight.Bold,
                        fontFamily = spoqahansanseeo
                    )
                    CurtainCallBorderText(
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .padding(vertical = 4.dp),
                        text = stringResource(R.string.partymember_create_essential),
                        borderColor = Me_Pink,
                        contentColor = Me_Pink,
                        fontSize = 13.dp.toSp(),
                        radiusSize = 20.dp
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp, bottom = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SortTypeRow(
                        modifier = Modifier.weight(1f),
                        sortType = partyMemberCreateUiState.sortType,
                        onClick = { showDialog = true }
                    )
                    Text(
                        text = "8:00 업데이트",
                        color = Silver_Sand,
                        fontSize = 10.dp.toSp(),
                        fontWeight = FontWeight.Normal,
                        fontFamily = spoqahansanseeo
                    )
                }
            }
        }
    }

    if (isCheckFirstType) {
        items(playItems.itemCount) { index ->
            playItems[index]?.let { playItem ->
                PerformanceSimpleCard(
                    modifier = Modifier
                        .width(100.dp)
                        .padding(bottom = 16.dp),
                    imageUrl = playItem.poster,
                    title = playItem.name,
                    currentIndex = index,
                    selectedIndex = selectedIndex,
                    onChangeSelect = onChangeSelect
                )
            }
        }
    } else {
        items(musicalItems.itemCount) { index ->
            musicalItems[index]?.let { musicalItem ->
                PerformanceSimpleCard(
                    modifier = Modifier
                        .width(100.dp)
                        .padding(bottom = 16.dp),
                    imageUrl = musicalItem.poster,
                    title = musicalItem.name,
                    currentIndex = index,
                    selectedIndex = selectedIndex,
                    onChangeSelect = onChangeSelect
                )
            }
        }
    }
    item(span = { GridItemSpan(3) }) {
        Spacer(Modifier.size(70.dp))
    }
}

fun LazyGridScope.showEtcFirstStep(
    modifier: Modifier = Modifier,
    selectedDate: String,
    personnelCount: Int,
    clickedUndeterminedDate: Boolean,
    onSelectDate: (String) -> Unit,
    onClickUndeterminedDate: (Boolean) -> Unit,
    onClickPersonnel: (Int) -> Unit
) {
    item(span = { GridItemSpan(3) }) {
        var isClickedDate by remember { mutableStateOf(false) }
        Box(Modifier.padding(top = 15.dp)) {
            SelectDateWithButton(
                date = selectedDate,
                isClickedDate = isClickedDate,
                isClickedButton = clickedUndeterminedDate,
                onDateClick = { isClickedDate = it },
                onButtonClick = {
                    onClickUndeterminedDate(it)
                    onSelectDate("")
                },
                onCalenderClick = {
                    onSelectDate(it)
                    onClickUndeterminedDate(false)
                }
            )

            SelectPersonnelButton(
                modifier = Modifier.padding(top = 130.dp),
                personnelCount = personnelCount,
                maxCount = 100,
                onClick = onClickPersonnel
            )
        }
    }
}

@Composable
fun SelectPersonnelButton(
    modifier: Modifier = Modifier,
    personnelCount: Int,
    maxCount: Int = Int.MAX_VALUE,
    onClick: (Int) -> Unit
) {
    Column(modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(R.string.partymember_create_select_personnel),
                modifier = Modifier.weight(1f),
                color = Black_Pearl,
                fontSize = 18.dp.toSp(),
                fontWeight = FontWeight.Bold,
                fontFamily = spoqahansanseeo
            )
            CurtainCallBorderText(
                modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp),
                text = stringResource(R.string.partymember_create_essential),
                borderColor = Me_Pink,
                contentColor = Me_Pink,
                fontSize = 13.dp.toSp(),
                radiusSize = 20.dp
            )
        }

        Row(
            modifier = Modifier.padding(top = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(42.dp)
                    .background(Cultured, RoundedCornerShape(topStart = 10.dp, bottomStart = 10.dp))
                    .clickable {
                        if (personnelCount > DEFAULT_PERSONNEL_COUNT) {
                            onClick(personnelCount - 1)
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_minus),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = if (personnelCount > DEFAULT_PERSONNEL_COUNT) Arsenic else Silver_Sand
                )
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(42.dp)
                    .background(White),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = personnelCount.toString(),
                    color = if (personnelCount >= DEFAULT_PERSONNEL_COUNT) Chinese_Black else Silver_Sand,
                    fontSize = 20.dp.toSp(),
                    fontWeight = FontWeight.Bold,
                    fontFamily = spoqahansanseeo
                )
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(42.dp)
                    .background(Cultured, RoundedCornerShape(topEnd = 10.dp, bottomEnd = 10.dp))
                    .clickable {
                        if (personnelCount < maxCount) {
                            onClick(personnelCount + 1)
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_plus),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = if (personnelCount < maxCount) Arsenic else Silver_Sand
                )
            }
        }
    }
}

@Composable
private fun SelectDateWithButton(
    modifier: Modifier = Modifier,
    date: String,
    isClickedDate: Boolean,
    isClickedButton: Boolean,
    onDateClick: (Boolean) -> Unit,
    onButtonClick: (Boolean) -> Unit,
    onCalenderClick: (String) -> Unit,
) {
    Column(modifier.zIndex(if (isClickedDate) 1f else 0f)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(R.string.partymember_create_select_date),
                modifier = Modifier.weight(1f),
                color = Black_Pearl,
                fontSize = 18.dp.toSp(),
                fontWeight = FontWeight.Bold,
                fontFamily = spoqahansanseeo
            )
            CurtainCallBorderText(
                modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp),
                text = stringResource(R.string.partymember_create_essential),
                borderColor = Me_Pink,
                contentColor = Me_Pink,
                fontSize = 13.dp.toSp(),
                radiusSize = 20.dp
            )
        }
        Row(
            modifier = Modifier.padding(top = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier
                    .weight(1f)
                    .height(42.dp)
                    .padding(end = 10.dp)
                    .clickable {
                        onDateClick(isClickedDate.not())
                    },
                shape = RoundedCornerShape(10.dp),
                color = Cultured
            ) {
                Text(
                    text = date.ifEmpty { stringResource(R.string.partymember_create_select_date) },
                    modifier = Modifier
                        .padding(horizontal = 18.dp)
                        .padding(vertical = 10.dp),
                    color = if (date.isEmpty()) Silver_Sand else Nero,
                    fontSize = 15.dp.toSp(),
                    fontWeight = FontWeight.Medium,
                    fontFamily = spoqahansanseeo
                )
            }
            if (isClickedButton) {
                CurtainCallRoundedText(
                    modifier = Modifier.padding(horizontal = 25.dp, vertical = 11.dp),
                    text = stringResource(R.string.partymember_create_undetermined_date),
                    containerColor = Me_Pink,
                    contentColor = White,
                    fontSize = 15.dp.toSp(),
                    radiusSize = 10.dp
                )
            } else {
                CurtainCallBorderText(
                    modifier = Modifier
                        .padding(horizontal = 25.dp, vertical = 11.dp)
                        .clickable {
                            onButtonClick(true)
                            onDateClick(false)
                        },
                    text = stringResource(R.string.partymember_create_undetermined_date),
                    borderColor = Silver_Sand,
                    contentColor = Roman_Silver,
                    fontSize = 15.dp.toSp(),
                    radiusSize = 10.dp
                )
            }
        }

        if (isClickedDate) {
            SelectedDateCalender(
                modifier = Modifier.padding(top = 10.dp),
                onDateClick = {
                    onCalenderClick(
                        String.format(
                            "%d. %02d. %02d",
                            it.date.year,
                            it.date.month.value,
                            it.date.dayOfMonth
                        )
                    )
                    onDateClick(false)
                }
            )
        }
    }
}
