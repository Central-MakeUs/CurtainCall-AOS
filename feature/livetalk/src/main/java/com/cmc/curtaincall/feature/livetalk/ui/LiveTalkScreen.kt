package com.cmc.curtaincall.feature.livetalk.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cmc.curtaincall.common.designsystem.R
import com.cmc.curtaincall.common.designsystem.component.appbars.CurtainCallLiveTalkAppBarWithBack
import com.cmc.curtaincall.common.designsystem.component.card.LiveTalkCard
import com.cmc.curtaincall.common.designsystem.extensions.toSp
import com.cmc.curtaincall.common.designsystem.theme.Bright_Gray
import com.cmc.curtaincall.common.designsystem.theme.Cultured
import com.cmc.curtaincall.common.designsystem.theme.CurtainCallTheme
import com.cmc.curtaincall.common.designsystem.theme.White
import com.cmc.curtaincall.common.designsystem.theme.spoqahansanseeo
import com.cmc.curtaincall.common.utility.extensions.toTime
import com.cmc.curtaincall.feature.livetalk.LiveTalkViewModel
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.compose.state.messages.list.MessageItemState
import io.getstream.chat.android.compose.ui.messages.composer.MessageComposer
import io.getstream.chat.android.compose.ui.messages.list.MessageList
import io.getstream.chat.android.compose.ui.theme.ChatTheme
import io.getstream.chat.android.compose.ui.theme.StreamColors
import io.getstream.chat.android.compose.viewmodel.messages.MessageComposerViewModel
import io.getstream.chat.android.compose.viewmodel.messages.MessageListViewModel
import io.getstream.chat.android.compose.viewmodel.messages.MessagesViewModelFactory

@Composable
fun LiveTalkScreen(
    chatClient: ChatClient,
    onBack: () -> Unit = {}
) {
    val context = LocalContext.current
    val messageFactory by lazy {
        MessagesViewModelFactory(
            context = context,
            channelId = "messaging:1234",
            chatClient = chatClient
        )
    }

    ChatTheme(
        isInDarkMode = false,
        colors = StreamColors.defaultColors().copy(
            barsBackground = CurtainCallTheme.colors.primary,
            inputBackground = CurtainCallTheme.colors.secondary
        )
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                CurtainCallLiveTalkAppBarWithBack(
                    title = "데스노트",
                    date = "일시 | 2024.02.24(토), 오후 7:30",
                    containerColor = CurtainCallTheme.colors.primary,
                    contentColor = CurtainCallTheme.colors.onPrimary,
                    onBack = onBack
                )
            },
            bottomBar = {
                LiveTalkMessageComposer(
                    modifier = Modifier
                        .background(CurtainCallTheme.colors.primary)
                        .padding(bottom = 10.dp)
                        .padding(horizontal = 20.dp)
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    messageFactory = messageFactory
                )
            },
            containerColor = CurtainCallTheme.colors.primary
        ) { paddingValues ->
            LiveTalkContent(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .background(CurtainCallTheme.colors.primary),
                chatClient = chatClient,
                messageFactory = messageFactory
            )
        }
    }
}

@Composable
private fun LiveTalkMessageComposer(
    modifier: Modifier = Modifier,
    messageFactory: MessagesViewModelFactory
) {
    val messageComposerViewModel = viewModel(MessageComposerViewModel::class.java, factory = messageFactory)
    MessageComposer(
        modifier = modifier,
        viewModel = messageComposerViewModel,
        integrations = {},
        trailingContent = {},
        input = { inputState ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(42.dp)
                    .background(
                        color = CurtainCallTheme.colors.secondary,
                        shape = RoundedCornerShape(30.dp)
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_livetalk_add),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .size(34.dp),
                    tint = Color.Unspecified
                )
                BasicTextField(
                    value = inputState.inputValue,
                    onValueChange = { messageComposerViewModel.setMessageInput(it) },
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .weight(1f),
                    textStyle = CurtainCallTheme.typography.body4.copy(
                        CurtainCallTheme.colors.primary
                    ),
                    singleLine = true,
                    maxLines = 1
                ) { innerTextField ->
                    innerTextField()
                    if (inputState.inputValue.isEmpty()) {
                        Text(
                            text = "고라파덕으로 메시지 보내기...",
                            style = CurtainCallTheme.typography.body4.copy(
                                CurtainCallTheme.colors.primary
                            )
                        )
                    }
                }
                Icon(
                    painter = painterResource(R.drawable.ic_livetalk_send),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .size(24.dp),
                    tint = Color.Unspecified
                )
            }
        }
    )
}

