package com.cmc.curtaincall.common.designsystem.custom.my

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import coil.compose.AsyncImage
import com.cmc.curtaincall.common.designsystem.R
import com.cmc.curtaincall.common.designsystem.component.basic.DottedLine
import com.cmc.curtaincall.common.designsystem.component.divider.HorizontalDivider
import com.cmc.curtaincall.common.designsystem.theme.CurtainCallTheme
import com.cmc.curtaincall.common.designsystem.theme.Grey1
import com.cmc.curtaincall.common.designsystem.theme.Grey3
import com.cmc.curtaincall.common.designsystem.theme.Grey4
import com.cmc.curtaincall.common.designsystem.theme.Grey6
import com.cmc.curtaincall.common.designsystem.theme.Grey8
import com.cmc.curtaincall.common.designsystem.theme.Grey9
import com.cmc.curtaincall.common.utility.extensions.convertPartyDate
import com.cmc.curtaincall.common.utility.extensions.convertPartyTime
import com.cmc.curtaincall.domain.model.member.MyParticipationModel
import com.cmc.curtaincall.domain.model.member.MyRecruitmentModel
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun MyRecruitmentItem(
    modifier: Modifier = Modifier,
    myRecruitmentModel: MyRecruitmentModel,
    creatorImageUrl: String?,
    creatorNickname: String,
    isEditMode: Boolean = false,
    onMore: () -> Unit = {},
    onEdit: () -> Unit = {},
    onDelete: () -> Unit = {},
    onNavigateToDetail: () -> Unit = {},
    onNavigateToLiveTalk: () -> Unit = {}
) {
    val isEndRecruitment = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(myRecruitmentModel.showAt) <= Date()
    Box(
        modifier = modifier
            .size(320.dp, 222.dp)
            .background(CurtainCallTheme.colors.background, RoundedCornerShape(12.dp))
            .clickable { onNavigateToDetail() }
    ) {
        Row(
            modifier = Modifier
                .padding(top = 12.dp)
                .padding(horizontal = 12.dp)
                .fillMaxWidth()
        ) {
            AsyncImage(
                model = myRecruitmentModel.showPoster,
                contentDescription = null,
                error = painterResource(R.drawable.ic_error_poster),
                modifier = Modifier
                    .size(80.dp, 109.dp)
                    .clip(RoundedCornerShape(6.dp)),
                contentScale = ContentScale.FillBounds
            )
            Column(modifier = Modifier.padding(start = 10.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AsyncImage(
                        model = creatorImageUrl,
                        error = painterResource(R.drawable.ic_default_profile),
                        contentDescription = null,
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.FillBounds
                    )
                    Text(
                        text = creatorNickname,
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .weight(1f),
                        style = CurtainCallTheme.typography.body4.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                    Icon(
                        painter = painterResource(R.drawable.ic_more_vert),
                        contentDescription = null,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { onMore() },
                        tint = Color.Unspecified
                    )
                }
                Text(
                    text = myRecruitmentModel.title,
                    modifier = Modifier.padding(top = 17.dp),
                    style = CurtainCallTheme.typography.body2.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = myRecruitmentModel.content,
                    modifier = Modifier.padding(top = 8.dp),
                    style = CurtainCallTheme.typography.body4.copy(
                        color = Grey3
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        if (isEditMode) {
            Card(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(end = 12.dp, top = 38.dp)
                    .size(73.dp, 88.dp),
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
                        .clickable { onEdit() },
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
                        .clickable { onDelete() },
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

        Box(
            modifier = Modifier
                .padding(top = 126.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.CenterStart
        ) {
            Canvas(
                modifier = Modifier
                    .size(14.dp)
                    .offset(x = (-7).dp)
            ) {
                drawArc(
                    color = Grey8,
                    startAngle = -90f,
                    sweepAngle = 180f,
                    useCenter = false
                )
            }
            DottedLine(
                modifier = Modifier.fillMaxWidth(),
                strokeWidth = 5.dp.value,
                strokeColor = Grey8
            )
            Canvas(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .size(14.dp)
                    .offset(x = 7.dp)
            ) {
                drawArc(
                    color = Grey8,
                    startAngle = -90f,
                    sweepAngle = -180f,
                    useCenter = false
                )
            }
        }
        Row(
            modifier = Modifier
                .padding(top = 147.dp)
                .padding(start = 17.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .background(Grey8, RoundedCornerShape(4.dp))
                    .padding(vertical = 2.dp, horizontal = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = myRecruitmentModel.showAt?.convertPartyDate() ?: stringResource(R.string.no_information),
                    style = CurtainCallTheme.typography.body5.copy(
                        color = Grey4
                    )
                )
            }
            Box(
                modifier = Modifier
                    .padding(start = 6.dp)
                    .background(Grey8, RoundedCornerShape(4.dp))
                    .padding(vertical = 2.dp, horizontal = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = myRecruitmentModel.showAt?.convertPartyTime() ?: stringResource(R.string.no_information),
                    style = CurtainCallTheme.typography.body5.copy(
                        color = Grey4
                    )
                )
            }
            Box(
                modifier = Modifier
                    .padding(start = 6.dp)
                    .background(Grey8, RoundedCornerShape(4.dp))
                    .padding(vertical = 2.dp, horizontal = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(
                        if (myRecruitmentModel.curMemberNum == myRecruitmentModel.maxMemberNum || isEndRecruitment) {
                            R.string.finish_recruitment
                        } else {
                            R.string.recruiting
                        }
                    ),
                    style = CurtainCallTheme.typography.body5.copy(
                        color = Grey4
                    )
                )
            }
        }
        Box(
            modifier = Modifier
                .padding(top = 182.dp)
                .fillMaxWidth()
                .height(40.dp)
                .background(
                    color = CurtainCallTheme.colors.secondary,
                    shape = RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp)
                ).clickable { onNavigateToLiveTalk() },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.enter_livetalk),
                style = CurtainCallTheme.typography.body3.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = CurtainCallTheme.colors.primary
                )
            )
        }
    }
}

@Composable
fun MyParticipationtItem(
    modifier: Modifier = Modifier,
    myParticipationModel: MyParticipationModel,
    onNavigateToDetail: () -> Unit = {},
    onCancel: () -> Unit = {},
    onNavigateToLiveTalk: () -> Unit = {}
) {
    val isEndRecruitment = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(myParticipationModel.showAt) <= Date()
    Box(
        modifier = modifier
            .size(320.dp, 222.dp)
            .background(CurtainCallTheme.colors.background, RoundedCornerShape(12.dp))
            .clickable { onNavigateToDetail() }
    ) {
        Row(
            modifier = Modifier
                .padding(top = 12.dp)
                .padding(horizontal = 12.dp)
                .fillMaxWidth()
        ) {
            AsyncImage(
                model = myParticipationModel.showPoster,
                contentDescription = null,
                error = painterResource(R.drawable.ic_error_poster),
                modifier = Modifier
                    .size(80.dp, 109.dp)
                    .clip(RoundedCornerShape(6.dp)),
                contentScale = ContentScale.FillBounds
            )
            Column(modifier = Modifier.padding(start = 10.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AsyncImage(
                        model = myParticipationModel.creatorImageUrl,
                        error = painterResource(R.drawable.ic_default_profile),
                        contentDescription = null,
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.FillBounds
                    )
                    Text(
                        text = myParticipationModel.creatorNickname,
                        modifier = Modifier.padding(start = 8.dp),
                        style = CurtainCallTheme.typography.body4.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
                Text(
                    text = myParticipationModel.title,
                    modifier = Modifier.padding(top = 17.dp),
                    style = CurtainCallTheme.typography.body2.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = myParticipationModel.content,
                    modifier = Modifier.padding(top = 8.dp),
                    style = CurtainCallTheme.typography.body4.copy(
                        color = Grey3
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        Box(
            modifier = Modifier
                .padding(top = 126.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.CenterStart
        ) {
            Canvas(
                modifier = Modifier
                    .size(14.dp)
                    .offset(x = (-7).dp)
            ) {
                drawArc(
                    color = Grey8,
                    startAngle = -90f,
                    sweepAngle = 180f,
                    useCenter = false
                )
            }
            DottedLine(
                modifier = Modifier.fillMaxWidth(),
                strokeWidth = 5.dp.value,
                strokeColor = Grey8
            )
            Canvas(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .size(14.dp)
                    .offset(x = 7.dp)
            ) {
                drawArc(
                    color = Grey8,
                    startAngle = -90f,
                    sweepAngle = -180f,
                    useCenter = false
                )
            }
        }
        Row(
            modifier = Modifier
                .padding(top = 147.dp)
                .padding(start = 17.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .background(Grey8, RoundedCornerShape(4.dp))
                    .padding(vertical = 2.dp, horizontal = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = myParticipationModel.showAt.convertPartyDate() ?: stringResource(R.string.no_information),
                    style = CurtainCallTheme.typography.body5.copy(
                        color = Grey4
                    )
                )
            }
            Box(
                modifier = Modifier
                    .padding(start = 6.dp)
                    .background(Grey8, RoundedCornerShape(4.dp))
                    .padding(vertical = 2.dp, horizontal = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = myParticipationModel.showAt.convertPartyTime() ?: stringResource(R.string.no_information),
                    style = CurtainCallTheme.typography.body5.copy(
                        color = Grey4
                    )
                )
            }
            Box(
                modifier = Modifier
                    .padding(start = 6.dp)
                    .background(Grey8, RoundedCornerShape(4.dp))
                    .padding(vertical = 2.dp, horizontal = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(
                        if (myParticipationModel.curMemberNum == myParticipationModel.maxMemberNum || isEndRecruitment) {
                            R.string.finish_recruitment
                        } else {
                            R.string.recruiting
                        }
                    ),
                    style = CurtainCallTheme.typography.body5.copy(
                        color = Grey4
                    )
                )
            }
        }
        Row(
            modifier = Modifier
                .padding(top = 182.dp)
                .fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp)
                    .background(
                        color = Grey9,
                        shape = RoundedCornerShape(bottomStart = 12.dp)
                    )
                    .clickable { onCancel() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.cancel_participate),
                    style = CurtainCallTheme.typography.body3.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = Grey6
                    )
                )
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp)
                    .background(
                        color = CurtainCallTheme.colors.secondary,
                        shape = RoundedCornerShape(bottomEnd = 12.dp)
                    )
                    .clickable { onNavigateToLiveTalk() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.enter_livetalk),
                    style = CurtainCallTheme.typography.body3.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = CurtainCallTheme.colors.primary
                    )
                )
            }
        }
    }
}
