package com.cmc.curtaincall.feature.mypage.party

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import androidx.paging.compose.itemsIndexed
import com.cmc.curtaincall.common.designsystem.R
import com.cmc.curtaincall.common.designsystem.component.appbars.CurtainCallCenterTopAppBarWithBack
import com.cmc.curtaincall.common.designsystem.component.basic.SystemUiStatusBar
import com.cmc.curtaincall.common.designsystem.component.chips.CurtainCallBasicChip
import com.cmc.curtaincall.common.designsystem.component.dialogs.CurtainCallSelectDialog
import com.cmc.curtaincall.common.designsystem.custom.my.MyParticipationtItem
import com.cmc.curtaincall.common.designsystem.custom.my.MyRecruitmentItem
import com.cmc.curtaincall.common.designsystem.theme.CurtainCallTheme
import com.cmc.curtaincall.common.designsystem.theme.Grey4
import com.cmc.curtaincall.common.designsystem.theme.Grey8

@Composable
internal fun MyPagePartyScreen(
    myPagePartyViewModel: MyPagePartyViewModel = hiltViewModel(),
    onNavigateToParty: () -> Unit = {},
    onNavigateToPartyDetail: (Int?, String?) -> Unit = { _, _ -> },
    onNavigateToPartyEdit: (Int?, String?) -> Unit = { _, _ -> },
    onBack: () -> Unit = {}
) {
    SystemUiStatusBar(Grey8)
    Scaffold(
        topBar = {
            CurtainCallCenterTopAppBarWithBack(
                title = stringResource(R.string.mypage_profile_party_activity),
                containerColor = Grey8,
                onBack = onBack
            )
        }
    ) { paddingValues ->
        MyPagePartyContent(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(Grey8),
            onNavigateToParty = onNavigateToParty,
            onNavigateToPartyDetail = onNavigateToPartyDetail,
            onNavigateToPartyEdit = onNavigateToPartyEdit
        )
    }

    LaunchedEffect(Unit) {
        myPagePartyViewModel.checkMemberID()
    }
}

