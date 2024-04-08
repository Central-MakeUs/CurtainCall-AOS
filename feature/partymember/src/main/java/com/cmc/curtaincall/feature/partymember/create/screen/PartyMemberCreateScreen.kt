package com.cmc.curtaincall.feature.partymember.create.screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.cmc.curtaincall.common.designsystem.R
import com.cmc.curtaincall.common.designsystem.component.basic.CurtainCallRoundedText
import com.cmc.curtaincall.common.designsystem.component.basic.TopAppBarWithBack
import com.cmc.curtaincall.common.designsystem.component.card.PartyType
import com.cmc.curtaincall.common.designsystem.extensions.toSp
import com.cmc.curtaincall.common.designsystem.theme.*
import com.cmc.curtaincall.feature.partymember.create.PartyMemberCreateSideEffect
import com.cmc.curtaincall.feature.partymember.create.PartyMemberCreateViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.flow.collectLatest

private const val UNSELECTED_INDEX = -1
const val DEFAULT_PERSONNEL_COUNT = 2

enum class STEP(val prevStep: STEP) {
    PHASE1(PHASE1), PHASE2(PHASE1), PHASE3(PHASE2), PHASE1_1(PHASE1_1), PHASE1_2(PHASE1_1)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PartyMemberCreateScreen(
    partyMemberCreateViewModel: PartyMemberCreateViewModel = hiltViewModel(),
    partyType: PartyType,
    onBack: () -> Unit
) {
//    val context = LocalContext.current
//    val coroutineScope = rememberCoroutineScope()
//    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        partyMemberCreateViewModel.setPartyCategory(partyType)
        partyMemberCreateViewModel.loadPlayItems()
        partyMemberCreateViewModel.loadMusicalItems()

        partyMemberCreateViewModel.effects.collectLatest { effect ->
            when (effect) {
                PartyMemberCreateSideEffect.SuccessUpload -> {
                    onBack()
                }
            }
        }
    }

    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(White)
    var currentStep by remember { mutableStateOf(if (partyType == PartyType.ETC) STEP.PHASE1_1 else STEP.PHASE1) }
    Scaffold(
//        snackbarHost = {
//            CurtainCallSnackbarHost(snackbarHostState = snackbarHostState) { snackbarData ->
//                CurtainCallSnackbar(
//                    modifier = Modifier.fillMaxWidth(),
//                    snackbarData = snackbarData
//                )
//            }
//        },
        topBar = {
            TopAppBarWithBack(
                title = stringResource(R.string.partymember_create_appbar),
                containerColor = White,
                contentColor = Eerie_Black,
                onClick = {
                    if ((currentStep == STEP.PHASE1) or (currentStep == STEP.PHASE1_1)) {
                        onBack()
                    } else {
                        currentStep = currentStep.prevStep
                    }
                }
            )
        }
    ) { paddingValues ->
        PartyMemberCreateContent(
            partyMemberCreateViewModel = partyMemberCreateViewModel,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(White),
            partyType = partyType,
            currentStep = currentStep,
            onChangeStep = { currentStep = it }
        )
    }
}

