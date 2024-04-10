package com.cmc.curtaincall.feature.mypage.setting

import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cmc.curtaincall.common.designsystem.R
import com.cmc.curtaincall.common.designsystem.component.appbars.CurtainCallCenterTopAppBarWithBack
import com.cmc.curtaincall.common.designsystem.component.buttons.common.CurtainCallFilledButton
import com.cmc.curtaincall.common.designsystem.theme.CurtainCallTheme
import com.cmc.curtaincall.common.designsystem.theme.Grey1
import com.cmc.curtaincall.common.designsystem.theme.Grey2
import com.cmc.curtaincall.common.designsystem.theme.Grey4
import com.cmc.curtaincall.common.designsystem.theme.Grey6
import com.cmc.curtaincall.common.designsystem.theme.Grey8
import com.cmc.curtaincall.common.designsystem.theme.Grey9
import com.cmc.curtaincall.common.designsystem.theme.Red
import com.cmc.curtaincall.common.designsystem.theme.White
import kotlinx.coroutines.flow.collectLatest

private enum class DeletePhrase {
    FIRST, SECOND
}

@Composable
fun MyPageDeleteMemberScreen(
    myPageDeleteMemberViewModel: MyPageDeleteMemberViewModel = hiltViewModel(),
    onDeleteMember: () -> Unit,
    onBack: () -> Unit
) {
    var phrase by remember { mutableStateOf(DeletePhrase.FIRST) }
    val deleteReason by myPageDeleteMemberViewModel.deleteReason.collectAsStateWithLifecycle()
    val content by myPageDeleteMemberViewModel.content.collectAsStateWithLifecycle()

    LaunchedEffect(myPageDeleteMemberViewModel) {
        myPageDeleteMemberViewModel.deleteComplete.collectLatest { deleteComplete ->
            if (deleteComplete) {
                onDeleteMember()
            }
        }
    }

    Scaffold(
        topBar = {
            CurtainCallCenterTopAppBarWithBack(
                title = stringResource(R.string.mypage_setting_account_delete),
                onBack = {
                    if (phrase == DeletePhrase.FIRST) {
                        onBack()
                    } else {
                        phrase = DeletePhrase.FIRST
                    }
                }
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            CurtainCallFilledButton(
                modifier = Modifier
                    .padding(bottom = 14.dp)
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth()
                    .height(51.dp),
                text = when (phrase) {
                    DeletePhrase.FIRST -> stringResource(R.string.next)
                    DeletePhrase.SECOND -> stringResource(R.string.mypage_setting_account_delete)
                },
                enabled = when (phrase) {
                    DeletePhrase.FIRST -> deleteReason != DeleteReason.NONE
                    else -> true
                },
                onClick = {
                    when (phrase) {
                        DeletePhrase.FIRST -> {
                            phrase = DeletePhrase.SECOND
                        }

                        DeletePhrase.SECOND -> {
                            myPageDeleteMemberViewModel.deleteMember()
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        when (phrase) {
            DeletePhrase.FIRST -> {
                MyPageDeleteMemberFirstContent(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize()
                        .background(CurtainCallTheme.colors.background),
                    deleteReason = deleteReason,
                    onChangeDeleteReason = { myPageDeleteMemberViewModel.changeDeleteReason(it) },
                    content = content,
                    onChangeContent = { myPageDeleteMemberViewModel.changeContent(it) }
                )
            }

            else -> {
                MyPageDeleteMemberSecondContent(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize()
                        .background(CurtainCallTheme.colors.background)
                )
            }
        }
    }
}

@Composable
private fun MyPageDeleteMemberSecondContent(
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Column(
            modifier = Modifier
                .padding(top = 30.dp)
                .padding(horizontal = 20.dp)
        ) {
            Text(
                text = stringResource(R.string.mypage_setting_delete_member_second_title),
                style = CurtainCallTheme.typography.subTitle2
            )
            Text(
                text = stringResource(R.string.mypage_setting_delete_member_second_subtitle),
                modifier = Modifier.padding(top = 14.dp),
                style = CurtainCallTheme.typography.body3.copy(
                    color = Grey4
                )
            )
            Row(
                modifier = Modifier
                    .padding(top = 40.dp)
                    .fillMaxWidth()
            ) {
                Spacer(
                    modifier = Modifier
                        .padding(top = 7.dp)
                        .size(6.dp)
                        .background(
                            color = CurtainCallTheme.colors.primary,
                            shape = CircleShape
                        )
                )
                Text(
                    text = stringResource(R.string.mypage_setting_delete_member_second_description1),
                    modifier = Modifier.padding(start = 8.dp),
                    style = CurtainCallTheme.typography.body3.copy(
                        color = Grey2
                    )
                )
            }
            Row(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .fillMaxWidth()
            ) {
                Spacer(
                    modifier = Modifier
                        .padding(top = 7.dp)
                        .size(6.dp)
                        .background(
                            color = CurtainCallTheme.colors.primary,
                            shape = CircleShape
                        )
                )
                Text(
                    text = stringResource(R.string.mypage_setting_delete_member_second_description2),
                    modifier = Modifier.padding(start = 8.dp),
                    style = CurtainCallTheme.typography.body3.copy(
                        color = Grey2
                    )
                )
            }
            Row(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .fillMaxWidth()
            ) {
                Spacer(
                    modifier = Modifier
                        .padding(top = 7.dp)
                        .size(6.dp)
                        .background(
                            color = CurtainCallTheme.colors.primary,
                            shape = CircleShape
                        )
                )
                Text(
                    text = stringResource(R.string.mypage_setting_delete_member_second_description3),
                    modifier = Modifier.padding(start = 8.dp),
                    style = CurtainCallTheme.typography.body3.copy(
                        color = Grey2
                    )
                )
            }
            Text(
                text = stringResource(R.string.mypage_setting_delete_member_continue),
                modifier = Modifier.padding(top = 40.dp),
                style = CurtainCallTheme.typography.subTitle4
            )
        }
    }
}

@Composable
private fun MyPageDeleteMemberFirstContent(
    modifier: Modifier = Modifier,
    deleteReason: DeleteReason,
    onChangeDeleteReason: (DeleteReason) -> Unit = {},
    content: String = "",
    onChangeContent: (String) -> Unit = {}
) {
    val scrollState = rememberScrollState()
    Column(modifier.verticalScroll(scrollState)) {
        Column(
            modifier = Modifier
                .padding(top = 30.dp)
                .padding(horizontal = 20.dp)
                .fillMaxSize()
        ) {
            Text(
                text = stringResource(R.string.mypage_setting_delete_member_first_title),
                style = CurtainCallTheme.typography.subTitle2
            )
            Text(
                text = stringResource(R.string.mypage_setting_delete_member_first_subtitle),
                modifier = Modifier.padding(top = 14.dp),
                style = CurtainCallTheme.typography.body3.copy(
                    color = Grey4
                )
            )
            Spacer(Modifier.height(30.dp))
            DeleteReason.values().dropLast(1).forEach { reason ->
                Spacer(Modifier.size(20.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = {
                            if (deleteReason == reason) {
                                onChangeDeleteReason(DeleteReason.NONE)
                            } else {
                                onChangeDeleteReason(reason)
                            }
                        },
                        modifier = Modifier
                            .background(
                                color = if (deleteReason == reason) CurtainCallTheme.colors.primary else Grey8,
                                shape = RoundedCornerShape(6.dp)
                            ).size(20.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_check),
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = White
                        )
                    }
                    Text(
                        text = reason.value,
                        modifier = Modifier.padding(start = 10.dp),
                        style = CurtainCallTheme.typography.body3.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            }
            if (deleteReason == DeleteReason.ETC) {
                BasicTextField(
                    value = content,
                    onValueChange = onChangeContent,
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .fillMaxWidth()
                        .background(
                            color = Grey9,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .heightIn(min = 130.dp)
                        .padding(horizontal = 14.dp, vertical = 12.dp),
                    textStyle = CurtainCallTheme.typography.body3.copy(
                        color = Grey1
                    )
                ) { innerTextField ->
                    Box(modifier = Modifier.fillMaxWidth()) {
                        if (content.isEmpty()) {
                            Text(
                                text = stringResource(R.string.mypage_setting_delete_reason_placeholder),
                                style = CurtainCallTheme.typography.body3.copy(
                                    color = Grey6
                                )
                            )
                        }
                    }
                    innerTextField()
                }
                if (content.length > 500) {
                    Text(
                        text = stringResource(R.string.party_member_show_content_restrict),
                        modifier = Modifier.padding(top = 8.dp, start = 14.dp),
                        style = CurtainCallTheme.typography.body4.copy(
                            color = Red
                        )
                    )
                }
            }
        }
    }
}