@Composable
private fun LiveTalkContent(
    modifier: Modifier = Modifier,
    chatClient: ChatClient,
    messageFactory: MessagesViewModelFactory
) {
    val messageListViewModel = viewModel(MessageListViewModel::class.java, factory = messageFactory)
    MessageList(
        viewModel = messageListViewModel,
        modifier = modifier,
        emptyContent = {},
        loadingContent = {}
    ) {
        if (it is MessageItemState) {
            val message = it.message
            if (message.user.id == "146") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = "오후 1:30",
                        style = CurtainCallTheme.typography.caption.copy(
                            color = CurtainCallTheme.colors.onPrimary
                        )
                    )
                    Box(
                        modifier = Modifier
                            .background(
                                color = CurtainCallTheme.colors.secondary,
                                shape = RoundedCornerShape(
                                    topStart = 10.dp,
                                    bottomStart = 10.dp,
                                    bottomEnd = 10.dp
                                )
                            ).padding(horizontal = 10.dp, vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = message.text,
                            style = CurtainCallTheme.typography.body4.copy(
                                color = CurtainCallTheme.colors.primary
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LiveTalkContent(
    liveTalkViewModel: LiveTalkViewModel,
    modifier: Modifier = Modifier,
    chatClient: ChatClient,
    onNavigateDetail: (String, String, String) -> Unit
) {
    val liveTalkShows by liveTalkViewModel.liveTalkShows.collectAsStateWithLifecycle()
    Column(modifier) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(top = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item(span = { GridItemSpan(2) }) {
                Column {
                    Text(
                        text = stringResource(R.string.livetalk_app_bar_title),
                        color = White,
                        fontSize = 24.dp.toSp(),
                        fontWeight = FontWeight.Bold,
                        fontFamily = spoqahansanseeo
                    )
                    Text(
                        text = stringResource(R.string.livetalk_app_bar_description),
                        modifier = Modifier.padding(top = 2.dp),
                        color = White,
                        fontSize = 14.dp.toSp(),
                        fontWeight = FontWeight.Medium,
                        fontFamily = spoqahansanseeo
                    )
                    Spacer(modifier = Modifier.size(30.dp))
                }
            }

            if (liveTalkShows.isEmpty()) {
                item(span = { GridItemSpan(2) }) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 190.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_error_report),
                            contentDescription = null,
                            modifier = Modifier.size(60.dp),
                            tint = Cultured
                        )
                        Text(
                            text = "지금은 진행 중인 라이브톡이 없어요!",
                            modifier = Modifier.padding(top = 12.dp),
                            color = Bright_Gray,
                            fontSize = 15.dp.toSp(),
                            fontWeight = FontWeight.Medium,
                            fontFamily = spoqahansanseeo
                        )
                    }
                }
            } else {
                items(liveTalkShows) { liveTalkShow ->
                    LiveTalkCard(
                        modifier = Modifier.width(152.dp),
                        startTime = liveTalkShow.showAt.toTime(),
                        endTime = liveTalkShow.showEndAt.toTime(),
                        posterUrl = liveTalkShow.poster,
                        name = liveTalkShow.name,
                        facilityName = liveTalkShow.facilityName,
                        onClick = {
                            onNavigateDetail(
                                liveTalkShow.id,
                                liveTalkShow.name,
                                liveTalkShow.showAt
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun LiveTalkSearchContent(
    modifier: Modifier = Modifier
) {
    Column(modifier) {
    }
}
