package com.cmc.curtaincall.feature.mypage.party.recruitment

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
import androidx.compose.runtime.LaunchedEffect
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
internal fun MyPageRecruitmentScreen(
    myPageViewModel: MyPageViewModel = hiltViewModel(),
    onNavigateRecruitmentDetail: (PartyType, Int) -> Unit,
    onNavigatePartyMember: (PartyType) -> Unit,
    onBack: () -> Unit
) {
    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(Cultured)

    LaunchedEffect(Unit) {
        myPageViewModel.getMemberId()
    }

    Scaffold(
        topBar = {
            TopAppBarWithBack(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                title = stringResource(R.string.mypage_my_gathering_tab),
                containerColor = Cultured,
                contentColor = Nero,
                onClick = onBack
            )
        }
    ) { paddingValues ->
        MyPageRecruitmentContent(
            myPageViewModel = myPageViewModel,
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(Cultured),
            onNavigateRecruitmentDetail = onNavigateRecruitmentDetail,
            onNavigatePartyMember = onNavigatePartyMember
        )
    }
}

@Composable
private fun MyPageRecruitmentContent(
    myPageViewModel: MyPageViewModel,
    modifier: Modifier = Modifier,
    onNavigateRecruitmentDetail: (PartyType, Int) -> Unit,
    onNavigatePartyMember: (PartyType) -> Unit
) {
    val myRecruitmentPartTye by myPageViewModel.myRecruitmentPartType.collectAsStateWithLifecycle()
    val recruitmentItems = when (myRecruitmentPartTye) {
        PartyType.PERFORMANCE -> {
            myPageViewModel.watchingRecruitmentItems.collectAsLazyPagingItems()
        }

        PartyType.MEAL -> {
            myPageViewModel.foodRecruitmentItems.collectAsLazyPagingItems()
        }

        PartyType.ETC -> {
            myPageViewModel.etcRecruitmentItems.collectAsLazyPagingItems()
        }
    }
    Column(modifier) {
        MyPagePartyMenuTab(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, top = 30.dp),
            partyType = myRecruitmentPartTye,
            onChangePartType = { myPageViewModel.setRecruitmentPartyType(it) }
        )

        if (recruitmentItems.itemCount == 0) {
            Column {
                Spacer(Modifier.weight(150f))
                EmptyMyParty(
                    modifier = Modifier.fillMaxWidth(),
                    title = stringResource(R.string.mypage_setting_empty_partmember_title),
                    subTitle = stringResource(R.string.mypage_setting_empty_partymember_subtitle),
                    onClick = { onNavigatePartyMember(myRecruitmentPartTye) }
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
                items(recruitmentItems) { recruitmentModel ->
                    recruitmentModel?.let { recruitmentModel ->
                        if (myRecruitmentPartTye == PartyType.ETC) {
                            PartyMemberEtcItemCard(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight()
                                    .padding(bottom = 20.dp),
                                profileImageUrl = recruitmentModel.creatorImageUrl,
                                nickname = recruitmentModel.creatorNickname,
                                createAtDate = recruitmentModel.createdAt.toChangeFullDate(),
                                createAtTime = recruitmentModel.createdAt.toTime(),
                                description = recruitmentModel.title,
                                date = recruitmentModel.showAt?.toDateWithDay(),
                                numberOfMember = recruitmentModel.curMemberNum,
                                numberOfTotal = recruitmentModel.maxMemberNum,
                                onClick = {
                                    onNavigateRecruitmentDetail(
                                        myRecruitmentPartTye,
                                        recruitmentModel.id
                                    )
                                }
                            )
                        } else {
                            PartyMemberItemCard(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight()
                                    .padding(bottom = 20.dp),
                                title = recruitmentModel.showName,
                                profileImageUrl = recruitmentModel.creatorImageUrl,
                                nickname = recruitmentModel.creatorNickname,
                                createAtDate = recruitmentModel.createdAt.toChangeFullDate(),
                                createAtTime = recruitmentModel.createdAt.toTime(),
                                numberOfMember = recruitmentModel.curMemberNum,
                                numberOfTotal = recruitmentModel.maxMemberNum,
                                description = recruitmentModel.title,
                                posterUrl = recruitmentModel.showPoster,
                                date = recruitmentModel.showAt?.toDateWithDay(),
                                time = recruitmentModel.showAt?.toTime(),
                                location = recruitmentModel.facilityName,
                                onClick = {
                                    onNavigateRecruitmentDetail(
                                        myRecruitmentPartTye,
                                        recruitmentModel.id
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
