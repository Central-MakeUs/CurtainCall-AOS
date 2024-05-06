package com.cmc.curtaincall.feature.partymember.recruit.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.cmc.curtaincall.common.designsystem.R
import com.cmc.curtaincall.common.designsystem.component.appbars.CurtainCallCenterTopAppBarWithBack
import com.cmc.curtaincall.common.designsystem.component.appbars.CurtainCallSearchTitleTopAppBarWithBack
import com.cmc.curtaincall.common.designsystem.component.appbars.SearchAppBarState
import com.cmc.curtaincall.common.designsystem.component.basic.CurtainCallSnackbar
import com.cmc.curtaincall.common.designsystem.component.basic.CurtainCallSnackbarHost
import com.cmc.curtaincall.common.designsystem.component.basic.SystemUiStatusBar
import com.cmc.curtaincall.common.designsystem.component.buttons.common.CurtainCallFilledButton
import com.cmc.curtaincall.common.designsystem.theme.CurtainCallTheme
import com.cmc.curtaincall.common.designsystem.theme.Grey7
import com.cmc.curtaincall.feature.partymember.recruit.PartyMemberRecruitSideEffect
import com.cmc.curtaincall.feature.partymember.recruit.PartyMemberRecruitUiState
import com.cmc.curtaincall.feature.partymember.recruit.PartyMemberRecruitViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun PartyMemberRecruitScreen(
    partyMemberRecruitViewModel: PartyMemberRecruitViewModel = hiltViewModel(),
    onBack: () -> Unit = {}
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var snackbarPainter by remember { mutableIntStateOf(R.drawable.ic_complete_green) }
    var isShowFAB by remember { mutableStateOf(true) }
    val partyMemberRecruitUiState by partyMemberRecruitViewModel.uiState.collectAsStateWithLifecycle()
    val searchAppBarState by partyMemberRecruitViewModel.searchAppBarState.collectAsStateWithLifecycle()

    SystemUiStatusBar(CurtainCallTheme.colors.background)
    Scaffold(
        topBar = {
            if (partyMemberRecruitUiState.phrase >= 2) {
                CurtainCallCenterTopAppBarWithBack(
                    title = stringResource(R.string.party_member_recruit),
                    onBack = {
                        when (partyMemberRecruitUiState.phrase) {
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
            } else {
                CurtainCallSearchTitleTopAppBarWithBack(
                    title = stringResource(R.string.party_member_recruit),
                    searchAppBarState = searchAppBarState,
                    onBack = onBack
                )
            }
        },
        snackbarHost = {
            CurtainCallSnackbarHost(snackbarHostState = snackbarHostState) { snackbarData ->
                CurtainCallSnackbar(
                    modifier = Modifier.fillMaxWidth(),
                    snackbarData = snackbarData,
                    painter = painterResource(snackbarPainter)
                )
            }
        },
        floatingActionButton = {
            if (isShowFAB) {
                CurtainCallFilledButton(
                    text = stringResource(
                        if (partyMemberRecruitUiState.phrase == 3) {
                            R.string.finish_write
                        } else {
                            R.string.next
                        }
                    ),
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
                            partyMemberRecruitUiState.selectShowDate.isNotEmpty() && partyMemberRecruitUiState.selectShowTime.isNotEmpty()
                        }

                        else -> {
                            partyMemberRecruitUiState.title.isNotEmpty() && partyMemberRecruitUiState.content.isNotEmpty() && partyMemberRecruitUiState.content.length < 500
                        }
                    },
                    onClick = {
                        if (partyMemberRecruitUiState.phrase < 3) {
                            partyMemberRecruitViewModel.movePhrase(partyMemberRecruitUiState.phrase + 1)
                        } else {
                            partyMemberRecruitViewModel.createParty()
                        }
                    }
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { paddingValues ->
        PartyMemberRecruitContent(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(CurtainCallTheme.colors.background),
            partyMemberRecruitUiState = partyMemberRecruitUiState,
            searchAppBarState = searchAppBarState
        )
    }

    BackHandler {
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

    LaunchedEffect(Unit) {
        partyMemberRecruitViewModel.effects.collectLatest { effect ->
            isShowFAB = false
            when (effect) {
                PartyMemberRecruitSideEffect.CreateParty -> {
                    snackbarPainter = R.drawable.ic_complete_green
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(
                            context.getString(R.string.party_member_upload_success)
                        )
                    }
                    delay(2000)
                    onBack()
                }

                PartyMemberRecruitSideEffect.FailedCreateParty -> {
                    snackbarPainter = R.drawable.ic_error_snackbar
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(
                            context.getString(R.string.party_member_upload_failure)
                        )
                    }
                    delay(2000)
                    onBack()
                }
            }
        }
    }
}

@Composable
private fun PartyMemberRecruitContent(
    modifier: Modifier = Modifier,
    partyMemberRecruitUiState: PartyMemberRecruitUiState,
    searchAppBarState: SearchAppBarState
) {
    val scrollState = rememberScrollState()
    val showInfoModels = partyMemberRecruitUiState.showInfoModels.collectAsLazyPagingItems()
    Column(
        modifier = modifier
            .then(
                if (partyMemberRecruitUiState.phrase == 1) {
                    Modifier
                } else {
                    Modifier.verticalScroll(scrollState)
                }
            )
    ) {
        PartyMemberRecruitPhrase(
            modifier = Modifier
                .padding(top = 20.dp)
                .fillMaxWidth(),
            phrase = partyMemberRecruitUiState.phrase
        )
        when (partyMemberRecruitUiState.phrase) {
            1 -> {
                PartyMemberRecruitFirstScreen(
                    searchAppBarState = searchAppBarState,
                    sortType = partyMemberRecruitUiState.sortType,
                    genreType = partyMemberRecruitUiState.genreType,
                    isShowTooltip = partyMemberRecruitUiState.isShowTooltip
                )
            }

            2 -> {
                PartyMemberRecruitSecondScreen(
                    showStartDate = partyMemberRecruitUiState.showStartDate,
                    showEndDate = partyMemberRecruitUiState.showEndDate,
                    showTimes = partyMemberRecruitUiState.showTimes,
                    selectShowDate = partyMemberRecruitUiState.selectShowDate,
                    selectShowTime = partyMemberRecruitUiState.selectShowTime,
                    selectMemberNumber = partyMemberRecruitUiState.selectMemberNumber
                )
            }

            else -> {
                PartyMemberRecruitThirdScreen(
                    title = partyMemberRecruitUiState.title,
                    content = partyMemberRecruitUiState.content
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
