package com.cmc.curtaincall.feature.mypage.faq

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cmc.curtaincall.common.designsystem.R
import com.cmc.curtaincall.common.designsystem.component.appbars.CurtainCallCenterTopAppBarWithBack
import com.cmc.curtaincall.common.designsystem.component.chips.CurtainCallBasicChip
import com.cmc.curtaincall.common.designsystem.theme.Black
import com.cmc.curtaincall.common.designsystem.theme.CurtainCallTheme
import com.cmc.curtaincall.common.designsystem.theme.Grey2
import com.cmc.curtaincall.common.designsystem.theme.Grey8
import com.cmc.curtaincall.common.designsystem.theme.Grey9
import com.cmc.curtaincall.domain.enums.FAQMenu
import com.cmc.curtaincall.domain.enums.questions

@Composable
internal fun MyPageFAQScreen(
    onBack: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            CurtainCallCenterTopAppBarWithBack(
                title = stringResource(R.string.mypage_profile_frequently_asked_question),
                onBack = onBack
            )
        }
    ) { paddingValues ->
        MyPageFAQContent(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(CurtainCallTheme.colors.background)
        )
    }
}

@Composable
private fun MyPageFAQContent(
    modifier: Modifier = Modifier,
    myPageFAQViewModel: MyPageFAQViewModel = hiltViewModel()
) {
    val menu by myPageFAQViewModel.menu.collectAsStateWithLifecycle()
    Column(modifier) {
        LazyRow(
            modifier = Modifier
                .padding(top = 30.dp)
                .fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(FAQMenu.values()) {
                CurtainCallBasicChip(
                    text = it.value,
                    isSelect = it == menu,
                    onClick = { myPageFAQViewModel.selectFAQMenu(it) }
                )
            }
        }
        when (menu) {
            FAQMenu.SHOW -> {
                MyPageShowFAQ(
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .fillMaxSize(),
                    qna = menu.questions().toList()
                )
            }

            FAQMenu.PARTY_MEMBER -> {
                MyPagePartyMemberFAQ(
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .fillMaxSize(),
                    qna = menu.questions().toList()
                )
            }

            FAQMenu.PARTY_MEMBER_TALK -> {
                MyPagePartyMemberTalkFAQ(
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .fillMaxSize(),
                    qna = menu.questions().toList()
                )
            }

            FAQMenu.LIVE_TALK -> {
                MyPageLiveTalkFAQ(
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .fillMaxSize(),
                    qna = menu.questions().toList()
                )
            }

            FAQMenu.INQUIRY -> {
                MyPageInquiryFAQ(
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .fillMaxSize(),
                    qna = menu.questions().toList()
                )
            }
        }
    }
}

@Composable
private fun MyPageShowFAQ(
    modifier: Modifier = Modifier,
    qna: List<Pair<String, String>> = listOf()
) {
    LazyColumn(modifier = modifier) {
        itemsIndexed(qna) { index, item ->
            MyPageFAQItem(
                question = item.first,
                answer = item.second,
                isLast = index == qna.lastIndex
            )
        }
    }
}

@Composable
private fun MyPagePartyMemberFAQ(
    modifier: Modifier = Modifier,
    qna: List<Pair<String, String>> = listOf()
) {
    LazyColumn(modifier = modifier) {
        itemsIndexed(qna) { index, item ->
            MyPageFAQItem(
                question = item.first,
                answer = item.second,
                isLast = index == qna.lastIndex
            )
        }
    }
}

@Composable
private fun MyPagePartyMemberTalkFAQ(
    modifier: Modifier = Modifier,
    qna: List<Pair<String, String>> = listOf()
) {
    LazyColumn(modifier = modifier) {
        itemsIndexed(qna) { index, item ->
            MyPageFAQItem(
                question = item.first,
                answer = item.second,
                isLast = index == qna.lastIndex
            )
        }
    }
}

@Composable
private fun MyPageLiveTalkFAQ(
    modifier: Modifier = Modifier,
    qna: List<Pair<String, String>> = listOf()
) {
    LazyColumn(modifier = modifier) {
        itemsIndexed(qna) { index, item ->
            MyPageFAQItem(
                question = item.first,
                answer = item.second,
                isLast = index == qna.lastIndex
            )
        }
    }
}

@Composable
private fun MyPageInquiryFAQ(
    modifier: Modifier = Modifier,
    qna: List<Pair<String, String>> = listOf()
) {
    LazyColumn(modifier = modifier) {
        itemsIndexed(qna) { index, item ->
            MyPageFAQItem(
                question = item.first,
                answer = item.second,
                isLast = index == qna.lastIndex
            )
        }
    }
}

@Composable
private fun MyPageFAQItem(
    question: String,
    answer: String,
    isLast: Boolean = false
) {
    var isClick by remember { mutableStateOf(false) }
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .padding(top = 20.dp)
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = question,
                modifier = Modifier.weight(1f),
                style = CurtainCallTheme.typography.body2.copy(
                    fontWeight = FontWeight.SemiBold
                )
            )
            Icon(
                painter = painterResource(
                    if (isClick) {
                        R.drawable.ic_arrow_up
                    } else {
                        R.drawable.ic_arrow_down
                    }
                ),
                contentDescription = null,
                modifier = Modifier
                    .padding(top = 1.dp)
                    .size(24.dp)
                    .clickable { isClick = isClick.not() },
                tint = Black
            )
        }
        if (isLast.not() && isClick.not()) {
            Spacer(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Grey8)
            )
        }
        if (isClick) {
            Box(
                modifier = Modifier
                    .padding(top = 13.dp)
                    .fillMaxWidth()
                    .background(Grey9)
                    .padding(20.dp)
            ) {
                Text(
                    text = answer,
                    style = CurtainCallTheme.typography.body4.copy(
                        color = Grey2
                    )
                )
            }
        }
    }
}
