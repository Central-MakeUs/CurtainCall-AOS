package com.cmc.curtaincall.feature.home.report

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cmc.curtaincall.common.designsystem.R
import com.cmc.curtaincall.common.designsystem.component.appbars.CurtainCallCloseTopAppBar
import com.cmc.curtaincall.common.designsystem.component.basic.CurtainCallSnackbar
import com.cmc.curtaincall.common.designsystem.component.basic.CurtainCallSnackbarHost
import com.cmc.curtaincall.common.designsystem.component.basic.SystemUiStatusBar
import com.cmc.curtaincall.common.designsystem.component.buttons.common.CurtainCallFilledButton
import com.cmc.curtaincall.common.designsystem.theme.CurtainCallTheme
import com.cmc.curtaincall.common.designsystem.theme.Grey1
import com.cmc.curtaincall.common.designsystem.theme.Grey2
import com.cmc.curtaincall.common.designsystem.theme.Grey3
import com.cmc.curtaincall.common.designsystem.theme.Grey6
import com.cmc.curtaincall.common.designsystem.theme.Grey8
import com.cmc.curtaincall.common.designsystem.theme.Grey9
import com.cmc.curtaincall.common.designsystem.theme.Red
import com.cmc.curtaincall.common.designsystem.theme.White
import com.cmc.curtaincall.domain.type.ReportType
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun HomeReportScreen(
    homeReportViewModel: HomeReportViewModel = hiltViewModel(),
    reportId: Int?,
    reportType: ReportType?,
    onNavigateHome: () -> Unit,
    onBack: () -> Unit
) {
    requireNotNull(reportId)
    requireNotNull(reportType)

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var snackbarPainter by remember { mutableIntStateOf(R.drawable.ic_complete_green) }
    var isShowFAB by remember { mutableStateOf(true) }
    val homeReportUiState by homeReportViewModel.uiState.collectAsStateWithLifecycle()
    SystemUiStatusBar(White)
    Scaffold(
        topBar = {
            CurtainCallCloseTopAppBar(
                title = stringResource(R.string.report),
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
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            if (isShowFAB) {
                CurtainCallFilledButton(
                    modifier = Modifier
                        .padding(bottom = 14.dp)
                        .padding(horizontal = 20.dp)
                        .fillMaxWidth()
                        .height(51.dp),
                    text = if (homeReportUiState.phrase == 1) {
                        stringResource(R.string.next)
                    } else {
                        stringResource(R.string.report)
                    },
                    enabled = if (homeReportUiState.phrase == 1) {
                        homeReportUiState.reportReason != HomeReportReason.NONE
                    } else {
                        homeReportUiState.content.isNotEmpty()
                    },
                    onClick = {
                        if (homeReportUiState.phrase == 1) {
                            homeReportViewModel.movePhrase(homeReportUiState.phrase + 1)
                        } else {
                            homeReportViewModel.requestReport(
                                idToReport = reportId,
                                type = reportType
                            )
                        }
                    }
                )
            }
        }
    ) { paddingValues ->
        HomeReportContent(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(CurtainCallTheme.colors.background)
        )
    }

    LaunchedEffect(homeReportViewModel) {
        homeReportViewModel.reportSuccess.collectLatest { isSuccess ->
            isShowFAB = false
            snackbarPainter = if (isSuccess) {
                R.drawable.ic_complete_green
            } else {
                R.drawable.ic_error_snackbar
            }

            coroutineScope.launch {
                snackbarHostState.showSnackbar(
                    context.getString(
                        if (isSuccess) {
                            R.string.home_report_success
                        } else {
                            R.string.home_report_failure
                        }
                    )
                )
            }
            delay(2000)
            onBack()
        }
    }
}

@Composable
private fun HomeReportContent(
    modifier: Modifier = Modifier,
    homeReportViewModel: HomeReportViewModel = hiltViewModel()
) {
    val homeReportUiState by homeReportViewModel.uiState.collectAsStateWithLifecycle()
    val reportReason = homeReportUiState.reportReason

    Column(modifier) {
        if (homeReportUiState.phrase == 1) {
            Text(
                text = stringResource(R.string.home_report_first_phrase_title),
                modifier = Modifier
                    .padding(top = 30.dp)
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth(),
                style = CurtainCallTheme.typography.subTitle2
            )
            Spacer(Modifier.size(50.dp))
            HomeReportReason.values().dropLast(1).forEach { reason ->
                Row(
                    modifier = Modifier
                        .padding(bottom = 20.dp)
                        .padding(horizontal = 20.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            if (reportReason == reason) {
                                homeReportViewModel.changeReportReason(HomeReportReason.NONE)
                            } else {
                                homeReportViewModel.changeReportReason(reason)
                            }
                        },
                        modifier = Modifier
                            .background(
                                color = if (reportReason == reason) CurtainCallTheme.colors.primary else Grey8,
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
        } else {
            Column(
                modifier = Modifier
                    .padding(top = 30.dp)
                    .padding(horizontal = 20.dp)
                    .fillMaxSize()
            ) {
                Text(
                    text = stringResource(R.string.home_report_second_phrase_title),
                    style = CurtainCallTheme.typography.subTitle2
                )
                BasicTextField(
                    value = homeReportUiState.content,
                    onValueChange = { homeReportViewModel.setContent(it) },
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
                        if (homeReportUiState.content.isEmpty()) {
                            Text(
                                text = stringResource(R.string.home_report_content_placeholder),
                                style = CurtainCallTheme.typography.body3.copy(
                                    color = Grey6
                                )
                            )
                        }
                    }
                    innerTextField()
                }
                if (homeReportUiState.content.length > 400) {
                    Text(
                        text = stringResource(R.string.home_report_content_restrict),
                        modifier = Modifier.padding(top = 8.dp, start = 14.dp),
                        style = CurtainCallTheme.typography.body4.copy(
                            color = Red
                        )
                    )
                }
                Text(
                    text = stringResource(R.string.home_report_rule_title),
                    modifier = Modifier.padding(top = 40.dp),
                    style = CurtainCallTheme.typography.body2.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = Grey2
                    )
                )
                Row(
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "*",
                        style = CurtainCallTheme.typography.body4.copy(
                            color = Grey3
                        )
                    )
                    Text(
                        text = stringResource(R.string.home_report_rule1),
                        modifier = Modifier.padding(start = 6.dp),
                        style = CurtainCallTheme.typography.body4.copy(
                            color = Grey3
                        )
                    )
                }
                Row(
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "*",
                        style = CurtainCallTheme.typography.body4.copy(
                            color = Grey3
                        )
                    )
                    Text(
                        text = stringResource(R.string.home_report_rule2),
                        modifier = Modifier.padding(start = 6.dp),
                        style = CurtainCallTheme.typography.body4.copy(
                            color = Grey3
                        )
                    )
                }
                Row(
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "*",
                        style = CurtainCallTheme.typography.body4.copy(
                            color = Grey3
                        )
                    )
                    Text(
                        text = stringResource(R.string.home_report_rule3),
                        modifier = Modifier.padding(start = 6.dp),
                        style = CurtainCallTheme.typography.body4.copy(
                            color = Grey3
                        )
                    )
                }
            }
        }
    }
}
