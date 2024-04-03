package com.cmc.curtaincall.feature.partymember.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.cmc.curtaincall.common.designsystem.R
import com.cmc.curtaincall.common.designsystem.component.appbars.CurtainCallSearchTitleTopAppBarWithCalendar
import com.cmc.curtaincall.common.designsystem.component.basic.SystemUiStatusBar
import com.cmc.curtaincall.common.designsystem.component.buttons.common.CurtainCallFilledButton
import com.cmc.curtaincall.common.designsystem.component.tooltip.CurtainCallPartyTooltip
import com.cmc.curtaincall.common.designsystem.custom.common.CurtainCallCalendar
import com.cmc.curtaincall.common.designsystem.custom.party.PartyContent
import com.cmc.curtaincall.common.designsystem.custom.party.PartyEmptyContent
import com.cmc.curtaincall.common.designsystem.theme.CurtainCallTheme
import com.cmc.curtaincall.common.designsystem.theme.Grey8
import com.kizitonwose.calendar.core.CalendarDay
import timber.log.Timber
import java.time.format.DateTimeFormatter

@Composable
fun PartyMemberScreen(
    onNavigateToRecruit: () -> Unit = {}
) {
    var selectedCalendarDays = remember { mutableStateOf<List<CalendarDay>>(listOf()) }
    var isShowCalendar by remember { mutableStateOf(false) }

    SystemUiStatusBar(Grey8)
    Scaffold(
        topBar = {
            CurtainCallSearchTitleTopAppBarWithCalendar(
                title = stringResource(R.string.party_member),
                containerColor = Grey8,
                selectedCalendarDays = selectedCalendarDays.value,
                onCalendarClick = { isShowCalendar = true }
            )
        },
        floatingActionButton = {
            CurtainCallFilledButton(
                modifier = Modifier
                    .padding(vertical = 14.dp)
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
        PartyMemberContent(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(Grey8),
            isShowCalendar = isShowCalendar,
            onSelectCalendarDays = {
                selectedCalendarDays.value = it
                isShowCalendar = false
            }
        )
    }
}

@Composable
private fun PartyMemberContent(
    modifier: Modifier = Modifier,
    partyMemberViewModel: PartyMemberViewModel = hiltViewModel(),
    isShowCalendar: Boolean = false,
    onSelectCalendarDays: (List<CalendarDay>) -> Unit = {}
) {
    val partyModels = partyMemberViewModel.partyModel.collectAsLazyPagingItems()
    val isShowTooltip by partyMemberViewModel.isShowTooltip.collectAsStateWithLifecycle()

    Box(modifier) {
        if (isShowTooltip) {
            CurtainCallPartyTooltip(
                modifier = Modifier.padding(start = 20.dp),
                text = stringResource(R.string.party_member_tooltip),
                onClick = { partyMemberViewModel.stopPartyTooltip() }
            )
        }

        if (isShowCalendar) {
            CurtainCallCalendar(
                modifier = Modifier
                    .zIndex(1f)
                    .align(Alignment.TopCenter)
                    .width(320.dp),
                onSelectDays = {
                    onSelectCalendarDays(it)
                    partyMemberViewModel.fetchPartyList(
                        startDate = it.first().date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                        endDate = it.getOrNull(1)?.date?.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                    )
                }
            )
        }

        if (partyModels.itemCount == 0) {
            Timber.d("PartyMemberContent itemCount zero")
            Column(modifier = Modifier.fillMaxSize()) {
                Spacer(Modifier.weight(200f))
                PartyEmptyContent(Modifier.align(Alignment.CenterHorizontally))
                Spacer(Modifier.weight(308f))
            }
        } else {
            Timber.d("PartyMemberContent itemCount not zero")
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(partyModels.itemCount) { index ->
                    partyModels[index]?.let { partyModel ->
                        PartyContent(partyModel = partyModel)
                    }
                }
            }
        }
    }
}
