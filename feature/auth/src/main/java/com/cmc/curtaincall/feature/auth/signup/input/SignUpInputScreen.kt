package com.cmc.curtaincall.feature.auth.signup.input

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cmc.curtaincall.common.designsystem.R
import com.cmc.curtaincall.common.designsystem.component.appbars.CurtainCallCenterTopAppBarWithBack
import com.cmc.curtaincall.common.designsystem.component.buttons.common.CurtainCallFilledButton
import com.cmc.curtaincall.common.designsystem.dimension.Paddings
import com.cmc.curtaincall.common.designsystem.theme.Black
import com.cmc.curtaincall.common.designsystem.theme.CurtainCallTheme
import com.cmc.curtaincall.common.designsystem.theme.Grey4
import com.cmc.curtaincall.common.designsystem.theme.Grey5
import com.cmc.curtaincall.common.designsystem.theme.Grey6
import com.cmc.curtaincall.common.designsystem.theme.Grey7
import com.cmc.curtaincall.common.designsystem.theme.Grey9
import com.cmc.curtaincall.common.designsystem.theme.Red
import com.cmc.curtaincall.common.designsystem.theme.White
import io.getstream.chat.android.client.ChatClient
import kotlinx.coroutines.flow.collectLatest

private const val INPUT_CHECK_REGEX = "^[ㄱ-ㅎ가-힣a-zA-Z0-9]{1,15}$"
private const val INPUT_FIRST_CHECK = "^[^\\s]{1,15}$"
private const val INPUT_SECOND_CHECK = "^[가-힣A-Za-z0-9]*\$"

@Composable
internal fun SignUpInputScreen(
    chatClient: ChatClient,
    onNavigateToHome: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            CurtainCallCenterTopAppBarWithBack(
                title = stringResource(R.string.signup_title),
                onBack = onBack
            )
        },
        modifier = Modifier.statusBarsPadding(),
        contentWindowInsets = WindowInsets.statusBars
    ) { paddingValues ->
        SignUpInputContent(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(White),
            chatClient = chatClient,
            onNavigateToHome = onNavigateToHome
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun SignUpInputContent(
    modifier: Modifier = Modifier,
    signUpViewModel: SignUpInputViewModel = hiltViewModel(),
    chatClient: ChatClient,
    onNavigateToHome: () -> Unit = {}
) {
    val validationCheckState by signUpViewModel.validationCheckState.collectAsStateWithLifecycle()
    var userNickname by remember { mutableStateOf("") }
    var borderColor by remember { mutableStateOf(Color.Transparent) }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(signUpViewModel) {
        signUpViewModel.isComplete.collectLatest { isComplete ->
            if (isComplete) onNavigateToHome()
        }
    }

    LaunchedEffect(Unit) {
        signUpViewModel.user.collectLatest { user ->
            signUpViewModel.connectChattingClient(chatClient, user)
        }
    }

    Column(modifier.padding(horizontal = 20.dp)) {
        Text(
            text = stringResource(R.string.signup_input_description),
            modifier = Modifier.padding(top = 30.dp),
            style = CurtainCallTheme.typography.subTitle2
        )
        SignUpCheckableText(
            modifier = Modifier
                .padding(top = 14.dp)
                .fillMaxWidth(),
            checked = Regex(INPUT_FIRST_CHECK).matches(userNickname),
            text = stringResource(R.string.signup_input_nickname_rule1)
        )
        SignUpCheckableText(
            modifier = Modifier
                .padding(top = Paddings.small)
                .fillMaxWidth(),
            checked = Regex(INPUT_SECOND_CHECK).matches(userNickname) && userNickname.isNotEmpty(),
            text = stringResource(R.string.signup_input_nickname_rule2)
        )
        Row(
            modifier = Modifier
                .padding(top = 50.dp)
                .fillMaxWidth()
                .height(44.dp)
        ) {
            BasicTextField(
                value = userNickname,
                onValueChange = {
                    userNickname = it
                    signUpViewModel.clearValidationState()
                },
                modifier = Modifier
                    .width(222.dp)
                    .fillMaxHeight()
                    .background(Grey9, RoundedCornerShape(10.dp))
                    .border(1.dp, if (validationCheckState == ValidationCheckState.Duplicate) Red else borderColor, RoundedCornerShape(10.dp))
                    .onFocusChanged {
                        borderColor = if (it.isFocused) Grey5 else Color.Transparent
                    },
                textStyle = CurtainCallTheme.typography.body3.copy(
                    color = if (validationCheckState == ValidationCheckState.Duplicate) Red else Black
                ),
                singleLine = true,
                maxLines = 1
            ) { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 14.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    innerTextField()
                    if (userNickname.isEmpty()) {
                        Text(
                            text = stringResource(R.string.signup_input_nickname_placeholder),
                            style = CurtainCallTheme.typography.body3.copy(color = Grey6)
                        )
                    }
                }
            }
            Spacer(Modifier.weight(1f))
            CurtainCallFilledButton(
                modifier = Modifier
                    .width(88.dp)
                    .fillMaxHeight(),
                text = stringResource(R.string.signup_input_duplicated_check),
                enabled = Regex(INPUT_CHECK_REGEX).matches(userNickname) && validationCheckState == ValidationCheckState.None,
                textStyle = CurtainCallTheme.typography.body3
            ) {
                signUpViewModel.checkDuplicateNickname(userNickname)
                keyboardController?.hide()
            }
        }
        if (validationCheckState != ValidationCheckState.None) {
            Text(
                text = stringResource(
                    if (validationCheckState == ValidationCheckState.Validate) {
                        R.string.signup_input_nickname_validate
                    } else {
                        R.string.signup_input_nickname_duplicate
                    }
                ),
                modifier = Modifier.padding(top = 8.dp, start = 14.dp),
                style = CurtainCallTheme.typography.body4.copy(
                    color = if (validationCheckState == ValidationCheckState.Validate) {
                        CurtainCallTheme.colors.systemGreen
                    } else {
                        CurtainCallTheme.colors.systemRed
                    }
                )
            )
        }
        Spacer(Modifier.weight(1f))
        CurtainCallFilledButton(
            modifier = Modifier
                .padding(bottom = 30.dp)
                .fillMaxWidth()
                .height(51.dp),
            text = stringResource(R.string.signup_complete),
            enabled = validationCheckState == ValidationCheckState.Validate
        ) {
            signUpViewModel.createMember(userNickname)
        }
    }
}

@Composable
private fun SignUpCheckableText(
    modifier: Modifier = Modifier,
    checked: Boolean = false,
    text: String
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_check),
            contentDescription = text,
            modifier = Modifier.size(14.dp),
            tint = if (checked) {
                CurtainCallTheme.colors.systemGreen
            } else {
                Grey7
            }
        )
        Text(
            text = text,
            modifier = Modifier.padding(start = 6.dp),
            style = CurtainCallTheme.typography.body3.copy(color = Grey4)
        )
    }
}
