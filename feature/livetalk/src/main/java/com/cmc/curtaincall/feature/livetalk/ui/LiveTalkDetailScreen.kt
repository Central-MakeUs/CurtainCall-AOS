package com.cmc.curtaincall.feature.livetalk.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.cmc.curtaincall.common.designsystem.R
import com.cmc.curtaincall.common.designsystem.component.basic.CurtainCallMultiLineTextField
import com.cmc.curtaincall.common.designsystem.extensions.toSp
import com.cmc.curtaincall.common.designsystem.theme.Cetacean_Blue
import com.cmc.curtaincall.common.designsystem.theme.Eggplant
import com.cmc.curtaincall.common.designsystem.theme.Electric_Indigo
import com.cmc.curtaincall.common.designsystem.theme.Harmonies
import com.cmc.curtaincall.common.designsystem.theme.Roman_Silver
import com.cmc.curtaincall.common.designsystem.theme.White
import com.cmc.curtaincall.common.designsystem.theme.spoqahansanseeo
import com.cmc.curtaincall.common.utility.extensions.toDateInKorea
import com.cmc.curtaincall.common.utility.extensions.toTimePM
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.models.Channel
import io.getstream.chat.android.client.models.User
import io.getstream.chat.android.compose.state.messages.list.MessageItemState
import io.getstream.chat.android.compose.ui.messages.composer.MessageComposer
import io.getstream.chat.android.compose.ui.messages.header.MessageListHeader
import io.getstream.chat.android.compose.ui.messages.list.MessageList
import io.getstream.chat.android.compose.viewmodel.messages.MessageComposerViewModel
import io.getstream.chat.android.compose.viewmodel.messages.MessageListViewModel
import io.getstream.chat.android.compose.viewmodel.messages.MessagesViewModelFactory

@Composable
internal fun LiveTalkDetailScreen(
    // liveTalkViewModel: LiveTalkViewModel = hiltViewModel(),
    chatClient: ChatClient,
    showId: String,
    showName: String,
    showAt: String,
    onBack: () -> Unit
) {
//    val systemUiController = rememberSystemUiController()
//    systemUiController.setStatusBarColor(Electric_Indigo)
//
//    val user by liveTalkViewModel.userState.collectAsStateWithLifecycle()
//    ChatTheme(
//        isInDarkMode = false,
//        colors = StreamColors.defaultColors().copy(
//            appBackground = Cetacean_Blue,
//            barsBackground = Lavendar_Phlox,
//            ownMessagesBackground = Color.Transparent,
//            otherMessagesBackground = Color.Transparent
//        )
//    ) {
//        LiveTalkDetailContent(
//            chatClient = chatClient,
//            showId = showId,
//            showName = showName,
//            showAt = showAt,
//            user = user,
//            onBack = onBack
//        )
//    }
}

