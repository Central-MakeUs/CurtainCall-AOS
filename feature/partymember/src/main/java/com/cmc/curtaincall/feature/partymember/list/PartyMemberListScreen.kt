package com.cmc.curtaincall.feature.partymember.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import androidx.paging.compose.itemsIndexed
import com.cmc.curtaincall.common.designsystem.R
import com.cmc.curtaincall.common.designsystem.component.basic.SearchAppBar
import com.cmc.curtaincall.common.designsystem.component.basic.SearchTopAppBarWithBack
import com.cmc.curtaincall.common.designsystem.component.basic.TopAppBarWithBack
import com.cmc.curtaincall.common.designsystem.component.card.PartyMemberEtcItemCard
import com.cmc.curtaincall.common.designsystem.component.card.PartyMemberItemCard
import com.cmc.curtaincall.common.designsystem.component.card.PartyType
import com.cmc.curtaincall.common.designsystem.component.content.empty.EmptyContent
import com.cmc.curtaincall.common.designsystem.component.item.search.SearchTextItem
import com.cmc.curtaincall.common.designsystem.extensions.toSp
import com.cmc.curtaincall.common.designsystem.theme.*
import com.cmc.curtaincall.common.utility.extensions.toChangeFullDate
import com.cmc.curtaincall.common.utility.extensions.toDateWithDay
import com.cmc.curtaincall.common.utility.extensions.toTime
import com.cmc.curtaincall.feature.partymember.PartyMemberUiState
import com.cmc.curtaincall.feature.partymember.PartyMemberViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PartyMemberListScreen(
    partyMemberViewModel: PartyMemberViewModel = hiltViewModel(),
    partyType: PartyType,
    onNavigateDetail: (PartyType, Int, Boolean) -> Unit,
    onNavigateCreate: (PartyType) -> Unit,
    onBack: () -> Unit
) {
    val partyMemberUiState by partyMemberViewModel.uiState.collectAsStateWithLifecycle()

    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(
        if (partyMemberUiState.isActiveSearch) White else Cultured
    )

    LaunchedEffect(partyMemberViewModel) {
        partyMemberViewModel.changePartType(partyType)
    }

    Scaffold(
        topBar = {
            if (partyType == PartyType.ETC) {
                TopAppBarWithBack(
                    title = stringResource(
                        when (partyType) {
                            PartyType.PERFORMANCE -> R.string.partymember_performance_title
                            PartyType.MEAL -> R.string.partymember_restaurant_title
                            PartyType.ETC -> R.string.partymember_etc_title
                        }
                    ),
                    containerColor = Cultured,
                    contentColor = Nero,
                    onClick = onBack
                )
            } else {
                if (partyMemberUiState.isActiveSearch) {
                    SearchAppBar(
                        value = partyMemberUiState.queryString,
                        onValueChange = {
                            partyMemberViewModel.setQueryString(it)
                            partyMemberViewModel.changeDoneSearch(false)
                        },
                        containerColor = White,
                        contentColor = Nero,
                        placeholder = stringResource(R.string.search_performance_title),
                        onDone = {
                            if (partyMemberUiState.queryString.isNotEmpty()) {
                                partyMemberViewModel.searchPartyList(partyMemberUiState.queryString)
                                partyMemberViewModel.insertPartySearchWord()
                            }
                            partyMemberViewModel.changeDoneSearch(true)
                        },
                        onClick = {
                            partyMemberViewModel.changeActiveSearch(false)
                            partyMemberViewModel.setQueryString("")
                        },
                        onAction = { partyMemberViewModel.setQueryString("") }
                    )
                } else {
                    SearchTopAppBarWithBack(
                        title = stringResource(
                            when (partyType) {
                                PartyType.PERFORMANCE -> R.string.partymember_performance_title
                                PartyType.MEAL -> R.string.partymember_restaurant_title
                                PartyType.ETC -> R.string.partymember_etc_title
                            }
                        ),
                        containerColor = Cultured,
                        contentColor = Nero,
                        tint = Roman_Silver,
                        onBack = onBack,
                        onClick = { partyMemberViewModel.changeActiveSearch(true) }
                    )
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onNavigateCreate(partyType) },
                modifier = Modifier
                    .padding(bottom = 40.dp)
                    .size(58.dp),
                shape = CircleShape,
                containerColor = Cetacean_Blue
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_pen),
                    contentDescription = null,
                    modifier = Modifier.size(29.dp),
                    tint = Color.Unspecified
                )
            }
        }
    ) { paddingValues ->
        if (partyMemberUiState.isActiveSearch) {
            PartyMemberListSearchContent(
                partyMemberViewModel = partyMemberViewModel,
                partyType = partyType,
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .background(White),
                onSearchClick = {
                    partyMemberViewModel.setQueryString(it)
                    partyMemberViewModel.searchPartyList(it)
                    partyMemberViewModel.insertPartySearchWord()
                    partyMemberViewModel.changeDoneSearch(true)
                },
                onNavigateDetail = onNavigateDetail
            )
        } else {
            PartyMemberListContent(
                partyMemberUiState = partyMemberUiState,
                partyMemberViewModel = partyMemberViewModel,
                partyType = partyType,
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .background(Cultured),
                onNavigateDetail = onNavigateDetail
            )
        }
    }
}