@Composable
private fun MyPagePartyContent(
    modifier: Modifier = Modifier,
    myPagePartyViewModel: MyPagePartyViewModel = hiltViewModel(),
    onNavigateToParty: () -> Unit = {},
    onNavigateToPartyDetail: (Int?, String?) -> Unit = { _, _ -> },
    onNavigateToPartyEdit: (Int?, String?) -> Unit = { _, _ -> },
) {
    val partyType by myPagePartyViewModel.myPartyType.collectAsStateWithLifecycle()
    val memberInfo by myPagePartyViewModel.memberInfo.collectAsStateWithLifecycle()
    val myParticipationModels = myPagePartyViewModel.myParticipationModels.collectAsLazyPagingItems()
    val myRecruitmentModels = myPagePartyViewModel.myRecruitmentModels.collectAsLazyPagingItems()
    var selectEditIndex by remember { mutableIntStateOf(Int.MIN_VALUE) }
    var cancelPartyId by remember { mutableStateOf(Int.MIN_VALUE) }

    if (cancelPartyId != Int.MIN_VALUE) {
        CurtainCallSelectDialog(
            title = stringResource(R.string.party_member_cancel_participate_popup_title),
            description = stringResource(R.string.party_member_cancel_participate_popup_description),
            cancelButtonText = stringResource(R.string.dismiss),
            actionButtonText = stringResource(R.string.party_member_cancel_participate_popup_positive),
            onAction = {
                myPagePartyViewModel.cancelParty(cancelPartyId)
                cancelPartyId = Int.MIN_VALUE
            },
            onCancel = { cancelPartyId = Int.MIN_VALUE },
            onDismiss = { cancelPartyId = Int.MIN_VALUE }
        )
    }

    Column(modifier) {
        Row(
            modifier = Modifier
                .padding(top = 12.dp)
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
                .height(45.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CurtainCallBasicChip(
                text = MyPartyType.Participation.value,
                textStyle = CurtainCallTheme.typography.body2.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                isSelect = partyType == MyPartyType.Participation,
                onClick = { myPagePartyViewModel.selectMyPartyType(MyPartyType.Participation) }
            )
            CurtainCallBasicChip(
                modifier = Modifier.padding(start = 8.dp),
                text = MyPartyType.Recruitment.value,
                textStyle = CurtainCallTheme.typography.body2.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                isSelect = partyType == MyPartyType.Recruitment,
                onClick = { myPagePartyViewModel.selectMyPartyType(MyPartyType.Recruitment) }
            )
        }
        if (partyType == MyPartyType.Participation) {
            if (myParticipationModels.itemCount == 0) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(Modifier.weight(183f))
                    Text(
                        text = stringResource(R.string.mypage_myparty_participation_title),
                        style = CurtainCallTheme.typography.body3.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = CurtainCallTheme.colors.primary
                        )
                    )
                    Text(
                        text = stringResource(R.string.mypage_myparty_participation_subtitle),
                        modifier = Modifier.padding(top = 10.dp),
                        style = CurtainCallTheme.typography.body4.copy(
                            color = Grey4
                        ),
                        textAlign = TextAlign.Center
                    )
                    Box(
                        modifier = Modifier
                            .padding(top = 20.dp)
                            .background(CurtainCallTheme.colors.secondary, RoundedCornerShape(40.dp))
                            .padding(horizontal = 18.dp, vertical = 8.dp)
                            .clickable { onNavigateToParty() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.mypage_myparty_navigate_to_party),
                            style = CurtainCallTheme.typography.body4.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = CurtainCallTheme.colors.primary
                            )
                        )
                    }
                    Spacer(Modifier.weight(312f))
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        horizontal = 20.dp,
                        vertical = 12.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(myParticipationModels) { myParticipationModel ->
                        myParticipationModel?.let {
                            MyParticipationtItem(
                                myParticipationModel = it,
                                onNavigateToDetail = {
                                    onNavigateToPartyDetail(it.id, it.showName)
                                },
                                onCancel = {
                                    cancelPartyId = it.id
                                }
                            )
                        }
                    }
                }
            }
        } else {
            if (myRecruitmentModels.itemCount == 0) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(Modifier.weight(191f))
                    Text(
                        text = stringResource(R.string.mypage_myparty_recruitment_title),
                        style = CurtainCallTheme.typography.body3.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = CurtainCallTheme.colors.primary
                        )
                    )
                    Text(
                        text = stringResource(R.string.mypage_myparty_recruitment_subtitle),
                        modifier = Modifier.padding(top = 10.dp),
                        style = CurtainCallTheme.typography.body4.copy(
                            color = Grey4
                        ),
                        textAlign = TextAlign.Center
                    )
                    Box(
                        modifier = Modifier
                            .padding(top = 20.dp)
                            .background(CurtainCallTheme.colors.secondary, RoundedCornerShape(40.dp))
                            .padding(horizontal = 18.dp, vertical = 8.dp)
                            .clickable { onNavigateToParty() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.mypage_myparty_navigate_to_party),
                            style = CurtainCallTheme.typography.body4.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = CurtainCallTheme.colors.primary
                            )
                        )
                    }
                    Spacer(Modifier.weight(312f))
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        horizontal = 20.dp,
                        vertical = 12.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    itemsIndexed(myRecruitmentModels) { index, myRecruitmentModel ->
                        myRecruitmentModel?.let {
                            MyRecruitmentItem(
                                myRecruitmentModel = it,
                                creatorImageUrl = memberInfo.imageUrl,
                                creatorNickname = memberInfo.nickname,
                                isEditMode = index == selectEditIndex,
                                onMore = {
                                    if (index == selectEditIndex) {
                                        selectEditIndex = Int.MIN_VALUE
                                    } else {
                                        selectEditIndex = index
                                    }
                                },
                                onEdit = {
                                    selectEditIndex = Int.MIN_VALUE
                                    onNavigateToPartyEdit(it.id, it.showName)
                                },
                                onDelete = {
                                    selectEditIndex = Int.MIN_VALUE
                                    myPagePartyViewModel.deleteParty(it.id)
                                },
                                onNavigateToDetail = {
                                    selectEditIndex = Int.MIN_VALUE
                                    onNavigateToPartyDetail(it.id, it.showName)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
