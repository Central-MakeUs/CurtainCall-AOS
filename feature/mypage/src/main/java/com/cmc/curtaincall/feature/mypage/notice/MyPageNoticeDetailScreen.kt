package com.cmc.curtaincall.feature.mypage.notice

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cmc.curtaincall.common.designsystem.R
import com.cmc.curtaincall.common.designsystem.component.appbars.CurtainCallCenterTopAppBarWithBack
import com.cmc.curtaincall.common.designsystem.component.basic.SystemUiStatusBar
import com.cmc.curtaincall.common.designsystem.custom.my.MyNoticeContent
import com.cmc.curtaincall.common.designsystem.theme.CurtainCallTheme
import com.cmc.curtaincall.common.designsystem.theme.Grey2
import com.cmc.curtaincall.common.designsystem.theme.White
import com.cmc.curtaincall.common.utility.extensions.convertUIDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MyPageNoticeDetailScreen(
    myPageNoticeViewModel: MyPageNoticeViewModel = hiltViewModel(),
    noticeId: Int?,
    onBack: () -> Unit = {}
) {
    checkNotNull(noticeId)

    LaunchedEffect(Unit) {
        myPageNoticeViewModel.requestNoticeDetail(noticeId)
    }

    SystemUiStatusBar(White)
    Scaffold(
        topBar = {
            CurtainCallCenterTopAppBarWithBack(
                title = stringResource(R.string.mypage_profile_notice),
                onBack = onBack
            )
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { paddingValues ->
        MyPageNoticeDetailContent(
            myPageNoticeViewModel = myPageNoticeViewModel,
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(White),
            onBack = onBack
        )
    }
}

@Composable
private fun MyPageNoticeDetailContent(
    modifier: Modifier = Modifier,
    myPageNoticeViewModel: MyPageNoticeViewModel = hiltViewModel(),
    onBack: () -> Unit = {}
) {
    val noticeDetail by myPageNoticeViewModel.noticeDetail.collectAsStateWithLifecycle()
    Column(modifier) {
        Column(
            modifier = Modifier
                .padding(vertical = 10.dp)
                .fillMaxWidth()
        ) {
            MyNoticeContent(
                title = noticeDetail.title,
                createdAt = noticeDetail.createdAt.convertUIDate(),
                onClick = onBack
            )
            Text(
                text = noticeDetail.content,
                modifier = Modifier
                    .padding(top = 20.dp)
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth(),
                style = CurtainCallTheme.typography.body3.copy(
                    color = Grey2
                )
            )
        }
    }
}
