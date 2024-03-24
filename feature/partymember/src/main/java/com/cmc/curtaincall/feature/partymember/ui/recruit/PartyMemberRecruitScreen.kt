package com.cmc.curtaincall.feature.partymember.ui.recruit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.cmc.curtaincall.common.designsystem.R
import com.cmc.curtaincall.common.designsystem.component.appbars.CurtainCallSearchTitleTopAppBarWithBack
import com.cmc.curtaincall.common.designsystem.component.basic.SystemUiStatusBar
import com.cmc.curtaincall.common.designsystem.theme.CurtainCallTheme

@Composable
fun PartyMemberRecruitScreen(
    onBack: () -> Unit = {}
) {
    SystemUiStatusBar(CurtainCallTheme.colors.background)
    Scaffold(
        topBar = {
            CurtainCallSearchTitleTopAppBarWithBack(
                title = stringResource(R.string.party_member_recruit),
                onBack = onBack
            )
        }
    ) { paddingValues ->
        PartyMemberRecruitContent(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(CurtainCallTheme.colors.background)
        )
    }
}

@Composable
private fun PartyMemberRecruitContent(
    modifier: Modifier = Modifier
) {
    Column(modifier) {
    }
}
