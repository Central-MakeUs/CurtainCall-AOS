package com.cmc.curtaincall.feature.mypage.setting

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cmc.curtaincall.common.designsystem.R
import com.cmc.curtaincall.common.designsystem.component.appbars.CurtainCallCenterTopAppBarWithBack
import com.cmc.curtaincall.common.designsystem.component.basic.SystemUiStatusBar
import com.cmc.curtaincall.common.designsystem.component.dialogs.CurtainCallSelectDialog
import com.cmc.curtaincall.common.designsystem.theme.CurtainCallTheme
import com.cmc.curtaincall.common.designsystem.theme.Grey5
import com.cmc.curtaincall.common.designsystem.theme.Grey9
import com.cmc.curtaincall.common.designsystem.theme.White
import com.cmc.curtaincall.domain.Urls.PRIVACY_INFORMATION_TERMS_URL
import com.cmc.curtaincall.domain.Urls.SERVICE_TERMS_URL
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState
import kotlinx.coroutines.flow.collectLatest

@Composable
internal fun MyPageSettingScreen(
    onLogout: () -> Unit,
    onNavigateDeleteMember: () -> Unit,
    onBack: () -> Unit
) {
    var webViewUrl by remember { mutableStateOf("") }
    val webViewState = rememberWebViewState(url = webViewUrl)

    SystemUiStatusBar(White)
    Scaffold(
        topBar = {
            CurtainCallCenterTopAppBarWithBack(
                title = stringResource(R.string.mypage_profile_setting),
                onBack = {
                    if (webViewUrl.isNotEmpty()) {
                        webViewUrl = ""
                    } else {
                        onBack()
                    }
                }
            )
        }
    ) { paddingValues ->
        if (webViewUrl.isEmpty()) {
            MyPageSettingContent(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .background(White),
                onLogout = onLogout,
                onNavigateDeleteMember = onNavigateDeleteMember,
                onClickTerms = { webViewUrl = it }
            )
        } else {
            WebView(
                state = webViewState,
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 30.dp)
                    .fillMaxSize()
            )
        }
    }

    BackHandler {
        if (webViewUrl.isNotEmpty()) {
            webViewUrl = ""
        } else {
            onBack()
        }
    }
}

@Composable
private fun MyPageSettingContent(
    modifier: Modifier = Modifier,
    myPageSettingViewModel: MyPageSettingViewModel = hiltViewModel(),
    onLogout: () -> Unit = {},
    onNavigateDeleteMember: () -> Unit = {},
    onClickTerms: (String) -> Unit = {}
) {
    var isShowLogoutDialog by remember { mutableStateOf(false) }
    if (isShowLogoutDialog) {
        CurtainCallSelectDialog(
            title = stringResource(R.string.mypage_setting_logout_dialog),
            cancelButtonText = stringResource(R.string.dismiss),
            actionButtonText = stringResource(R.string.mypage_setting_logout),
            onDismiss = { isShowLogoutDialog = false },
            onCancel = { isShowLogoutDialog = false },
            onAction = { myPageSettingViewModel.memberLogout() }
        )
    }

    LaunchedEffect(myPageSettingViewModel) {
        myPageSettingViewModel.logoutComplete.collectLatest { check ->
            if (check) {
                onLogout()
            }
        }
    }

    Column(modifier) {
        Text(
            text = stringResource(R.string.mypage_profile_setting),
            modifier = Modifier.run { padding(top = 20.dp, start = 20.dp) },
            style = CurtainCallTheme.typography.subTitle4
        )
        Row(
            modifier = Modifier
                .padding(top = 15.dp)
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
                .clickable { isShowLogoutDialog = true }
                .padding(vertical = 15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.mypage_setting_logout),
                style = CurtainCallTheme.typography.body2
            )
        }
        Row(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
                .clickable {
                    // TODO 계정 삭제
                }
                .padding(vertical = 15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.mypage_setting_account_delete),
                style = CurtainCallTheme.typography.body2
            )
        }
        Spacer(
            modifier = Modifier
                .padding(top = 25.dp)
                .fillMaxWidth()
                .height(10.dp)
                .background(Grey9)
        )
        Text(
            text = stringResource(R.string.mypage_setting_information),
            modifier = Modifier.run { padding(top = 20.dp, start = 20.dp) },
            style = CurtainCallTheme.typography.subTitle4
        )
        Row(
            modifier = Modifier
                .padding(top = 15.dp)
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
                .padding(vertical = 15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.mypage_setting_privacy_information_terms),
                modifier = Modifier.weight(1f),
                style = CurtainCallTheme.typography.body2
            )
            Text(
                text = stringResource(R.string.mypage_setting_terms_view),
                modifier = Modifier.clickable { onClickTerms(PRIVACY_INFORMATION_TERMS_URL) },
                style = CurtainCallTheme.typography.body4.copy(
                    color = Grey5
                )
            )
        }
        Row(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
                .padding(vertical = 15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.mypage_setting_service_use_terms),
                modifier = Modifier.weight(1f),
                style = CurtainCallTheme.typography.body2
            )
            Text(
                text = stringResource(R.string.mypage_setting_terms_view),
                modifier = Modifier.clickable { onClickTerms(SERVICE_TERMS_URL) },
                style = CurtainCallTheme.typography.body4.copy(
                    color = Grey5
                )
            )
        }
    }
}
