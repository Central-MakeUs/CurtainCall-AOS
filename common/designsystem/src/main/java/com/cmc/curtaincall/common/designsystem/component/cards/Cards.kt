package com.cmc.curtaincall.common.designsystem.component.cards

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import coil.compose.AsyncImage
import com.cmc.curtaincall.common.designsystem.R
import com.cmc.curtaincall.common.designsystem.component.buttons.common.CurtainCallFilledButton
import com.cmc.curtaincall.common.designsystem.component.chips.CurtainCallBasicChip
import com.cmc.curtaincall.common.designsystem.theme.CurtainCallTheme
import com.cmc.curtaincall.common.designsystem.theme.Grey4
import com.cmc.curtaincall.common.designsystem.theme.Grey8
import com.cmc.curtaincall.domain.model.show.ShowDetailModel
import com.cmc.curtaincall.domain.enums.translateShowGenreType

@Composable
fun ShowDetailCard(
    modifier: Modifier = Modifier,
    showDetailModel: ShowDetailModel = ShowDetailModel(),
    isFavorite: Boolean = false,
    onLikeClick: () -> Unit = {},
    onNavigateToLiveTalk: () -> Unit = {}
) {
    var updateFavorite by remember(isFavorite) { mutableStateOf(isFavorite) }
    Card(
        modifier = modifier.size(320.dp, 558.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CurtainCallTheme.colors.background),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 10.dp)
    ) {
        Icon(
            painter = painterResource(
                if (updateFavorite) {
                    R.drawable.ic_white_like_filled
                } else {
                    R.drawable.ic_white_like_unfilled
                }
            ),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.End)
                .padding(18.dp)
                .size(24.dp)
                .clickable {
                    onLikeClick()
                    updateFavorite = updateFavorite.not()
                },
            tint = Color.Unspecified
        )
        AsyncImage(
            model = showDetailModel.poster,
            error = painterResource(R.drawable.ic_error_poster),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clip(RoundedCornerShape(18.dp))
                .size(200.dp, 286.dp),
            contentScale = ContentScale.FillBounds
        )
        CurtainCallBasicChip(
            text = translateShowGenreType(showDetailModel.genre).value,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 18.dp),
            textStyle = CurtainCallTheme.typography.body4,
            isSelect = true
        )
        Text(
            text = showDetailModel.name,
            style = CurtainCallTheme.typography.subTitle1,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 8.dp)
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
                .height(26.dp),
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Row(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 20.dp)
                .clip(RoundedCornerShape(30.dp))
                .border(1.dp, Grey8, RoundedCornerShape(30.dp))
                .padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_star),
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = Color.Unspecified
            )
            Text(
                text = String.format(
                    stringResource(R.string.review_grade_format),
                    if (showDetailModel.reviewCount == 0) 0.0f else showDetailModel.reviewGradeSum / showDetailModel.reviewCount.toFloat()
                ),
                modifier = Modifier.padding(start = 2.dp),
                style = CurtainCallTheme.typography.body4.copy(
                    fontWeight = FontWeight.SemiBold
                )
            )
            Text(
                text = String.format(
                    stringResource(R.string.review_grade_sum_format),
                    showDetailModel.reviewCount
                ),
                modifier = Modifier.padding(start = 6.dp),
                style = CurtainCallTheme.typography.body4.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = Grey4
                )
            )
        }
        CurtainCallFilledButton(
            text = stringResource(R.string.live_talk),
            modifier = Modifier
                .padding(top = 18.dp)
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
                .height(46.dp),
            containerColor = CurtainCallTheme.colors.secondary,
            contentColor = CurtainCallTheme.colors.onSecondary,
            textStyle = CurtainCallTheme.typography.body2.copy(
                fontWeight = FontWeight.SemiBold
            ),
            onClick = onLikeClick
        )
    }
}
