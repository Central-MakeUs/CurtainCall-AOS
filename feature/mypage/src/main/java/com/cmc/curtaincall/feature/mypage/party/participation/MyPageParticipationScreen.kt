package com.cmc.curtaincall.feature.mypage.party.participation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.cmc.curtaincall.common.designsystem.R
import com.cmc.curtaincall.common.designsystem.component.basic.TopAppBarWithBack
import com.cmc.curtaincall.common.designsystem.component.card.PartyMemberEtcItemCard
import com.cmc.curtaincall.common.designsystem.component.card.PartyMemberItemCard
import com.cmc.curtaincall.common.designsystem.component.card.PartyType
import com.cmc.curtaincall.common.designsystem.component.item.EmptyMyParty
import com.cmc.curtaincall.common.designsystem.theme.Cultured
import com.cmc.curtaincall.common.designsystem.theme.Nero
import com.cmc.curtaincall.common.utility.extensions.toChangeFullDate
import com.cmc.curtaincall.common.utility.extensions.toDateWithDay
import com.cmc.curtaincall.common.utility.extensions.toTime
import com.cmc.curtaincall.feature.mypage.MyPageViewModel
import com.cmc.curtaincall.feature.mypage.party.MyPagePartyMenuTab
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPageParticipationScreen(
    myPageViewModel: MyPageViewModel = hiltViewModel(),
    onNavigateParticipationDetail: (PartyType, Int) -> Unit,
    onNavigatePartyMember: (PartyType) -> Unit,
    onBack: () -> Unit
) {
    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(Cultured)

    Scaffold(
        topBar = {
            TopAppBarWithBack(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                title = stringResource(R.string.mypage_my_participation_tab),
                containerColor = Cultured,
                contentColor = Nero,
                onClick = onBack
            )
        }
    ) { paddingValues ->
        MyPageParticipationContent(
            myPageViewModel = myPageViewModel,
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(Cultured),
            onNavigateParticipationDetail = onNavigateParticipationDetail,
            onNavigatePartyMember = onNavigatePartyMember
        )
    }
}

@Composable
private fun MyPageParticipationContent(
    myPageViewModel: MyPageViewModel,
    modifier: Modifier = Modifier,
    onNavigateParticipationDetail: (PartyType, Int) -> Unit,
    onNavigatePartyMember: (PartyType) -> Unit,
) {
    val myParticipationPartyType by myPageViewModel.myParticipationPartyType.collectAsStateWithLifecycle()
    val participationItems = when (myParticipationPartyType) {
        PartyType.PERFORMANCE -> {
            myPageViewModel.watchingParticipationItems.collectAsLazyPagingItems()
        }

        PartyType.MEAL -> {
            myPageViewModel.foodParticipationItems.collectAsLazyPagingItems()
        }

        PartyType.ETC -> {
            myPageViewModel.etcParticipationItems.collectAsLazyPagingItems()
        }
    }
    Column(modifier) {
        MyPagePartyMenuTab(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, top = 30.dp),
            partyType = myParticipationPartyType,
            onChangePartType = { myPageViewModel.setParticipationPartyType(it) }
        )

        if (participationItems.itemCount == 0) {
            Column {
                Spacer(Modifier.weight(150f))
                EmptyMyParty(
                    modifier = Modifier.fillMaxWidth(),
                    title = stringResource(R.string.mypage_setting_empty_partmember_title),
                    subTitle = stringResource(R.string.mypage_setting_empty_partymember_subtitle),
                    onClick = { onNavigatePartyMember(myParticipationPartyType) }
                )
                Spacer(Modifier.weight(239f))
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(top = 26.dp)
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
            ) {
                items(participationItems) { participationModel ->
                    participationModel?.let { participationModel ->
                        if (myParticipationPartyType == PartyType.ETC) {
                            PartyMemberEtcItemCard(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight()
                                    .padding(bottom = 20.dp),
                                profileImageUrl = participationModel.creatorImageUrl,
                                nickname = participationModel.creatorNickname,
                                createAtDate = participationModel.createdAt.toChangeFullDate(),
                                createAtTime = participationModel.createdAt.toTime(),
                                description = participationModel.title,
                                date = participationModel.showAt?.toDateWithDay(),
                                numberOfMember = participationModel.curMemberNum,
                                numberOfTotal = participationModel.maxMemberNum,
                                onClick = {
                                    onNavigateParticipationDetail(
                                        myParticipationPartyType,
                                        participationModel.id
                                    )
                                }
                            )
                        } else {
                            PartyMemberItemCard(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight()
                                    .padding(bottom = 20.dp),
                                title = participationModel.showName,
                                profileImageUrl = participationModel.creatorImageUrl,
                                nickname = participationModel.creatorNickname,
                                createAtDate = participationModel.createdAt.toChangeFullDate(),
                                createAtTime = participationModel.createdAt.toTime(),
                                numberOfMember = participationModel.curMemberNum,
                                numberOfTotal = participationModel.maxMemberNum,
                                description = participationModel.title,
                                posterUrl = participationModel.showPoster,
                                date = participationModel.showAt.toDateWithDay(),
                                time = participationModel.showAt.toTime(),
                                location = participationModel.facilityName,
                                onClick = {
                                    onNavigateParticipationDetail(
                                        myParticipationPartyType,
                                        participationModel.id
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
