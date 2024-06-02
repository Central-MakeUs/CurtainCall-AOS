package com.cmc.curtaincall.feature.livetalk.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.cmc.curtaincall.common.designsystem.R
import com.cmc.curtaincall.common.designsystem.component.appbars.CurtainCallCenterTopAppBarWithBack
import com.cmc.curtaincall.common.designsystem.theme.CurtainCallTheme
import com.cmc.curtaincall.feature.livetalk.LiveTalkViewModel
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.compose.state.messages.list.MessageItemGroupPosition
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
                CurtainCallCenterTopAppBarWithBack(
                    title = "데스노트",
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
                messageFactory = messageFactory
            )
        }
    }
}

@Composable
private fun LiveTalkMessageComposer(
    modifier: Modifier = Modifier,
    liveTalkViewModel: LiveTalkViewModel = hiltViewModel(),
    messageFactory: MessagesViewModelFactory
) {
    val memberName by liveTalkViewModel.memberName.collectAsStateWithLifecycle()
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
                    .heightIn(min = 42.dp)
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
                        .padding(vertical = 4.dp)
                        .weight(1f),
                    textStyle = CurtainCallTheme.typography.body4.copy(
                        CurtainCallTheme.colors.primary
                    )
                ) { innerTextField ->
                    innerTextField()
                    if (inputState.inputValue.isEmpty()) {
                        Text(
                            text = "${memberName}으로 메시지 보내기...",
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
                        .size(24.dp)
                        .clickable {
                            messageComposerViewModel.sendMessage(
                                messageComposerViewModel.buildNewMessage(
                                    inputState.inputValue
                                )
                            )
                        },
                    tint = Color.Unspecified
                )
            }
        }
    )
}

@Composable
private fun LiveTalkContent(
    modifier: Modifier = Modifier,
    messageFactory: MessagesViewModelFactory
) {
    val messageListViewModel = viewModel(MessageListViewModel::class.java, factory = messageFactory)
    MessageList(
        viewModel = messageListViewModel,
        modifier = modifier,
        emptyContent = {},
        loadingContent = {},
    ) { item ->
        if (item is MessageItemState) {
            val message = item.message
            Column(Modifier.fillMaxWidth()) {
                // 내 채팅
                if (item.isMine) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 30.dp, end = 20.dp),
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
                                .weight(1f, false)
                                .padding(start = 6.dp)
                                .background(
                                    color = CurtainCallTheme.colors.secondary,
                                    shape = RoundedCornerShape(
                                        topStart = 10.dp,
                                        bottomStart = 10.dp,
                                        bottomEnd = 10.dp
                                    )
                                )
                                .padding(horizontal = 10.dp, vertical = 8.dp),
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
                } else {
                    // 다른 사용자 채팅의 가장 상단 메세지
                    if (item.groupPosition == MessageItemGroupPosition.Top ||
                        item.groupPosition == MessageItemGroupPosition.None
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(start = 20.dp, end = 30.dp)
                                .fillMaxWidth()
                        ) {
                            AsyncImage(
                                model = message.user.image,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape),
                                placeholder = painterResource(R.drawable.ic_default_profile),
                                error = painterResource(R.drawable.ic_default_profile)
                            )
                            Column(
                                modifier = Modifier
                                    .padding(start = 10.dp)
                                    .fillMaxWidth()
                            ) {
                                Text(
                                    text = message.user.name,
                                    style = CurtainCallTheme.typography.body4.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        color = CurtainCallTheme.colors.onPrimary
                                    )
                                )
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 6.dp),
                                    verticalAlignment = Alignment.Bottom
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .weight(1f, false)
                                            .background(
                                                color = CurtainCallTheme.colors.onPrimary.copy(
                                                    alpha = 0.3f
                                                ),
                                                shape = RoundedCornerShape(
                                                    topEnd = 10.dp,
                                                    bottomStart = 10.dp,
                                                    bottomEnd = 10.dp
                                                )
                                            )
                                            .padding(horizontal = 10.dp, vertical = 8.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = message.text,
                                            style = CurtainCallTheme.typography.body4.copy(
                                                color = CurtainCallTheme.colors.onPrimary
                                            )
                                        )
                                    }
                                    Text(
                                        text = "오후 1:30",
                                        modifier = Modifier
                                            .padding(start = 6.dp),
                                        style = CurtainCallTheme.typography.caption.copy(
                                            color = CurtainCallTheme.colors.onPrimary
                                        )
                                    )
                                }
                            }
                        }
                    } else {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 66.dp, end = 30.dp),
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(1f, false)
                                    .background(
                                        color = CurtainCallTheme.colors.onPrimary.copy(
                                            alpha = 0.3f
                                        ),
                                        shape = RoundedCornerShape(
                                            topEnd = 10.dp,
                                            bottomStart = 10.dp,
                                            bottomEnd = 10.dp
                                        )
                                    )
                                    .padding(horizontal = 10.dp, vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = message.text,
                                    style = CurtainCallTheme.typography.body4.copy(
                                        color = CurtainCallTheme.colors.onPrimary
                                    )
                                )
                            }
                            Text(
                                text = "오후 1:30",
                                modifier = Modifier
                                    .padding(start = 6.dp),
                                style = CurtainCallTheme.typography.caption.copy(
                                    color = CurtainCallTheme.colors.onPrimary
                                )
                            )
                        }
                    }
                }
                Spacer(
                    modifier = Modifier
                        .height(
                            if (item.groupPosition == MessageItemGroupPosition.Bottom ||
                                item.groupPosition == MessageItemGroupPosition.None
                            ) {
                                20.dp
                            } else {
                                8.dp
                            }
                        )
                )
            }
        }
    }
}
