package com.cmc.curtaincall.feature.mypage.notice

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.cmc.curtaincall.common.designsystem.R
import com.cmc.curtaincall.common.designsystem.component.appbars.CurtainCallCenterTopAppBarWithBack
import com.cmc.curtaincall.common.designsystem.component.basic.SystemUiStatusBar
import com.cmc.curtaincall.common.designsystem.custom.my.MyNoticeContent
import com.cmc.curtaincall.common.designsystem.theme.White
import com.cmc.curtaincall.common.utility.extensions.convertUIDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MyPageNoticeScreen(
    onNavigateToNoticeDetail: (Int) -> Unit = {},
    onBack: () -> Unit
) {
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
        MyPageNoticeContent(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(White),
            onNavigateToNoticeDetail = onNavigateToNoticeDetail
        )
    }
}

@Composable
private fun MyPageNoticeContent(
    modifier: Modifier = Modifier,
    myPageNoticeViewModel: MyPageNoticeViewModel = hiltViewModel(),
    onNavigateToNoticeDetail: (Int) -> Unit = {}
) {
    val notices = myPageNoticeViewModel.notices.collectAsLazyPagingItems()
    Column(modifier) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
        ) {
            items(notices.itemCount) { index ->
                notices[index]?.let { notice ->
                    MyNoticeContent(
                        title = notice.title,
                        createdAt = notice.createdAt.convertUIDate(),
                        isLast = index == notices.itemCount - 1,
                        onClick = { onNavigateToNoticeDetail(notice.id) }
                    )
                }
            }
        }
    }
}
