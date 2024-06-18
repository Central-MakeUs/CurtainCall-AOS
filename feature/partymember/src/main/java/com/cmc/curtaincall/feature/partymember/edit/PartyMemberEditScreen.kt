package com.cmc.curtaincall.feature.partymember.edit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cmc.curtaincall.common.designsystem.R
import com.cmc.curtaincall.common.designsystem.component.appbars.CurtainCallCloseTopAppBar
import com.cmc.curtaincall.common.designsystem.component.basic.CurtainCallSnackbar
import com.cmc.curtaincall.common.designsystem.component.basic.CurtainCallSnackbarHost
import com.cmc.curtaincall.common.designsystem.component.basic.SystemUiStatusBar
import com.cmc.curtaincall.common.designsystem.component.buttons.common.CurtainCallFilledButton
import com.cmc.curtaincall.common.designsystem.theme.Black
import com.cmc.curtaincall.common.designsystem.theme.CurtainCallTheme
import com.cmc.curtaincall.common.designsystem.theme.Grey1
import com.cmc.curtaincall.common.designsystem.theme.Grey6
import com.cmc.curtaincall.common.designsystem.theme.Grey9
import com.cmc.curtaincall.common.designsystem.theme.Red
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun PartyMemberEditScreen(
    partyMemberEditViewModel: PartyMemberEditViewModel = hiltViewModel(),
    partyId: Int?,
    showName: String?,
    onBack: () -> Unit = {}
) {
    checkNotNull(partyId)
    checkNotNull(showName)

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var snackbarPainter by remember { mutableIntStateOf(R.drawable.ic_complete_green) }
    var isShowFAB by remember { mutableStateOf(true) }

    SystemUiStatusBar(CurtainCallTheme.colors.background)
    Scaffold(
        topBar = {
            CurtainCallCloseTopAppBar(
                title = showName,
                containerColor = CurtainCallTheme.colors.background,
                contentColor = Black,
                onClose = onBack
            )
        },
        snackbarHost = {
            CurtainCallSnackbarHost(snackbarHostState = snackbarHostState) { snackbarData ->
                CurtainCallSnackbar(
                    modifier = Modifier.fillMaxWidth(),
                    snackbarData = snackbarData,
                    painter = painterResource(snackbarPainter)
                )
            }
        },
        floatingActionButton = {
            if (isShowFAB) {
                CurtainCallFilledButton(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .fillMaxWidth()
                        .height(51.dp),
                    text = stringResource(R.string.edit_complete),
                    containerColor = CurtainCallTheme.colors.primary,
                    contentColor = CurtainCallTheme.colors.onPrimary,
                    onClick = {
                        partyMemberEditViewModel.editParty(partyId)
                    }
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { paddingValues ->
        PartyMemberEditContent(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(CurtainCallTheme.colors.background)
        )
    }

    LaunchedEffect(Unit) {
        partyMemberEditViewModel.isSuccessUpdate.collectLatest { isSuccessUpdate ->
            isShowFAB = false
            if (isSuccessUpdate) {
                snackbarPainter = R.drawable.ic_complete_green
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(
                        context.getString(R.string.party_member_edit_success)
                    )
                }
                delay(2000)
                onBack()
            } else {
                snackbarPainter = R.drawable.ic_error_snackbar
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(
                        context.getString(R.string.party_member_edit_failure)
                    )
                }
                delay(2000)
                onBack()
            }
        }
    }
}

@Composable
private fun PartyMemberEditContent(
    modifier: Modifier = Modifier,
    partyMemberEditViewModel: PartyMemberEditViewModel = hiltViewModel()
) {
    val title by partyMemberEditViewModel.title.collectAsStateWithLifecycle()
    val content by partyMemberEditViewModel.content.collectAsStateWithLifecycle()
    Column(modifier) {
        Text(
            text = stringResource(R.string.party_member_show_title_guide),
            modifier = Modifier.padding(start = 20.dp, top = 20.dp),
            style = CurtainCallTheme.typography.subTitle4
        )
        BasicTextField(
            value = title,
            onValueChange = { partyMemberEditViewModel.changeTitle(it) },
            modifier = Modifier
                .padding(top = 12.dp)
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
                .background(
                    color = Grey9,
                    shape = RoundedCornerShape(10.dp)
                )
                .height(44.dp),
            textStyle = CurtainCallTheme.typography.body3.copy(
                color = Grey1
            )
        ) { innerTextField ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 14.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                if (title.isEmpty()) {
                    Text(
                        text = stringResource(R.string.party_member_show_title_placeholder),
                        style = CurtainCallTheme.typography.body3.copy(
                            color = Grey6
                        ),
                        maxLines = 1
                    )
                }
                innerTextField()
            }
        }
        Text(
            text = stringResource(R.string.party_member_show_content_guide),
            modifier = Modifier.padding(top = 40.dp, start = 20.dp),
            style = CurtainCallTheme.typography.subTitle4
        )
        BasicTextField(
            value = content,
            onValueChange = { partyMemberEditViewModel.changeContent(it) },
            modifier = Modifier
                .padding(top = 12.dp)
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
                .background(
                    color = Grey9,
                    shape = RoundedCornerShape(10.dp)
                )
                .heightIn(min = 130.dp),
            textStyle = CurtainCallTheme.typography.body3.copy(
                color = Grey1
            )
        ) { innerTextField ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 12.dp)
            ) {
                if (content.isEmpty()) {
                    Text(
                        text = stringResource(R.string.party_member_show_content_placeholder),
                        style = CurtainCallTheme.typography.body3.copy(
                            color = Grey6
                        )
                    )
                }
                innerTextField()
            }
        }
        if (content.length > 500) {
            Text(
                text = stringResource(R.string.party_member_show_content_restrict),
                modifier = Modifier.padding(top = 8.dp, start = 34.dp),
                style = CurtainCallTheme.typography.body4.copy(
                    color = Red
                )
            )
        }
    }
}
