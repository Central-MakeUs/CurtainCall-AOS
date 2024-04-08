package com.cmc.curtaincall.feature.partymember.livetalk

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.cmc.curtaincall.common.designsystem.R
import com.cmc.curtaincall.common.designsystem.component.basic.CurtainCallMultiLineTextField
import com.cmc.curtaincall.common.designsystem.extensions.toSp
import com.cmc.curtaincall.common.designsystem.theme.Cetacean_Blue
import com.cmc.curtaincall.common.designsystem.theme.Chinese_Black
import com.cmc.curtaincall.common.designsystem.theme.Cultured
import com.cmc.curtaincall.common.designsystem.theme.Me_Pink
import com.cmc.curtaincall.common.designsystem.theme.Nero
import com.cmc.curtaincall.common.designsystem.theme.Roman_Silver
import com.cmc.curtaincall.common.designsystem.theme.White
import com.cmc.curtaincall.common.designsystem.theme.spoqahansanseeo
import com.cmc.curtaincall.common.utility.extensions.toDateInKorea
import com.cmc.curtaincall.common.utility.extensions.toTimePM
import com.cmc.curtaincall.feature.partymember.test.PartyMemberDetail2ViewModel
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.models.Channel
import io.getstream.chat.android.client.models.User
import io.getstream.chat.android.compose.state.messages.list.MessageItemState
import io.getstream.chat.android.compose.ui.messages.composer.MessageComposer
import io.getstream.chat.android.compose.ui.messages.header.MessageListHeader
import io.getstream.chat.android.compose.ui.messages.list.MessageList
import io.getstream.chat.android.compose.ui.theme.ChatTheme
import io.getstream.chat.android.compose.ui.theme.StreamColors
import io.getstream.chat.android.compose.viewmodel.messages.MessageComposerViewModel
import io.getstream.chat.android.compose.viewmodel.messages.MessageListViewModel
import io.getstream.chat.android.compose.viewmodel.messages.MessagesViewModelFactory

@Composable
fun PartyMemberLiveTalkScreen(
    partyMemberDetailViewModel: PartyMemberDetail2ViewModel = hiltViewModel(),
    chatClient: ChatClient,
    onBack: () -> Unit
) {
    val partyMemberDetailState by partyMemberDetailViewModel.uiState.collectAsStateWithLifecycle()
    ChatTheme(
        isInDarkMode = false,
        colors = StreamColors.defaultColors().copy(
            appBackground = Cultured,
            barsBackground = Cultured,
            ownMessagesBackground = Color.Transparent,
            otherMessagesBackground = Color.Transparent
        )
    ) {
//        PartyMemberLiveTalkContent(
//            partyId = partyMemberDetailState.partyDetailModel.id,
//            showName = if (partyMemberDetailState.partyDetailModel.category == PartyType.ETC.category) {
//                partyMemberDetailState.partyDetailModel.title
//            } else {
//                partyMemberDetailState.partyDetailModel.showName ?: ""
//            },
//            showAt = if (partyMemberDetailState.partyDetailModel.category == PartyType.ETC.category) {
//                partyMemberDetailState.partyDetailModel.createdAt
//            } else {
//                partyMemberDetailState.partyDetailModel.showAt ?: ""
//            },
//            user = partyMemberDetailState.user,
//            chatClient = chatClient,
//            onBack = onBack
//        )
    }
}