@Composable
private fun PartyMemberCreateContent(
    partyMemberCreateViewModel: PartyMemberCreateViewModel,
    modifier: Modifier = Modifier,
    partyType: PartyType,
    currentStep: STEP,
    onChangeStep: (STEP) -> Unit
) {
    // 1 step
    var selectedPerformanceIndex by remember { mutableIntStateOf(UNSELECTED_INDEX) }
    var isCheckFirstType by remember { mutableStateOf(true) }

    // 2 step
    var selectedDateState by remember { mutableStateOf("") }
    var selectedTimeState by remember { mutableStateOf("") }
    var personnelCountState by remember { mutableIntStateOf(DEFAULT_PERSONNEL_COUNT) }

    // 3 step
    var titleTextState by remember { mutableStateOf("") }
    var contentTextState by remember { mutableStateOf("") }

    // 1-1 step
    var clickedUndeterminDateState by remember { mutableStateOf(false) }

    val partyMemberCreateUiState by partyMemberCreateViewModel.uiState.collectAsStateWithLifecycle()
    val playItems = partyMemberCreateUiState.playItems.collectAsLazyPagingItems()
    val musicalItems = partyMemberCreateUiState.musicalItems.collectAsLazyPagingItems()

    LaunchedEffect(currentStep) {
        if (currentStep == STEP.PHASE1) {
            selectedDateState = ""
            selectedTimeState = ""
            personnelCountState = DEFAULT_PERSONNEL_COUNT
        } else if (currentStep == STEP.PHASE2) {
            titleTextState = ""
            contentTextState = ""
        } else {
            // TODO
        }
    }

    Box(modifier) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            createStep(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 26.dp),
                currentStep = currentStep
            )

            createClip(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                partyType = partyType
            )

            when (currentStep) {
                STEP.PHASE1 -> showPerformanceFirstStep(
                    partyMemberCreateViewModel = partyMemberCreateViewModel,
                    isCheckFirstType = isCheckFirstType,
                    onTypeChange = { isCheckFirstType = it },
                    playItems = playItems,
                    musicalItems = musicalItems,
                    selectedIndex = selectedPerformanceIndex,
                    onChangeSelect = { selectedPerformanceIndex = it }
                )

                STEP.PHASE2 -> showPerformanceSecondStep(
                    partyMemberCreateViewModel = partyMemberCreateViewModel,
                    modifier = Modifier.fillMaxWidth(),
                    selectedDate = selectedDateState,
                    selectedTime = selectedTimeState,
                    personnelCount = personnelCountState,
                    onSelectDate = { selectedDateState = it },
                    onSelectTime = { selectedTimeState = it },
                    onChangePersonnelCount = { personnelCountState = it }
                )

                STEP.PHASE3, STEP.PHASE1_2 -> showLastStep(
                    modifier = Modifier.fillMaxWidth(),
                    title = titleTextState,
                    content = contentTextState,
                    onChangeTitle = { titleTextState = it },
                    onChangeContent = { contentTextState = it }
                )

                STEP.PHASE1_1 -> showEtcFirstStep(
                    modifier = Modifier.fillMaxWidth(),
                    selectedDate = selectedDateState,
                    personnelCount = personnelCountState,
                    clickedUndeterminedDate = clickedUndeterminDateState,
                    onSelectDate = { selectedDateState = it },
                    onClickUndeterminedDate = { clickedUndeterminDateState = it },
                    onClickPersonnel = { personnelCountState = it }
                )
            }
        }

        when (currentStep) {
            STEP.PHASE1 -> {
                Button(
                    onClick = {
                        if (isCheckFirstType) {
                            playItems[selectedPerformanceIndex]?.let { playItem ->
                                partyMemberCreateViewModel.setShowId(playItem.id)
                            }
                        } else {
                            musicalItems[selectedPerformanceIndex]?.let { musicalItem ->
                                partyMemberCreateViewModel.setShowId(musicalItem.id)
                            }
                        }
                        onChangeStep(STEP.PHASE2)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(horizontal = 20.dp)
                        .padding(bottom = 19.dp)
                        .height(52.dp),
                    enabled = selectedPerformanceIndex != UNSELECTED_INDEX,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedPerformanceIndex != UNSELECTED_INDEX) Me_Pink else Bright_Gray,
                        disabledContainerColor = Bright_Gray
                    )
                ) {
                    Text(
                        text = stringResource(R.string.partymember_create_next),
                        color = if (selectedPerformanceIndex != UNSELECTED_INDEX) White else Silver_Sand,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = spoqahansanseeo
                    )
                }
            }

            STEP.PHASE2 -> {
                Button(
                    onClick = {
                        partyMemberCreateViewModel.setPartyInfo(selectedDateState, selectedTimeState, personnelCountState)
                        onChangeStep(STEP.PHASE3)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(horizontal = 20.dp)
                        .padding(bottom = 19.dp)
                        .height(52.dp),
                    enabled = selectedDateState.isNotEmpty() and selectedTimeState.isNotEmpty() and (personnelCountState >= DEFAULT_PERSONNEL_COUNT),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedDateState.isNotEmpty() and selectedTimeState.isNotEmpty() and (personnelCountState >= DEFAULT_PERSONNEL_COUNT)) {
                            Me_Pink
                        } else {
                            Bright_Gray
                        },
                        disabledContainerColor = Bright_Gray
                    )
                ) {
                    Text(
                        text = stringResource(R.string.partymember_create_next),
                        color = if (selectedDateState.isNotEmpty() and selectedTimeState.isNotEmpty() and (personnelCountState > 1)) White else Silver_Sand,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = spoqahansanseeo
                    )
                }
            }

            STEP.PHASE3, STEP.PHASE1_2 -> {
                Button(
                    onClick = {
                        partyMemberCreateViewModel.setPartyDescription(titleTextState, contentTextState)
                        partyMemberCreateViewModel.createParty()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(horizontal = 20.dp)
                        .padding(bottom = 19.dp)
                        .height(52.dp),
                    enabled = titleTextState.isNotEmpty() && contentTextState.isNotEmpty(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (titleTextState.isNotEmpty() && contentTextState.isNotEmpty()) Me_Pink else Bright_Gray,
                        disabledContainerColor = Bright_Gray
                    )
                ) {
                    Text(
                        text = stringResource(R.string.partymember_create_write_complete),
                        color = if (titleTextState.isNotEmpty() && contentTextState.isNotEmpty()) White else Silver_Sand,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = spoqahansanseeo
                    )
                }
            }

            STEP.PHASE1_1 -> {
                var validation = (selectedDateState.isNotEmpty() or clickedUndeterminDateState) and (personnelCountState >= DEFAULT_PERSONNEL_COUNT)
                Button(
                    onClick = {
                        if (clickedUndeterminDateState) {
                            partyMemberCreateViewModel.setPartyInfo(
                                date = null,
                                time = "00:00",
                                maxMemberNum = personnelCountState
                            )
                        } else {
                            partyMemberCreateViewModel.setPartyInfo(
                                date = selectedDateState,
                                time = "00:00",
                                maxMemberNum = personnelCountState
                            )
                        }
                        onChangeStep(STEP.PHASE1_2)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(horizontal = 20.dp)
                        .padding(bottom = 19.dp)
                        .height(52.dp),
                    enabled = validation,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (validation) Me_Pink else Bright_Gray,
                        disabledContainerColor = Bright_Gray
                    )
                ) {
                    Text(
                        text = stringResource(R.string.partymember_create_next),
                        color = if (validation) White else Silver_Sand,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = spoqahansanseeo
                    )
                }
            }
        }
    }
}

