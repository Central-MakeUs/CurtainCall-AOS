package com.cmc.curtaincall.feature.partymember.detail

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.cmc.curtaincall.common.designsystem.R
import com.cmc.curtaincall.common.designsystem.component.appbars.CurtainCallCenterTopAppBarWithBack
import com.cmc.curtaincall.common.designsystem.component.basic.DottedLine
import com.cmc.curtaincall.common.designsystem.component.basic.SystemUiStatusBar
import com.cmc.curtaincall.common.designsystem.component.buttons.common.CurtainCallFilledButton
import com.cmc.curtaincall.common.designsystem.theme.CurtainCallTheme
import com.cmc.curtaincall.common.designsystem.theme.Grey1
import com.cmc.curtaincall.common.designsystem.theme.Grey5
import com.cmc.curtaincall.common.designsystem.theme.Grey6
import com.cmc.curtaincall.common.designsystem.theme.Grey7
import com.cmc.curtaincall.common.designsystem.theme.Primary
import com.cmc.curtaincall.common.utility.extensions.convertUIDate

@Composable
fun PartyMemberDetailScreen(
    partyMemberDetailViewModel: PartyMemberDetailViewModel = hiltViewModel(),
    partyId: Int?,
    showName: String?,
    onBack: () -> Unit = {}
) {
    checkNotNull(partyId)
    checkNotNull(showName)

    LaunchedEffect(Unit) {
        partyMemberDetailViewModel.requestPartyDetail(partyId)
    }

    SystemUiStatusBar(CurtainCallTheme.colors.primary)
    Scaffold(
        topBar = {
            CurtainCallCenterTopAppBarWithBack(
                title = showName,
                containerColor = CurtainCallTheme.colors.primary,
                contentColor = CurtainCallTheme.colors.onPrimary,
                onBack = onBack
            )
        },
        floatingActionButton = {
            // TODO 파티원 참여 여부에 따라 분기처리
            CurtainCallFilledButton(
                text = stringResource(R.string.participate),
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
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { paddingValues ->
        PartyMemberDetailContent(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(CurtainCallTheme.colors.primary)
        )
    }
}

@Composable
private fun PartyMemberDetailContent(
    modifier: Modifier = Modifier,
    partyMemberDetailViewModel: PartyMemberDetailViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()
    val partyDetailModel by partyMemberDetailViewModel.partyDetailModel.collectAsStateWithLifecycle()

    Column(modifier = modifier.verticalScroll(scrollState)) {
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
                            modifier = Modifier.weight(1f),
                            style = CurtainCallTheme.typography.body3.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = stringResource(R.string.report),
                            modifier = Modifier.padding(start = 32.dp),
                            style = CurtainCallTheme.typography.body4.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = Grey6
                            )
                        )
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
            Row(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Canvas(
                    modifier = Modifier
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
                    modifier = Modifier.fillMaxWidth(),
                    strokeWidth = 5.dp.value,
                    strokeColor = Grey7
                )
                Canvas(
                    modifier = Modifier
                        .size(16.dp)
                        .offset(x = 6.dp)
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
                    (0 until 3).forEach {
                        Icon(
                            painter = painterResource(R.drawable.ic_default_profile),
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = Color.Unspecified
                        )
                    }
                    Text(
                        text = String.format("+%02d명", partyDetailModel.curMemberNum - 3),
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