@Composable
private fun PartyMemberLiveTalkContent(
    partyId: Int,
    showName: String,
    showAt: String,
    user: User,
    chatClient: ChatClient,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val messageFactory by lazy {
        MessagesViewModelFactory(
            context = context,
            channelId = "messaging:PARTY-$partyId",
            chatClient = chatClient
        )
    }

    val messageListViewModel = viewModel(MessageListViewModel::class.java, factory = messageFactory)
    val messageComposerViewModel = viewModel(MessageComposerViewModel::class.java, factory = messageFactory)
    val messageComposerState by messageComposerViewModel.messageComposerState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Cultured),
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
                            .padding(top = 10.dp, bottom = 28.dp)
                            .padding(horizontal = 20.dp)
                            .fillMaxWidth()
                    ) {
                        CurtainCallMultiLineTextField(
                            value = messageComposerState.inputValue,
                            onValueChange = { messageComposerViewModel.setMessageInput(it) },
                            modifier = Modifier
                                .weight(1f)
                                .height(36.dp),
                            fontSize = 14.dp.toSp(),
                            shape = RoundedCornerShape(20.dp),
                            containerColor = White,
                            contentColor = Cetacean_Blue,
                            contentModifier = Modifier
                                .padding(top = 7.dp)
                                .padding(horizontal = 18.dp),
                            placeholder = "메세지 입력..."
                        )
                        Box(
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .size(36.dp)
                                .background(White, CircleShape)
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
                                tint = Cetacean_Blue
                            )
                        }
                    }
                },
                trailingContent = {}
            )
        }
    ) { paddingValues ->
        PartyMemberLiveTalkMessageList(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            messageListViewModel = messageListViewModel,
            partyId = partyId,
            showName = showName,
            showAt = showAt,
            user = user,
            onBack = onBack
        )
    }
}

@Composable
private fun PartyMemberLiveTalkMessageList(
    modifier: Modifier = Modifier,
    messageListViewModel: MessageListViewModel,
    partyId: Int,
    showName: String,
    showAt: String,
    user: User,
    onBack: () -> Unit
) {
    Column(modifier) {
        MessageListHeader(
            channel = Channel(
                cid = "messaging:PARTY-$partyId",
                id = "PARTY-$partyId"
            ),
            currentUser = user,
            modifier = Modifier.fillMaxWidth(),
            color = Cultured,
            leadingContent = {},
            centerContent = {
                Column(Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .padding(top = 20.dp, bottom = 18.dp)
                            .padding(horizontal = 10.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_back),
                            contentDescription = null,
                            modifier = Modifier
                                .size(18.dp)
                                .clickable { onBack() },
                            tint = Nero
                        )
                        Column(
                            modifier = Modifier.padding(start = 6.dp)
                        ) {
                            Text(
                                text = showName,
                                color = Chinese_Black,
                                fontSize = 17.dp.toSp(),
                                fontWeight = FontWeight.Bold,
                                fontFamily = spoqahansanseeo,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "일시 | ${showAt.toDateInKorea()}",
                                modifier = Modifier.padding(top = 4.dp),
                                color = Roman_Silver,
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
        MessageList(
            viewModel = messageListViewModel,
            modifier = Modifier
                .fillMaxSize()
                .background(White),
            emptyContent = {},
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
                                    .background(Me_Pink, RoundedCornerShape(topStart = 10.dp, bottomStart = 10.dp, bottomEnd = 10.dp))
                                    .padding(horizontal = 12.dp, vertical = 6.dp),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Text(
                                    text = message.text,
                                    color = White,
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
                                color = Chinese_Black,
                                fontSize = 13.dp.toSp()
                            )
                            Row(
                                modifier = Modifier.padding(top = 4.dp),
                                verticalAlignment = Alignment.Bottom
                            ) {
                                Box(
                                    modifier = Modifier
                                        .background(Cultured, RoundedCornerShape(topEnd = 10.dp, bottomStart = 10.dp, bottomEnd = 10.dp))
                                        .padding(horizontal = 12.dp, vertical = 6.dp),
                                    contentAlignment = Alignment.CenterStart
                                ) {
                                    Text(
                                        text = message.text,
                                        color = Nero,
                                        fontSize = 14.dp.toSp()
                                    )
                                }
                                Text(
                                    text = message.createdAt?.toString()?.toTimePM() ?: "",
                                    modifier = Modifier.padding(start = 8.dp),
                                    color = Roman_Silver,
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