private fun LazyGridScope.createClip(
    modifier: Modifier = Modifier,
    partyType: PartyType
) {
    item(span = { GridItemSpan(3) }) {
        Row(modifier) {
            CurtainCallRoundedText(
                modifier = Modifier.padding(vertical = 4.dp, horizontal = 12.dp),
                text = stringResource(
                    when (partyType) {
                        PartyType.PERFORMANCE -> R.string.partymember_performance_title
                        PartyType.MEAL -> R.string.partymember_restaurant_title
                        PartyType.ETC -> R.string.partymember_etc_title
                    }
                ),
                containerColor = Cetacean_Blue,
                contentColor = White,
                fontSize = 12.dp.toSp(),
                radiusSize = 12.dp
            )
        }
    }
}

private fun LazyGridScope.createStep(
    modifier: Modifier = Modifier,
    currentStep: STEP,
) {
    item(span = { GridItemSpan(3) }) {
        Column(modifier) {
            Row {
                Column(Modifier.weight(1f)) {
                    Text(
                        text = stringResource(R.string.partymember_create_first_step),
                        color = Cetacean_Blue,
                        fontSize = 12.dp.toSp(),
                        fontWeight = FontWeight.Bold,
                        fontFamily = spoqahansanseeo
                    )
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp)
                            .height(8.dp)
                            .background(Cetacean_Blue, RoundedCornerShape(topStart = 30.dp, bottomStart = 30.dp))
                    )
                }
                Column(
                    modifier = Modifier
                        .padding(start = 3.dp)
                        .weight(1f)
                ) {
                    Text(
                        text = stringResource(R.string.partymember_create_second_step),
                        color = if (currentStep !in listOf(STEP.PHASE1, STEP.PHASE1_1)) Cetacean_Blue else Bright_Gray,
                        fontSize = 12.dp.toSp(),
                        fontWeight = FontWeight.Bold,
                        fontFamily = spoqahansanseeo
                    )

                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp)
                            .height(8.dp)
                            .background(
                                if (currentStep !in listOf(STEP.PHASE1, STEP.PHASE1_1)) Cetacean_Blue else Bright_Gray,
                                if (currentStep in listOf(STEP.PHASE1_1, STEP.PHASE1_2)) {
                                    RoundedCornerShape(
                                        topEnd = 30.dp,
                                        bottomEnd = 30.dp
                                    )
                                } else {
                                    RectangleShape
                                }
                            )
                    )
                }
                if (currentStep !in listOf(STEP.PHASE1_1, STEP.PHASE1_2)) {
                    Column(
                        modifier = Modifier
                            .padding(start = 3.dp)
                            .weight(1f)
                    ) {
                        Text(
                            text = stringResource(R.string.partymember_create_third_step),
                            color = if (currentStep == STEP.PHASE3) Cetacean_Blue else Bright_Gray,
                            fontSize = 12.dp.toSp(),
                            fontWeight = FontWeight.Bold,
                            fontFamily = spoqahansanseeo
                        )
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp)
                                .height(8.dp)
                                .background(
                                    if (currentStep == STEP.PHASE3) Cetacean_Blue else Bright_Gray,
                                    RoundedCornerShape(topEnd = 30.dp, bottomEnd = 30.dp)
                                )
                        )
                    }
                }
            }
        }
    }
}
