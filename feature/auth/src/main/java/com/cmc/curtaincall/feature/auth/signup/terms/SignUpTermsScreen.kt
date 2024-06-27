package com.cmc.curtaincall.feature.auth.signup.terms

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.cmc.curtaincall.common.designsystem.R
import com.cmc.curtaincall.common.designsystem.component.appbars.CurtainCallCenterTopAppBarWithBack
import com.cmc.curtaincall.common.designsystem.component.basic.SystemUiStatusBar
import com.cmc.curtaincall.common.designsystem.component.buttons.common.CurtainCallFilledButton
import com.cmc.curtaincall.common.designsystem.component.divider.HorizontalDivider
import com.cmc.curtaincall.common.designsystem.theme.CurtainCallTheme
import com.cmc.curtaincall.common.designsystem.theme.Grey4
import com.cmc.curtaincall.common.designsystem.theme.Grey5
import com.cmc.curtaincall.common.designsystem.theme.Grey6
import com.cmc.curtaincall.common.designsystem.theme.Grey8
import com.cmc.curtaincall.common.designsystem.theme.White
import com.cmc.curtaincall.domain.Urls.DEFAULT_URL
import com.cmc.curtaincall.domain.Urls.PRIVACY_INFORMATION_TERMS_URL
import com.cmc.curtaincall.domain.Urls.SERVICE_TERMS_URL
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState

@Composable
internal fun SignUpTermsScreen(
    onNavigateToSignUpInput: () -> Unit = {},
    onBack: () -> Unit
) {
    val signUpTermsUiState = rememberSignUpTermsState()
    SystemUiStatusBar(White)
    Scaffold(
        topBar = {
            CurtainCallCenterTopAppBarWithBack(
                title = stringResource(R.string.signup_terms_title),
                onBack = {
                    if (signUpTermsUiState.webUrl == DEFAULT_URL) {
                        onBack()
                    } else {
                        signUpTermsUiState.webUrl = DEFAULT_URL
                    }
                }
            )
        },
        modifier = Modifier.statusBarsPadding(),
        contentWindowInsets = WindowInsets.statusBars
    ) { paddingValues ->
        SignUpTermsContent(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(White),
            signUpTermsState = signUpTermsUiState,
            onStateChange = {
                signUpTermsUiState.webUrl = it.webUrl
                signUpTermsUiState.serviceUseTerms = it.serviceUseTerms
                signUpTermsUiState.privacyInfoTerms = it.privacyInfoTerms
                signUpTermsUiState.userAgreeTerms = it.userAgreeTerms
            },
            onNavigateToSignUpInput = onNavigateToSignUpInput,
            onNavigateToSignUpTermsWebView = {
                signUpTermsUiState.webUrl = it
            }
        )
    }

    BackHandler {
        if (signUpTermsUiState.webUrl == DEFAULT_URL) {
            onBack()
        } else {
            signUpTermsUiState.webUrl = DEFAULT_URL
        }
    }
}

@Composable
private fun SignUpTermsContent(
    modifier: Modifier = Modifier,
    signUpTermsState: SignUpTermsState,
    onStateChange: (SignUpTermsState) -> Unit = {},
    onNavigateToSignUpInput: () -> Unit = {},
    onNavigateToSignUpTermsWebView: (String) -> Unit = {}
) {
    val webViewState = rememberWebViewState(url = signUpTermsState.webUrl)
    Box(modifier) {
        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxSize()
        ) {
            Text(
                text = stringResource(R.string.signup_terms_description),
                modifier = Modifier.padding(top = 40.dp),
                style = CurtainCallTheme.typography.subTitle2
            )
            Text(
                text = stringResource(R.string.signup_terms_sub_description),
                modifier = Modifier.padding(top = 14.dp),
                style = CurtainCallTheme.typography.body3.copy(color = Grey4)
            )
            SignUpTermsCheckBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp),
                text = stringResource(R.string.signup_terms_all_agree),
                isChecked = signUpTermsState.isQualified(),
                onCheckedChange = {
                    onStateChange(
                        signUpTermsState.apply {
                            serviceUseTerms = it
                            privacyInfoTerms = it
                            userAgreeTerms = it
                        }
                    )
                }
            )
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 18.dp),
                background = Grey8
            )
            SignUpTermsCheckBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 18.dp),
                text = stringResource(R.string.signup_terms_service_use_agree),
                isChecked = signUpTermsState.serviceUseTerms,
                hasMore = true,
                onCheckedChange = { onStateChange(signUpTermsState.apply { serviceUseTerms = it }) },
                onNavigateToSignUpTermsWebView = { onNavigateToSignUpTermsWebView(SERVICE_TERMS_URL) }
            )
            SignUpTermsCheckBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 18.dp),
                text = stringResource(R.string.signup_terms_privacy_information_agree),
                isChecked = signUpTermsState.privacyInfoTerms,
                hasMore = true,
                onCheckedChange = { onStateChange(signUpTermsState.apply { privacyInfoTerms = it }) },
                onNavigateToSignUpTermsWebView = { onNavigateToSignUpTermsWebView(PRIVACY_INFORMATION_TERMS_URL) }
            )
            SignUpTermsCheckBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 18.dp),
                text = stringResource(R.string.signup_terms_user_agree),
                isChecked = signUpTermsState.userAgreeTerms,
                onCheckedChange = { onStateChange(signUpTermsState.apply { userAgreeTerms = it }) }
            )
            Spacer(Modifier.weight(1f))
            CurtainCallFilledButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 30.dp)
                    .height(51.dp),
                text = stringResource(R.string.next),
                containerColor = if (signUpTermsState.isQualified()) CurtainCallTheme.colors.primary else Grey8,
                contentColor = if (signUpTermsState.isQualified()) CurtainCallTheme.colors.onPrimary else Grey6,
                onClick = onNavigateToSignUpInput
            )
        }
        if (signUpTermsState.webUrl != DEFAULT_URL) {
            WebView(
                state = webViewState,
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .fillMaxSize(),
                onCreated = {
                    it.settings.javaScriptEnabled = true
                    it.settings.domStorageEnabled = true
                }
            )
        }
    }
}

@Composable
private fun SignUpTermsCheckBox(
    modifier: Modifier = Modifier,
    text: String,
    isChecked: Boolean = false,
    hasMore: Boolean = false,
    onCheckedChange: (Boolean) -> Unit = {},
    onNavigateToSignUpTermsWebView: () -> Unit = {}
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = { onCheckedChange(!isChecked) },
            modifier = Modifier
                .background(
                    color = if (isChecked) CurtainCallTheme.colors.primary else Grey8,
                    shape = RoundedCornerShape(6.dp)
                ).size(20.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_check),
                contentDescription = text,
                modifier = Modifier.size(14.dp),
                tint = White
            )
        }
        Text(
            text = text,
            modifier = Modifier.padding(start = 10.dp),
            style = CurtainCallTheme.typography.body3.copy(
                fontWeight = FontWeight.SemiBold
            )
        )
        Spacer(Modifier.weight(1f))
        if (hasMore) {
            Text(
                text = stringResource(R.string.view),
                modifier = Modifier.clickable { onNavigateToSignUpTermsWebView() },
                style = CurtainCallTheme.typography.body5.copy(color = Grey5)
            )
        }
    }
}
