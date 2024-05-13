package com.cmc.curtaincall.common.designsystem.custom.show

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
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
import com.cmc.curtaincall.common.designsystem.component.buttons.like.CurtainCallReviewLikeButton
import com.cmc.curtaincall.common.designsystem.component.chips.CurtainCallBasicChip
import com.cmc.curtaincall.common.designsystem.component.menus.CurtainCallMenu
import com.cmc.curtaincall.common.designsystem.custom.common.CurtainCallRatingBar
import com.cmc.curtaincall.common.designsystem.theme.CurtainCallTheme
import com.cmc.curtaincall.common.designsystem.theme.Grey3
import com.cmc.curtaincall.common.designsystem.theme.Grey5
import com.cmc.curtaincall.common.designsystem.theme.Grey6
import com.cmc.curtaincall.common.utility.extensions.convertUIDate
import com.cmc.curtaincall.domain.model.member.MemberReviewModel
import com.cmc.curtaincall.domain.model.review.ShowReviewModel

@Composable
fun ShowReviewEmptyContent(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_review_empty),
            contentDescription = null,
            modifier = Modifier.size(60.dp),
            tint = Color.Unspecified
        )
        Text(
            text = stringResource(R.string.empty_show_review),
            modifier = Modifier.padding(top = 12.dp),
            style = CurtainCallTheme.typography.body3.copy(
                fontWeight = FontWeight.SemiBold,
                color = CurtainCallTheme.colors.primary
            )
        )
    }
}

@Preview
@Composable
private fun ShowReviewEmptyContentPreview() {
    CurtainCallTheme {
        ShowReviewEmptyContent()
    }
}

@Composable
fun ShowReviewListEmptyContent(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_review_empty),
            contentDescription = null,
            modifier = Modifier.size(60.dp),
            tint = Color.Unspecified
        )
        Text(
            text = stringResource(R.string.show_review_empty_list_content),
            modifier = Modifier.padding(top = 12.dp),
            style = CurtainCallTheme.typography.body3.copy(
                fontWeight = FontWeight.SemiBold,
                color = CurtainCallTheme.colors.primary
            ),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ShowReviewItemContent(
    modifier: Modifier = Modifier,
    showReviewModel: ShowReviewModel
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = CurtainCallTheme.colors.background
        ),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
                .padding(top = 12.dp, bottom = 10.dp)
        ) {
            CurtainCallRatingBar(
                modifier = Modifier.size(84.dp, 16.dp),
                rating = showReviewModel.grade
            )
            Text(
                text = showReviewModel.content,
                modifier = Modifier.padding(top = 10.dp),
                style = CurtainCallTheme.typography.body4,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = String.format(
                    "%s | %s",
                    showReviewModel.creatorNickname,
                    showReviewModel.createdAt.convertUIDate()
                ),
                modifier = Modifier.padding(top = 10.dp),
                style = CurtainCallTheme.typography.body4.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = Grey6
                )
            )
        }
    }
}

@Preview
@Composable
private fun ShowReviewContentPreview() {
    CurtainCallTheme {
        ShowReviewItemContent(
            modifier = Modifier.fillMaxWidth(),
            showReviewModel = ShowReviewModel(
                content = "리뷰리뷰리뷰리뷰리뷰리뷰리뷰리뷰리뷰리뷰리뷰리뷰리뷰리뷰리뷰리뷰리뷰리뷰리뷰리뷰리뷰",
                grade = 3,
                creatorNickname = "ows3090",
                createdAt = "2023-08-31T03:28:00"
            )
        )
    }
}

