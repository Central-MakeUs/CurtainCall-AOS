package com.cmc.curtaincall.feature.mypage.writing

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.cmc.curtaincall.common.designsystem.component.appbars.CurtainCallCenterTopAppBarWithBack
import com.cmc.curtaincall.common.designsystem.R
import com.cmc.curtaincall.common.designsystem.component.basic.SystemUiStatusBar
import com.cmc.curtaincall.common.designsystem.custom.show.MyReviewContent
import com.cmc.curtaincall.common.designsystem.theme.CurtainCallTheme
import com.cmc.curtaincall.common.designsystem.theme.Grey4
import com.cmc.curtaincall.common.designsystem.theme.White

@Composable
internal fun MyPageWritingScreen(
    myPageWritingViewModel: MyPageWritingViewModel = hiltViewModel(),
    onNavigateToReviewCreate: (String, Int) -> Unit = { _, _ -> },
    onBack: () -> Unit = {}
) {
    SystemUiStatusBar(White)
    Scaffold(
        topBar = {
            CurtainCallCenterTopAppBarWithBack(
                title = stringResource(R.string.mypage_writing),
                onBack = onBack
            )
        }
    ) { paddingValues ->
        MyPageWritingContent(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(CurtainCallTheme.colors.background),
            onNavigateToReviewCreate = onNavigateToReviewCreate
        )
    }

    LaunchedEffect(Unit) {
        myPageWritingViewModel.fetchMyReviews()
    }
}

@Composable
private fun MyPageWritingContent(
    modifier: Modifier = Modifier,
    myPageWritingViewModel: MyPageWritingViewModel = hiltViewModel(),
    onNavigateToReviewCreate: (String, Int) -> Unit = { _, _ -> },
) {
    val myReviewModels = myPageWritingViewModel.myReviewModels.collectAsLazyPagingItems()
    if (myReviewModels.itemCount == 0) {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.weight(260f))
            Text(
                text = stringResource(R.string.mypage_empty_writing),
                style = CurtainCallTheme.typography.body3.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = CurtainCallTheme.colors.primary
                )
            )
            Text(
                text = stringResource(R.string.mypage_empty_writing_guide),
                modifier = Modifier.padding(top = 10.dp),
                style = CurtainCallTheme.typography.body4.copy(
                    color = Grey4
                ),
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.weight(346f))
        }
    } else {
        LazyColumn(
            modifier = modifier,
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(myReviewModels) { myReviewModel ->
                myReviewModel?.let { model ->
                    MyReviewContent(
                        modifier = Modifier.fillMaxWidth(),
                        memberReviewModel = model,
                        onClick = { onNavigateToReviewCreate(model.showId, model.id) }
                    )
                }
            }
        }
    }
}
