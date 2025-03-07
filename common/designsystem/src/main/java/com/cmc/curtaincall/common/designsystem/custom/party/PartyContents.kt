package com.cmc.curtaincall.common.designsystem.custom.party

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.cmc.curtaincall.common.designsystem.R
import com.cmc.curtaincall.common.designsystem.component.basic.DottedLine
import com.cmc.curtaincall.common.designsystem.theme.CurtainCallTheme
import com.cmc.curtaincall.common.designsystem.theme.Grey2
import com.cmc.curtaincall.common.designsystem.theme.Grey3
import com.cmc.curtaincall.common.designsystem.theme.Grey4
import com.cmc.curtaincall.common.designsystem.theme.Grey8
import com.cmc.curtaincall.common.utility.extensions.convertPartyDate
import com.cmc.curtaincall.common.utility.extensions.convertPartyTime
import com.cmc.curtaincall.domain.model.member.MyRecruitmentModel
import com.cmc.curtaincall.domain.model.party.PartyModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun PartyEmptyContent(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .background(CurtainCallTheme.colors.background, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_partymember),
                contentDescription = null,
                modifier = Modifier.size(34.dp),
                tint = CurtainCallTheme.colors.primary
            )
        }
        Text(
            text = stringResource(R.string.empty_party_member),
            modifier = Modifier.padding(top = 12.dp),
            style = CurtainCallTheme.typography.body3.copy(
                fontWeight = FontWeight.SemiBold,
                color = CurtainCallTheme.colors.primary
            ),
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
fun PartyEmptyContentPreview() {
    CurtainCallTheme {
        PartyEmptyContent()
    }
}

@Composable
fun PartyHomeContent(
    modifier: Modifier = Modifier,
    creatorImageUrl: String?,
    creatorNickname: String,
    myRecruitmentModel: MyRecruitmentModel,
    onClick: () -> Unit = {}
) {
    val showAt = myRecruitmentModel.showAt?.let {
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(it)
    } ?: Date()
    val today = Date()

    Column(
        modifier = modifier
            .size(290.dp, 182.dp)
            .background(CurtainCallTheme.colors.background, RoundedCornerShape(12.dp))
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .padding(top = 12.dp, bottom = 5.dp)
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
                        modifier = Modifier.padding(start = 8.dp),
                        style = CurtainCallTheme.typography.body4.copy(
                            fontWeight = FontWeight.SemiBold
                        )
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
        Box(
            modifier = Modifier.fillMaxWidth(),
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
                .padding(top = 7.dp, bottom = 14.dp)
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
//            if (partyModel.curMemberNum < partyModel.maxMemberNum && today <= showAt) {
//                if (partyModel.isParticipation) {
//                    R.string.participating
//                } else {
//                    R.string.recruiting
//                }
//            } else {
//                R.string.finish_recruitment
//            }
            Box(
                modifier = Modifier
                    .padding(start = 6.dp)
                    .background(
                        if (myRecruitmentModel.curMemberNum < myRecruitmentModel.maxMemberNum && today <= showAt) {
                            CurtainCallTheme.colors.secondary
                        } else {
                            Grey8
                        },
                        RoundedCornerShape(4.dp)
                    )
                    .padding(vertical = 2.dp, horizontal = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(
                        if (myRecruitmentModel.curMemberNum < myRecruitmentModel.maxMemberNum && today <= showAt) {
                            R.string.participating
                        } else {
                            R.string.finish_recruitment
                        }
                    ),
                    style = CurtainCallTheme.typography.body5.copy(
                        color = if (myRecruitmentModel.curMemberNum < myRecruitmentModel.maxMemberNum && today <= showAt) {
                            CurtainCallTheme.colors.primary
                        } else {
                            Grey4
                        }
                    )
                )
            }
        }
    }
}

@Composable
fun PartyContent(
    modifier: Modifier = Modifier,
    partyModel: PartyModel,
    onClick: () -> Unit = {}
) {
    val showAt = partyModel.showAt?.let {
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.KOREA).parse(it)
    } ?: Date()
    val today = Date()

    Column(
        modifier = modifier
            .size(320.dp, 182.dp)
            .background(CurtainCallTheme.colors.background, RoundedCornerShape(12.dp))
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .padding(top = 12.dp, bottom = 5.dp)
                .padding(horizontal = 12.dp)
                .fillMaxWidth()
        ) {
            AsyncImage(
                model = partyModel.showPoster,
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
                        model = partyModel.creatorImageUrl,
                        error = painterResource(R.drawable.ic_default_profile),
                        contentDescription = null,
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.FillBounds
                    )
                    Text(
                        text = partyModel.creatorNickname,
                        modifier = Modifier.padding(start = 8.dp),
                        style = CurtainCallTheme.typography.body4.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
                Text(
                    text = partyModel.title,
                    modifier = Modifier.padding(top = 17.dp),
                    style = CurtainCallTheme.typography.body2.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = partyModel.content,
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
            modifier = Modifier.fillMaxWidth(),
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
                .padding(top = 7.dp, bottom = 14.dp)
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
                    text = partyModel.showAt?.convertPartyDate() ?: stringResource(R.string.no_information),
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
                    text = partyModel.showAt?.convertPartyTime() ?: stringResource(R.string.no_information),
                    style = CurtainCallTheme.typography.body5.copy(
                        color = Grey4
                    )
                )
            }
            Box(
                modifier = Modifier
                    .padding(start = 6.dp)
                    .background(
                        if (partyModel.curMemberNum < partyModel.maxMemberNum && today <= showAt) {
                            if (partyModel.isParticipation) {
                                CurtainCallTheme.colors.secondary
                            } else {
                                CurtainCallTheme.colors.systemGreen.copy(0.1f)
                            }
                        } else {
                            Grey8
                        },
                        RoundedCornerShape(4.dp)
                    )
                    .padding(vertical = 2.dp, horizontal = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(
                        if (partyModel.curMemberNum < partyModel.maxMemberNum && today <= showAt) {
                            if (partyModel.isParticipation) {
                                R.string.participating
                            } else {
                                R.string.recruiting
                            }
                        } else {
                            R.string.finish_recruitment
                        }
                    ),
                    style = CurtainCallTheme.typography.body5.copy(
                        color = if (partyModel.curMemberNum < partyModel.maxMemberNum && today <= showAt) {
                            if (partyModel.isParticipation) {
                                CurtainCallTheme.colors.primary
                            } else {
                                CurtainCallTheme.colors.systemGreen
                            }
                        } else {
                            Grey4
                        }
                    )
                )
            }
            Spacer(Modifier.weight(1f))
            Text(
                text = "${partyModel.curMemberNum}/${partyModel.maxMemberNum}",
                modifier = Modifier.padding(end = 16.dp),
                style = CurtainCallTheme.typography.body5.copy(
                    color = Grey2
                )
            )
        }
    }
}

@Preview
@Composable
fun PartyContentPreview() {
    CurtainCallTheme {
        PartyContent(
            partyModel = PartyModel(
                creatorNickname = "ows3090",
                title = "제목제목제목",
                content = "내용두줄내용두줄내용두줄내용두줄내용두줄내용두줄내용두줄내용두줄내용두",
                showAt = "2023-04-28T19:30:00",
                curMemberNum = 3,
                maxMemberNum = 5
            )
        )
    }
}
