package com.cmc.curtaincall.feature.partymember.detail

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FabPosition
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.cmc.curtaincall.common.designsystem.R
import com.cmc.curtaincall.common.designsystem.component.appbars.CurtainCallCenterTopAppBarWithBack
import com.cmc.curtaincall.common.designsystem.component.appbars.CurtainCallCenterTopAppBarWithBackAndMore
import com.cmc.curtaincall.common.designsystem.component.basic.DottedLine
import com.cmc.curtaincall.common.designsystem.component.basic.SystemUiStatusBar
import com.cmc.curtaincall.common.designsystem.component.buttons.common.CurtainCallFilledButton
import com.cmc.curtaincall.common.designsystem.component.dialogs.CurtainCallSelectDialog
import com.cmc.curtaincall.common.designsystem.component.divider.HorizontalDivider
import com.cmc.curtaincall.common.designsystem.theme.CurtainCallTheme
import com.cmc.curtaincall.common.designsystem.theme.Grey1
import com.cmc.curtaincall.common.designsystem.theme.Grey4
import com.cmc.curtaincall.common.designsystem.theme.Grey5
import com.cmc.curtaincall.common.designsystem.theme.Grey6
import com.cmc.curtaincall.common.designsystem.theme.Grey7
import com.cmc.curtaincall.common.designsystem.theme.Grey8
import com.cmc.curtaincall.common.designsystem.theme.Primary
import com.cmc.curtaincall.common.utility.extensions.convertUIDate
import com.cmc.curtaincall.domain.type.ReportType
import kotlinx.coroutines.flow.collectLatest
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun PartyMemberDetailScreen(
    partyMemberDetailViewModel: PartyMemberDetailViewModel = hiltViewModel(),
    partyId: Int?,
    showName: String?,
    onNavigateToEdit: (Int?, String?) -> Unit = { _, _ -> },
    onNavigateToReport: (Int, ReportType) -> Unit = { _, _ -> },
    onBack: () -> Unit = {}
) {
    checkNotNull(partyId)
    checkNotNull(showName)

    val partyDetailModel by partyMemberDetailViewModel.partyDetailModel.collectAsStateWithLifecycle()
    val isParticipated by partyMemberDetailViewModel.isParticipated.collectAsStateWithLifecycle()
    val isMyWriting by partyMemberDetailViewModel.isMyWriting.collectAsStateWithLifecycle()
    var showParticipatePopup by remember { mutableStateOf(false) }
    var showCancelPopup by remember { mutableStateOf(false) }
    var showEditMenu by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        partyMemberDetailViewModel.requestPartyDetail(partyId)
    }

    if (showParticipatePopup) {
        CurtainCallSelectDialog(
            title = stringResource(R.string.party_member_participate_popup_title),
            description = stringResource(R.string.party_member_participate_popup_description),
            cancelButtonText = stringResource(R.string.dismiss),
            actionButtonText = stringResource(R.string.party_member_participate_popup_positive),
            onAction = {
                partyMemberDetailViewModel.participateParty(partyId)
                showParticipatePopup = false
            },
            onCancel = { showParticipatePopup = false },
            onDismiss = { showParticipatePopup = false }
        )
    }

    if (showCancelPopup) {
        CurtainCallSelectDialog(
            title = stringResource(R.string.party_member_cancel_participate_popup_title),
            description = stringResource(R.string.party_member_cancel_participate_popup_description),
            cancelButtonText = stringResource(R.string.dismiss),
            actionButtonText = stringResource(R.string.party_member_cancel_participate_popup_positive),
            onAction = {
                partyMemberDetailViewModel.cancelParty(partyId)
                showCancelPopup = false
            },
            onCancel = { showCancelPopup = false },
            onDismiss = { showCancelPopup = false }
        )
    }

    SystemUiStatusBar(CurtainCallTheme.colors.primary)
    Scaffold(
        topBar = {
            if (isMyWriting) {
                CurtainCallCenterTopAppBarWithBackAndMore(
                    title = showName,
                    containerColor = CurtainCallTheme.colors.primary,
                    contentColor = CurtainCallTheme.colors.onPrimary,
                    onMore = { showEditMenu = showEditMenu.not() },
                    onBack = onBack
                )
            } else {
                CurtainCallCenterTopAppBarWithBack(
                    title = showName,
                    containerColor = CurtainCallTheme.colors.primary,
                    contentColor = CurtainCallTheme.colors.onPrimary,
                    onBack = onBack
                )
            }
        },
        floatingActionButton = {
            if (isMyWriting) {
                CurtainCallFilledButton(
                    text = stringResource(R.string.enter_livetalk),
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .padding(bottom = 14.dp)
                        .fillMaxWidth()
                        .height(51.dp),
                    containerColor = CurtainCallTheme.colors.secondary,
                    contentColor = CurtainCallTheme.colors.primary,
                    onClick = {
                        // TODO 라이브톡 진입
                    }
                )
            } else {
                if (isParticipated) {
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .padding(bottom = 14.dp)
                            .fillMaxWidth()
                            .height(51.dp)
                    ) {
                        CurtainCallFilledButton(
                            text = stringResource(R.string.cancel_participate),
                            modifier = Modifier
                                .weight(104f)
                                .fillMaxHeight(),
                            containerColor = Grey8,
                            contentColor = Grey4,
                            onClick = { showCancelPopup = true }
                        )
                        CurtainCallFilledButton(
                            text = stringResource(R.string.enter_livetalk),
                            modifier = Modifier
                                .padding(start = 10.dp)
                                .weight(206f)
                                .fillMaxHeight(),
                            containerColor = CurtainCallTheme.colors.secondary,
                            contentColor = CurtainCallTheme.colors.primary,
                            onClick = {
                                // TODO 라이브톡 진입
                            }
                        )
                    }
                } else {
                    if (partyDetailModel.showAt.isNotEmpty()) {
                        val showAt = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.KOREA).parse(partyDetailModel.showAt)
                        if (showAt != null) {
                            CurtainCallFilledButton(
                                text = stringResource(
                                    if (partyDetailModel.curMemberNum == partyDetailModel.maxMemberNum) {
                                        R.string.party_member_full_number_of_member
                                    } else if (showAt <= Date()) {
                                        R.string.end_recruitment
                                    } else {
                                        R.string.participate
                                    }
                                ),
                                modifier = Modifier
                                    .padding(horizontal = 20.dp)
                                    .padding(bottom = 14.dp)
                                    .fillMaxWidth()
                                    .height(51.dp),
                                enabled = (partyDetailModel.curMemberNum == partyDetailModel.maxMemberNum).not() && (showAt <= Date()).not(),
                                containerColor = CurtainCallTheme.colors.secondary,
                                contentColor = CurtainCallTheme.colors.primary,
                                onClick = { showParticipatePopup = true }
                            )
                        }
                    }
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { paddingValues ->
        PartyMemberDetailContent(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(CurtainCallTheme.colors.primary),
            showEditMenu = showEditMenu,
            onCloseShowEditMenu = { showEditMenu = false },
            onNavigateToEdit = onNavigateToEdit,
            onNavigateToReport = onNavigateToReport
        )
    }

    LaunchedEffect(Unit) {
        partyMemberDetailViewModel.isSuccessDelete.collectLatest { isSuccessDelete ->
            if (isSuccessDelete) {
                onBack()
            }
        }
    }
}

@Composable
private fun PartyMemberDetailContent(
    modifier: Modifier = Modifier,
    partyMemberDetailViewModel: PartyMemberDetailViewModel = hiltViewModel(),
    showEditMenu: Boolean = false,
    onCloseShowEditMenu: () -> Unit = {},
    onNavigateToEdit: (Int?, String?) -> Unit = { _, _ -> },
    onNavigateToReport: (Int, ReportType) -> Unit = { _, _ -> },
) {
    val scrollState = rememberScrollState()
    val partyDetailModel by partyMemberDetailViewModel.partyDetailModel.collectAsStateWithLifecycle()
    val isMyWriting by partyMemberDetailViewModel.isMyWriting.collectAsStateWithLifecycle()
    var showDeletePopup by remember { mutableStateOf(false) }

    if (showDeletePopup) {
        CurtainCallSelectDialog(
            title = stringResource(R.string.party_member_delete_popup_title),
            cancelButtonText = stringResource(R.string.dismiss),
            actionButtonText = stringResource(R.string.delete_popup),
            onAction = {
                onCloseShowEditMenu()
                partyMemberDetailViewModel.deleteParty(partyDetailModel.id)
            },
            onCancel = { showDeletePopup = false },
            onDismiss = { showDeletePopup = false }
        )
    }

    Box(modifier = modifier.verticalScroll(scrollState)) {
        if (showEditMenu) {
            Card(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(end = 20.dp)
                    .size(73.dp, 88.dp)
                    .zIndex(1f),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = CurtainCallTheme.colors.background
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 10.dp
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .clickable {
                            onCloseShowEditMenu()
                            onNavigateToEdit(partyDetailModel.id, partyDetailModel.showName)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.edit),
                        style = CurtainCallTheme.typography.body3.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = Grey1
                        )
                    )
                }
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    background = Grey8
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .clickable { showDeletePopup = true },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.delete),
                        style = CurtainCallTheme.typography.body3.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = Grey1
                        )
                    )
                }
            }
        }
        Column(
            modifier = Modifier
                .padding(top = 20.dp, bottom = 121.dp)
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
                .background(CurtainCallTheme.colors.background, RoundedCornerShape(20.dp))
        ) {
            AsyncImage(
                model = partyDetailModel.showPoster,
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 40.dp)
                    .size(180.dp, 257.dp)
                    .clip(RoundedCornerShape(18.dp)),
                contentScale = ContentScale.FillBounds,
                error = painterResource(R.drawable.ic_error_poster),
                placeholder = painterResource(R.drawable.ic_error_poster)
            )
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 30.dp)
                    .fillMaxWidth()
            ) {
                AsyncImage(
                    model = partyDetailModel.creatorImageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape),
                    error = painterResource(R.drawable.ic_default_profile),
                    placeholder = painterResource(R.drawable.ic_error_poster)
                )
                Column(Modifier.padding(start = 12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = partyDetailModel.creatorNickname,
                            modifier = Modifier.width(159.dp),
                            style = CurtainCallTheme.typography.body3.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        if (!isMyWriting) {
                            Text(
                                text = stringResource(R.string.report),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onNavigateToReport(partyDetailModel.id, ReportType.PARTY) },
                                style = CurtainCallTheme.typography.body4.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = Grey6
                                ),
                                textAlign = TextAlign.End
                            )
                        }
                    }
                    Text(
                        text = partyDetailModel.createdAt.convertUIDate(),
                        style = CurtainCallTheme.typography.body3.copy(
                            color = Grey5
                        )
                    )
                }
            }
            Text(
                text = partyDetailModel.title,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 20.dp),
                style = CurtainCallTheme.typography.subTitle4,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = partyDetailModel.content,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 10.dp),
                style = CurtainCallTheme.typography.body3.copy(
                    color = Grey1
                )
            )
            Box(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.CenterStart
            ) {
                Canvas(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .size(16.dp)
                        .offset(x = (-8).dp)
                ) {
                    drawArc(
                        color = Primary,
                        startAngle = -90f,
                        sweepAngle = 180f,
                        useCenter = false
                    )
                }
                DottedLine(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .fillMaxWidth(),
                    strokeWidth = 5.dp.value,
                    strokeColor = Grey7
                )
                Canvas(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .size(16.dp)
                        .offset(x = 8.dp)
                ) {
                    drawArc(
                        color = Primary,
                        startAngle = -90f,
                        sweepAngle = -180f,
                        useCenter = false
                    )
                }
            }
            Row(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.party_member_detail_show_name),
                    modifier = Modifier.width(69.dp),
                    style = CurtainCallTheme.typography.body4.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = Grey5
                    )
                )
                Text(
                    text = partyDetailModel.showName,
                    style = CurtainCallTheme.typography.body4.copy(
                        color = Grey1
                    )
                )
            }
            Row(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.party_member_detail_show_date),
                    modifier = Modifier.width(69.dp),
                    style = CurtainCallTheme.typography.body4.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = Grey5
                    )
                )
                Text(
                    text = partyDetailModel.showAt.convertUIDate(),
                    style = CurtainCallTheme.typography.body4.copy(
                        color = Grey1
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Row(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.party_member_detail_show_time),
                    modifier = Modifier.width(69.dp),
                    style = CurtainCallTheme.typography.body4.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = Grey5
                    )
                )
                Text(
                    text = if (partyDetailModel.showAt.isNotEmpty()) partyDetailModel.showAt.substring(11, 16) else "",
                    style = CurtainCallTheme.typography.body4.copy(
                        color = Grey1
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Row(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.party_member_detail_show_location),
                    modifier = Modifier.width(69.dp),
                    style = CurtainCallTheme.typography.body4.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = Grey5
                    )
                )
                Text(
                    text = partyDetailModel.facilityName,
                    style = CurtainCallTheme.typography.body4.copy(
                        color = Grey1
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Row(
                modifier = Modifier
                    .padding(top = 9.dp)
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.party_member_detail_number_of_participation),
                    modifier = Modifier.width(69.dp),
                    style = CurtainCallTheme.typography.body4.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = Grey5
                    )
                )
                if (partyDetailModel.curMemberNum > 3) {
                    for (i in 0..2) {
                        Icon(
                            painter = painterResource(R.drawable.ic_default_profile),
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = Color.Unspecified
                        )
                    }
                    Text(
                        text = String.format("+%d명", partyDetailModel.curMemberNum - 3),
                        modifier = Modifier.padding(start = 4.dp),
                        style = CurtainCallTheme.typography.body4.copy(
                            color = Grey1
                        )
                    )
                } else {
                    (0 until partyDetailModel.curMemberNum).forEach {
                        Icon(
                            painter = painterResource(R.drawable.ic_default_profile),
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = Color.Unspecified
                        )
                    }
                }
            }
            Spacer(Modifier.size(31.dp))
        }
    }
}