@Composable
fun ShowReviewItemContent(
    modifier: Modifier = Modifier,
    showReviewModel: ShowReviewModel = ShowReviewModel(),
    isMyReview: Boolean = false,
    showMenu: Boolean = false,
    isFavorite: Boolean = false,
    onMoreClick: () -> Unit = {},
    onLikeClick: () -> Unit = {},
    onEditClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {},
    onReportClick: () -> Unit = {}
) {
    var updateLikeCount by remember(showReviewModel) { mutableIntStateOf(showReviewModel.likeCount) }
    var updateFavorite by remember(isFavorite) { mutableStateOf(isFavorite) }
    var hasVisualOverflow by remember { mutableStateOf(false) }
    var moreView by remember { mutableStateOf(false) }
    Box(
        modifier = modifier
            .background(CurtainCallTheme.colors.background)
            .padding(20.dp)
    ) {
        Column(Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = showReviewModel.creatorImageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape),
                    error = ColorPainter(Color.LightGray),
                    contentScale = ContentScale.FillBounds
                )
                Column(Modifier.padding(start = 12.dp)) {
                    Text(
                        text = showReviewModel.creatorNickname,
                        style = CurtainCallTheme.typography.body3.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                    Text(
                        text = showReviewModel.createdAt.convertUIDate(),
                        style = CurtainCallTheme.typography.body3.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = Grey5
                        )
                    )
                }
                if (isMyReview) {
                    Spacer(Modifier.weight(1f))
                    Icon(
                        painter = painterResource(R.drawable.ic_more_vert),
                        contentDescription = null,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { onMoreClick() },
                        tint = Color.Unspecified
                    )
                } else {
                    Text(
                        text = stringResource(R.string.report),
                        modifier
                            .fillMaxWidth()
                            .clickable { onReportClick() },
                        style = CurtainCallTheme.typography.body4.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = Grey6
                        ),
                        textAlign = TextAlign.End
                    )
                }
            }
            CurtainCallRatingBar(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .size(84.dp, 16.dp),
                rating = showReviewModel.grade
            )
            Text(
                text = showReviewModel.content,
                modifier = Modifier.padding(top = 10.dp),
                style = CurtainCallTheme.typography.body3.copy(
                    color = Grey3
                ),
                maxLines = if (moreView) Int.MAX_VALUE else 3,
                overflow = TextOverflow.Ellipsis,
                onTextLayout = { textLayoutResult ->
                    if (textLayoutResult.hasVisualOverflow) {
                        hasVisualOverflow = true
                    }
                }
            )
            if (hasVisualOverflow) {
                Text(
                    text = stringResource(
                        if (moreView) {
                            R.string.simple_view
                        } else {
                            R.string.more_view
                        }
                    ),
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .clickable { moreView = !moreView },
                    style = CurtainCallTheme.typography.body3.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = CurtainCallTheme.colors.primary
                    )
                )
            }
            CurtainCallReviewLikeButton(
                modifier = Modifier.padding(top = 20.dp),
                num = updateLikeCount,
                isSelected = updateFavorite,
                onClick = {
                    if (updateFavorite) {
                        updateLikeCount--
                    } else {
                        updateLikeCount++
                    }
                    updateFavorite = !updateFavorite
                    onLikeClick()
                }
            )
        }
        if (showMenu) {
            CurtainCallMenu(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 32.dp),
                onEdit = onEditClick,
                onDelete = onDeleteClick
            )
        }
    }
}

@Preview
@Composable
private fun ShowReviewContentPreview2() {
    var showMenus by remember { mutableStateOf(false) }
    CurtainCallTheme {
        ShowReviewItemContent(
            modifier = Modifier.fillMaxWidth(),
            showReviewModel = ShowReviewModel(
                content = "청재미있게봤어요고전연극은처음인데엄청재미있게봤어요고전연극은처음인데엄청재미있게" +
                    "봤어요고전연극은처음인데엄청재미있게봤어요고전연극은처음인데엄청재미있게봤어요고전연극은처음인" +
                    "데엄청재미있게봤어요고전연극은처음인데엄청재미있게봤어요고전연극은처음인데엄청재미있게봤어요고전연극" +
                    "은처음인데엄청재미있게봤어요고전연극은처음인데엄청재미있게봤어요고전연극은처음인데엄청재미있게봤어요고전연극" +
                    "은처음인데엄청재미있게봤어요고전연극은처음인데엄청재미있게봤어요고전연극은처음인데엄청재미있게봤어요고전연극은처음" +
                    "인데엄청재미있게봤어요고전연극은처음인데엄청재미있게봤어요고전연극은처음인데엄청재미있게봤어요고전연극은처음인데엄청재" +
                    "미있게봤어요고전연극은처음인데엄청재미있게봤어요고전연극은처음인데엄청재미있게봤어요고전연극은처음인데엄청재미있게봤어" +
                    "요고전연극은처음인데엄청재미있게봤어요고전연극은처음인데엄청재미있게봤어요고전연극은처음인데엄청재미있게봤어요고전연극" +
                    "은처음인데엄청재미있게봤어요고전연극은처음인데엄청재미있",
                createdAt = "2023-08-31T03:28:00",
                creatorNickname = "ows3090",
                grade = 4,
                likeCount = 37
            ),
            isMyReview = true,
            showMenu = showMenus,
            onMoreClick = { showMenus = !showMenus },
            isFavorite = true
        )
    }
}

@Composable
fun MyReviewContent(
    modifier: Modifier = Modifier,
    memberReviewModel: MemberReviewModel = MemberReviewModel(),
    onClick: () -> Unit = {}
) {
    Card(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = CurtainCallTheme.colors.background
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 12.dp, bottom = 10.dp)
                .padding(horizontal = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CurtainCallBasicChip(
                    text = memberReviewModel.genreType.value,
                    isSelect = true
                )
                Text(
                    text = memberReviewModel.showName,
                    modifier = Modifier.padding(start = 10.dp),
                    style = CurtainCallTheme.typography.body3.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    maxLines = 1
                )
            }
            CurtainCallRatingBar(
                modifier = Modifier
                    .padding(top = 11.dp)
                    .size(84.dp, 16.dp),
                rating = memberReviewModel.grade
            )
            Text(
                text = memberReviewModel.content,
                modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth(),
                style = CurtainCallTheme.typography.body4,
                maxLines = 1
            )
            Text(
                text = memberReviewModel.createdAt.convertUIDate(),
                modifier = Modifier.padding(top = 10.dp),
                style = CurtainCallTheme.typography.body4.copy(
                    color = Grey5
                )
            )
        }
    }
}