@Composable
private fun LiveTalkDetailContent(
    chatClient: ChatClient,
    showId: String,
    showName: String,
    showAt: String,
    user: User,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val messageFactory by lazy {
        MessagesViewModelFactory(
            context = context,
            channelId = "messaging:$showId-${showAt.take(13)}",
            chatClient = chatClient
        )
    }

    val messageListViewModel = viewModel(MessageListViewModel::class.java, factory = messageFactory)
    val messageComposerViewModel = viewModel(MessageComposerViewModel::class.java, factory = messageFactory)
    val messageComposerState by messageComposerViewModel.messageComposerState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Cetacean_Blue),
        topBar = {
            MessageListHeader(
                channel = Channel(
                    cid = "messaging:$showId-${showAt.take(13)}",
                    id = "$showId-${showAt.take(13)}"
                ),
                currentUser = user,
                modifier = Modifier.fillMaxWidth(),
                color = Electric_Indigo,
                leadingContent = {},
                centerContent = {
                    Column(Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier
                                .padding(top = 20.dp, bottom = 18.dp)
                                .padding(horizontal = 10.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_arrow_back),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(18.dp)
                                    .clickable { onBack() },
                                tint = White
                            )
                            Column(
                                modifier = Modifier.padding(start = 6.dp)
                            ) {
                                Text(
                                    text = showName,
                                    color = White,
                                    fontSize = 17.dp.toSp(),
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = spoqahansanseeo,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = "일시 | ${showAt.toDateInKorea()}",
                                    modifier = Modifier.padding(top = 4.dp),
                                    color = White,
                                    fontSize = 14.dp.toSp(),
                                    fontWeight = FontWeight.Medium,
                                    fontFamily = spoqahansanseeo
                                )
                            }
                        }
                    }
                },
                trailingContent = {}
            )
        },
        bottomBar = {
            MessageComposer(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 74.dp),
                viewModel = messageComposerViewModel,
                headerContent = {},
                footerContent = {},
                mentionPopupContent = {},
                commandPopupContent = {},
                integrations = {},
                input = { inputState ->
                    Row(
                        modifier = Modifier
                            .padding(vertical = 10.dp)
                            .padding(horizontal = 20.dp)
                            .fillMaxWidth()
                    ) {
                        CurtainCallMultiLineTextField(
                            value = messageComposerState.inputValue,
                            onValueChange = { messageComposerViewModel.setMessageInput(it) },
                            modifier = Modifier
                                .weight(1f)
                                .heightIn(min = 36.dp, max = 108.dp),
                            fontSize = 14.dp.toSp(),
                            shape = RoundedCornerShape(20.dp),
                            containerColor = Eggplant,
                            contentColor = White,
                            contentModifier = Modifier
                                .padding(top = 7.dp)
                                .padding(horizontal = 18.dp),
                            placeholder = "메세지 입력..."
                        )
                        Box(
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .size(36.dp)
                                .background(Eggplant, CircleShape)
                                .clickable {
                                    messageComposerViewModel.sendMessage(
                                        messageComposerViewModel.buildNewMessage(
                                            messageComposerState.inputValue
                                        )
                                    )
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Send,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp),
                                tint = White
                            )
                        }
                    }
                },
                trailingContent = {}
            )
        }
    ) { paddingValues ->
        LiveTalkMessageList(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            messageListViewModel = messageListViewModel,
            showId = showId,
            showName = showName,
            showAt = showAt,
            user = user,
            onBack = onBack
        )
    }
}

@Composable
private fun LiveTalkMessageList(
    modifier: Modifier = Modifier,
    messageListViewModel: MessageListViewModel,
    showId: String,
    showName: String,
    showAt: String,
    user: User,
    onBack: () -> Unit
) {
    Column(modifier) {
        MessageList(
            viewModel = messageListViewModel,
            modifier = Modifier
                .fillMaxSize()
                .background(Cetacean_Blue),
            emptyContent = {
                Box(Modifier.fillMaxSize().background(Cetacean_Blue))
            },
            loadingContent = {}
        ) {
            if (it is MessageItemState) {
                val message = it.message
                if (message.user.id == user.id) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp, horizontal = 20.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = message.createdAt?.toString()?.toTimePM() ?: "",
                                modifier = Modifier.padding(end = 8.dp),
                                color = Roman_Silver,
                                fontSize = 10.dp.toSp()
                            )
                            Box(
                                modifier = Modifier
                                    .background(White, RoundedCornerShape(topStart = 10.dp, bottomStart = 10.dp, bottomEnd = 10.dp))
                                    .padding(horizontal = 12.dp, vertical = 6.dp),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Text(
                                    text = message.text,
                                    color = Cetacean_Blue,
                                    fontSize = 14.dp.toSp(),
                                    fontWeight = FontWeight.Medium,
                                    fontFamily = spoqahansanseeo
                                )
                            }
                        }
                    }
                } else {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp, horizontal = 20.dp),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        AsyncImage(
                            model = message.user.image,
                            contentDescription = null,
                            error = painterResource(R.drawable.ic_default_profile),
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        Column(Modifier.padding(start = 12.dp)) {
                            Text(
                                text = message.user.name,
                                modifier = Modifier.padding(top = 5.dp),
                                color = White,
                                fontSize = 13.dp.toSp()
                            )
                            Row(
                                modifier = Modifier.padding(top = 4.dp),
                                verticalAlignment = Alignment.Bottom
                            ) {
                                Box(
                                    modifier = Modifier
                                        .background(Harmonies, RoundedCornerShape(topEnd = 10.dp, bottomStart = 10.dp, bottomEnd = 10.dp))
                                        .padding(horizontal = 12.dp, vertical = 6.dp),
                                    contentAlignment = Alignment.CenterStart
                                ) {
                                    Text(
                                        text = message.text,
                                        color = White,
                                        fontSize = 14.dp.toSp()
                                    )
                                }
                                Text(
                                    text = message.createdAt?.toString()?.toTimePM() ?: "",
                                    modifier = Modifier.padding(start = 8.dp),
                                    color = White,
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
