package com.cmc.curtaincall.feature.partymember.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cmc.curtaincall.common.designsystem.R
import com.cmc.curtaincall.common.designsystem.component.appbars.CurtainCallCenterTopAppBarWithBack
import com.cmc.curtaincall.common.designsystem.component.basic.SystemUiStatusBar
import com.cmc.curtaincall.common.designsystem.component.buttons.common.CurtainCallFilledButton
import com.cmc.curtaincall.common.designsystem.theme.CurtainCallTheme

@Composable
fun PartyMemberDetailScreen(
    partyId: Int?,
    showName: String?,
    onBack: () -> Unit = {}
) {
    checkNotNull(partyId)
    checkNotNull(showName)

    SystemUiStatusBar(CurtainCallTheme.colors.primary)
    Scaffold(
        topBar = {
            CurtainCallCenterTopAppBarWithBack(
                title = showName,
                containerColor = CurtainCallTheme.colors.primary,
                contentColor = CurtainCallTheme.colors.onPrimary,
                onBack = onBack
            )
        },
        floatingActionButton = {
            // TODO 파티원 참여 여부에 따라 분기처리
            CurtainCallFilledButton(
                text = stringResource(R.string.participate),
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 14.dp)
                    .fillMaxWidth()
                    .height(51.dp),
                containerColor = CurtainCallTheme.colors.secondary,
                contentColor = CurtainCallTheme.colors.primary,
                onClick = {
                    // TODO 라이브톡 진입
                }
            )
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { paddingValues ->
        PartyMemberDetailContent(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(CurtainCallTheme.colors.primary),
            partyId = partyId
        )
    }
}

@Composable
private fun PartyMemberDetailContent(
    modifier: Modifier = Modifier,
    partyId: Int,
    partyMemberDetailViewModel: PartyMemberDetailViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()
    LaunchedEffect(Unit) {
        partyMemberDetailViewModel.requestPartyDetail(partyId)
    }

    Column(modifier = modifier.verticalScroll(scrollState)) {
        Column(
            modifier = Modifier
                .padding(top = 20.dp, bottom = 121.dp)
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
                .background(CurtainCallTheme.colors.background, RoundedCornerShape(20.dp))
        ) {
        }
    }
}
