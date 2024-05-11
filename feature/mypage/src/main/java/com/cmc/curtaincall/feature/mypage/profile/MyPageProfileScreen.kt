package com.cmc.curtaincall.feature.mypage.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.cmc.curtaincall.common.designsystem.R
import com.cmc.curtaincall.common.designsystem.component.appbars.CurtainCallCenterTopAppBarWithBack
import com.cmc.curtaincall.common.designsystem.component.buttons.common.CurtainCallFilledButton
import com.cmc.curtaincall.common.designsystem.component.custom.ProfileImageEditBottomSheet
import com.cmc.curtaincall.common.designsystem.theme.Black
import com.cmc.curtaincall.common.designsystem.theme.CurtainCallTheme
import com.cmc.curtaincall.common.designsystem.theme.Grey5
import com.cmc.curtaincall.common.designsystem.theme.Grey6
import com.cmc.curtaincall.common.designsystem.theme.Grey9
import com.cmc.curtaincall.common.designsystem.theme.Red
import com.cmc.curtaincall.domain.enums.ProfileEditImageMode
import kotlinx.coroutines.flow.collectLatest

private const val INPUT_CHECK_REGEX = "^[ㄱ-ㅎ가-힣a-zA-Z0-9]{1,15}$"
private const val INPUT_FIRST_CHECK = "^[^\\s]{1,15}$"
private const val INPUT_SECOND_CHECK = "^[가-힣A-Za-z0-9]*\$"

enum class ValidationCheckState {
    None, Validate, Duplicate
}

@Composable
fun MyPageProfileScreen(
    onBack: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            CurtainCallCenterTopAppBarWithBack(
                title = stringResource(R.string.mypage_profile_edit),
                onBack = onBack
            )
        }
    ) { paddingValues ->
        MyPageProfileContent(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(CurtainCallTheme.colors.background)
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun MyPageProfileContent(
    modifier: Modifier = Modifier,
    myPageProfileViewModel: MyPageProfileViewModel = hiltViewModel()
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current
    val validationCheckState by myPageProfileViewModel.validationCheckState.collectAsStateWithLifecycle()
    val memberInfoModel by myPageProfileViewModel.memberInfoModel.collectAsStateWithLifecycle()
    val isDefaultProfile by myPageProfileViewModel.isDefaultProfile.collectAsStateWithLifecycle()

    var profileUri by remember { mutableStateOf<Uri?>(null) }
    val takePhotoFromAlbum = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { uri ->
            profileUri = uri
            myPageProfileViewModel.changeDefaultProfile(false)
        }
    }
    var isShowBottomSheet by remember { mutableStateOf(false) }
    var isNickNameEdit by remember { mutableStateOf(false) }
    var borderColor by remember { mutableStateOf(Color.Transparent) }
    var userNickname by remember(memberInfoModel.nickname) { mutableStateOf(memberInfoModel.nickname) }

    LaunchedEffect(Unit) {
        myPageProfileViewModel.updateEffect.collectLatest { update ->
            if (update) {
                profileUri = null
                userNickname = memberInfoModel.nickname
                isNickNameEdit = false
                myPageProfileViewModel.changeDefaultProfile(false)
                myPageProfileViewModel.clearValidationState()
                myPageProfileViewModel.requestMemberInfo()
            }
        }
    }

    if (isShowBottomSheet) {
        ProfileImageEditBottomSheet(
            onSelectEditMode = { mode ->
                when (mode) {
                    ProfileEditImageMode.ALBUM -> {
                        takePhotoFromAlbum.launch("image/*")
                    }

                    ProfileEditImageMode.DEFAULT -> {
                        myPageProfileViewModel.changeDefaultProfile(true)
                    }

                    else -> Unit
                }
                isShowBottomSheet = false
            },
            onDismissRequest = {
                isShowBottomSheet = false
            }
        )
    }
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = if (isDefaultProfile) {
                null
            } else {
                profileUri ?: memberInfoModel.imageUrl
            },
            contentDescription = null,
            modifier = Modifier
                .padding(top = 60.dp)
                .size(80.dp)
                .clip(CircleShape)
                .clickable { isShowBottomSheet = true },
            error = painterResource(R.drawable.ic_default_profile),
            placeholder = painterResource(R.drawable.ic_default_profile),
            contentScale = ContentScale.FillBounds
        )
        if (isNickNameEdit) {
            Row(
                modifier = Modifier
                    .padding(top = 40.dp)
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth()
                    .height(44.dp)
            ) {
                BasicTextField(
                    value = userNickname,
                    onValueChange = {
                        userNickname = it
                        myPageProfileViewModel.clearValidationState()
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
                    myPageProfileViewModel.checkDuplicateNickname(userNickname)
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
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(top = 8.dp, start = 34.dp),
                    style = CurtainCallTheme.typography.body4.copy(
                        color = if (validationCheckState == ValidationCheckState.Validate) {
                            CurtainCallTheme.colors.systemGreen
                        } else {
                            CurtainCallTheme.colors.systemRed
                        }
                    )
                )
            }
        } else {
            Row(
                modifier = Modifier
                    .padding(start = 10.dp)
                    .padding(top = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = String.format("%s님", memberInfoModel.nickname),
                    style = CurtainCallTheme.typography.subTitle2
                )
                Icon(
                    painter = painterResource(R.drawable.ic_edit),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start = 6.dp)
                        .size(18.dp)
                        .clickable { isNickNameEdit = true },
                    tint = Color.Unspecified
                )
            }
        }
        Spacer(Modifier.weight(1f))
        CurtainCallFilledButton(
            text = stringResource(R.string.mypage_profile_edit_complete),
            modifier = Modifier
                .padding(bottom = 30.dp)
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
                .height(51.dp),
            enabled = profileUri != null || isDefaultProfile || validationCheckState == ValidationCheckState.Validate,
            onClick = {
                myPageProfileViewModel.updateMember(
                    context = context,
                    profileUri = profileUri,
                    nickname = userNickname
                )
            }
        )
    }
}