@Composable
private fun PartyMemberListSearchContent(
    partyMemberViewModel: PartyMemberViewModel,
    partyType: PartyType,
    modifier: Modifier = Modifier,
    onSearchClick: (String) -> Unit,
    onNavigateDetail: (PartyType, Int, Boolean) -> Unit,
) {
    val searchWords by partyMemberViewModel.searchWords.collectAsStateWithLifecycle()
    val partyMemberUiState by partyMemberViewModel.uiState.collectAsStateWithLifecycle()
    val partySearchItems = partyMemberUiState.partySearchItems.collectAsLazyPagingItems()

    LazyColumn(modifier) {
        item {
            Spacer(
                modifier = Modifier
                    .padding(vertical = 20.dp)
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Cultured)
            )
        }

        if (partyMemberUiState.queryString.isEmpty()) {
            if (searchWords.isEmpty()) {
                item {
                    EmptyContent(
                        modifier = Modifier.fillMaxSize(),
                        text = stringResource(R.string.search_empty_recently_word)
                    )
                }
            } else {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .padding(bottom = 20.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(R.string.search_recently_word),
                                modifier = Modifier.weight(1f),
                                color = Chinese_Black,
                                fontSize = 16.dp.toSp(),
                                fontWeight = FontWeight.Bold,
                                fontFamily = spoqahansanseeo
                            )
                            Text(
                                text = stringResource(R.string.search_delete_recentyl_word),
                                modifier = Modifier.clickable { partyMemberViewModel.deletePartySearchWordList() },
                                color = Roman_Silver,
                                fontSize = 14.dp.toSp(),
                                fontWeight = FontWeight.Medium,
                                fontFamily = spoqahansanseeo
                            )
                        }
                        searchWords.forEach { searchWord ->
                            SearchTextItem(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 10.dp),
                                text = searchWord.word,
                                onDelete = { partyMemberViewModel.deletePartySearchWord(searchWord) },
                                onClick = { onSearchClick(searchWord.word) }
                            )
                        }
                    }
                }
            }
        } else {
            if (partyMemberUiState.isDoneSearch) {
                itemsIndexed(partySearchItems) { index, partyModel ->
                    partyModel?.let { partyModel ->
                        PartyMemberItemCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp)
                                .wrapContentHeight()
                                .padding(bottom = 20.dp),
                            title = partyModel.showName,
                            nickname = partyModel.creatorNickname,
                            createAtDate = partyModel.createdAt.toChangeFullDate(),
                            createAtTime = partyModel.createdAt.toTime(),
                            numberOfMember = partyModel.curMemberNum,
                            numberOfTotal = partyModel.maxMemberNum,
                            description = partyModel.title,
                            posterUrl = partyModel.showPoster,
                            date = partyModel.showAt?.toDateWithDay(),
                            time = partyModel.showAt?.toTime(),
                            location = partyModel.facilityName,
                            onClick = {
                                onNavigateDetail(
                                    partyType,
                                    partyModel.id,
                                    partyModel.creatorId == partyMemberViewModel.memberId.value
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PartyMemberListContent(
    partyMemberUiState: PartyMemberUiState,
    partyMemberViewModel: PartyMemberViewModel,
    partyType: PartyType,
    modifier: Modifier = Modifier,
    onNavigateDetail: (PartyType, Int, Boolean) -> Unit
) {
    val pagingItems = when (partyType) {
        PartyType.PERFORMANCE -> {
            partyMemberUiState.watchingItems.collectAsLazyPagingItems()
        }

        PartyType.MEAL -> {
            partyMemberUiState.foodCafeItems.collectAsLazyPagingItems()
        }

        PartyType.ETC -> {
            partyMemberUiState.etcItems.collectAsLazyPagingItems()
        }
    }

    Column(modifier = modifier.background(Cultured)) {
        if (pagingItems.itemCount == 0) {
            EmptyContent(
                modifier = Modifier.fillMaxSize(),
                text = stringResource(R.string.partymember_empty_text)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(top = 30.dp)
                    .padding(horizontal = 20.dp)
            ) {
                item {
                    Text(
                        text = stringResource(R.string.partymember_list_description),
                        modifier = Modifier.padding(bottom = 12.dp),
                        color = Black_Coral,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = spoqahansanseeo
                    )
                }
                items(pagingItems) { partyModel ->
                    partyModel?.let { partyModel ->
                        if (partyType == PartyType.ETC) {
                            PartyMemberEtcItemCard(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight()
                                    .padding(bottom = 16.dp),
                                profileImageUrl = partyModel.creatorImageUrl,
                                nickname = partyModel.creatorNickname,
                                createAtDate = partyModel.createdAt.toChangeFullDate(),
                                createAtTime = partyModel.createdAt.toTime(),
                                description = partyModel.title,
                                date = partyModel.showAt?.toDateWithDay(),
                                numberOfMember = partyModel.curMemberNum,
                                numberOfTotal = partyModel.maxMemberNum,
                                onClick = {
                                    onNavigateDetail(
                                        partyType,
                                        partyModel.id,
                                        partyModel.creatorId == partyMemberViewModel.memberId.value
                                    )
                                }
                            )
                        } else {
                            PartyMemberItemCard(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight()
                                    .padding(bottom = 20.dp),
                                title = partyModel.showName,
                                profileImageUrl = partyModel.creatorImageUrl,
                                nickname = partyModel.creatorNickname,
                                createAtDate = partyModel.createdAt.toChangeFullDate(),
                                createAtTime = partyModel.createdAt.toTime(),
                                numberOfMember = partyModel.curMemberNum,
                                numberOfTotal = partyModel.maxMemberNum,
                                description = partyModel.title,
                                posterUrl = partyModel.showPoster,
                                date = partyModel.showAt?.toDateWithDay(),
                                time = partyModel.showAt?.toTime(),
                                location = partyModel.facilityName,
                                onClick = {
                                    onNavigateDetail(
                                        partyType,
                                        partyModel.id,
                                        partyModel.creatorId == partyMemberViewModel.memberId.value
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
