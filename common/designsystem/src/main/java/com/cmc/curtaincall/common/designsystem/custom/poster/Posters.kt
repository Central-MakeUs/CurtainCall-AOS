package com.cmc.curtaincall.common.designsystem.custom.poster

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.cmc.curtaincall.common.designsystem.R
import com.cmc.curtaincall.common.designsystem.component.buttons.like.CurtainCallLikeButton
import com.cmc.curtaincall.common.designsystem.dimension.Paddings
import com.cmc.curtaincall.common.designsystem.theme.CurtainCallTheme
import com.cmc.curtaincall.common.designsystem.theme.Grey1
import com.cmc.curtaincall.common.designsystem.theme.Grey5
import com.cmc.curtaincall.domain.enums.ShowGenreType

@Composable
fun CurtainCallPopularPoster(
    model: Any?,
    text: String,
    rank: Int = 1,
    genreType: ShowGenreType = ShowGenreType.PLAY,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .size(120.dp, 221.dp)
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(120 / 160f)
        ) {
            AsyncImage(
                model = model,
                contentDescription = text,
                error = painterResource(R.drawable.ic_error_poster),
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.FillBounds
            )
            Box(
                modifier = Modifier
                    .background(Grey1.copy(alpha = 0.6f), RoundedCornerShape(topStart = 10.dp, bottomEnd = 5.dp))
                    .padding(horizontal = Paddings.medium, vertical = Paddings.small),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = rank.toString(),
                    style = CurtainCallTheme.typography.body2.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = CurtainCallTheme.colors.onPrimary
                    )
                )
            }
        }
        Box(
            modifier = Modifier
                .padding(top = 10.dp)
                .background(CurtainCallTheme.colors.primary, RoundedCornerShape(20.dp))
                .padding(horizontal = Paddings.medium, vertical = Paddings.small),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = genreType.value,
                style = CurtainCallTheme.typography.caption.copy(
                    color = CurtainCallTheme.colors.onPrimary
                )
            )
        }
        Text(
            text = text,
            modifier = Modifier.padding(top = 6.dp),
            style = CurtainCallTheme.typography.body3.copy(
                fontWeight = FontWeight.SemiBold
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun CurtainCallOpenShowPoster(
    model: Any?,
    text: String,
    dDay: String,
    openDate: String,
    genreType: ShowGenreType = ShowGenreType.PLAY,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .size(120.dp, 238.dp)
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(120 / 160f)
        ) {
            AsyncImage(
                model = model,
                contentDescription = text,
                error = painterResource(R.drawable.ic_error_poster),
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.FillBounds
            )
            Box(
                modifier = Modifier
                    .padding(top = Paddings.medium, start = Paddings.medium)
                    .background(Grey1.copy(alpha = 0.6f), RoundedCornerShape(6.dp))
                    .padding(horizontal = Paddings.medium, vertical = 3.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = dDay,
                    style = CurtainCallTheme.typography.body4.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = CurtainCallTheme.colors.onPrimary
                    )
                )
            }
        }
        Box(
            modifier = Modifier
                .padding(top = 10.dp)
                .background(CurtainCallTheme.colors.primary, RoundedCornerShape(20.dp))
                .padding(horizontal = Paddings.medium, vertical = Paddings.small),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = genreType.value,
                style = CurtainCallTheme.typography.caption.copy(
                    color = CurtainCallTheme.colors.onPrimary
                )
            )
        }
        Text(
            text = text,
            modifier = Modifier.padding(top = 6.dp),
            style = CurtainCallTheme.typography.body3.copy(
                fontWeight = FontWeight.SemiBold
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = "$openDate~",
            style = CurtainCallTheme.typography.body5.copy(
                color = Grey5
            )
        )
    }
}

@Composable
fun CurtainCallEndShowPoster(
    model: Any?,
    text: String,
    dDay: String,
    endDate: String,
    genreType: ShowGenreType = ShowGenreType.PLAY,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .size(120.dp, 238.dp)
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(120 / 160f)
        ) {
            AsyncImage(
                model = model,
                contentDescription = text,
                error = painterResource(R.drawable.ic_error_poster),
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.FillBounds
            )
            Box(
                modifier = Modifier
                    .padding(top = Paddings.medium, start = Paddings.medium)
                    .background(Grey1.copy(alpha = 0.6f), RoundedCornerShape(6.dp))
                    .padding(horizontal = Paddings.medium, vertical = 3.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = dDay,
                    style = CurtainCallTheme.typography.body4.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = CurtainCallTheme.colors.onPrimary
                    )
                )
            }
        }
        Box(
            modifier = Modifier
                .padding(top = 10.dp)
                .background(CurtainCallTheme.colors.primary, RoundedCornerShape(20.dp))
                .padding(horizontal = Paddings.medium, vertical = Paddings.small),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = genreType.value,
                style = CurtainCallTheme.typography.caption.copy(
                    color = CurtainCallTheme.colors.onPrimary
                )
            )
        }
        Text(
            text = text,
            modifier = Modifier.padding(top = 6.dp),
            style = CurtainCallTheme.typography.body3.copy(
                fontWeight = FontWeight.SemiBold
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = "~$endDate",
            style = CurtainCallTheme.typography.body5.copy(
                color = Grey5
            )
        )
    }
}

@Composable
fun CurtainCallShowPoster(
    model: Any?,
    text: String,
    isLike: Boolean = false,
    onLikeClick: () -> Unit = {},
    onClick: () -> Unit = {}
) {
    var updateLike by remember(isLike) { mutableStateOf(isLike) }
    Column(
        modifier = Modifier
            .size(153.dp, 250.dp)
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(153 / 219f)
                .clip(RoundedCornerShape(12.dp))
        ) {
            AsyncImage(
                model = model,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds
            )
            CurtainCallLikeButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 10.dp, end = 10.dp)
                    .size(28.dp),
                isSelected = updateLike,
                onClick = {
                    onLikeClick()
                    updateLike = !isLike
                }
            )
        }
        Text(
            text = text,
            modifier = Modifier
                .padding(top = 10.dp)
                .fillMaxWidth(),
            style = CurtainCallTheme.typography.body2.copy(
                fontWeight = FontWeight.SemiBold
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun CurtainCallTitlePoster(
    model: Any?,
    title: String,
    isSelected: Boolean = false,
    onSelect: () -> Unit = {}
) {
    Column(
        modifier = Modifier.size(100.dp, 160.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = model,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(100f / 134)
                .clip(RoundedCornerShape(10.dp))
                .clickable { onSelect() }
                .then(
                    if (isSelected) {
                        Modifier.border(3.dp, CurtainCallTheme.colors.primary, RoundedCornerShape(10.dp))
                    } else {
                        Modifier
                    }
                ),
            error = painterResource(R.drawable.ic_error_poster),
            placeholder = painterResource(R.drawable.ic_error_poster),
            contentScale = ContentScale.FillBounds
        )
        Text(
            text = title,
            modifier = Modifier.padding(top = 6.dp),
            style = CurtainCallTheme.typography.body3.copy(
                fontWeight = FontWeight.SemiBold,
                color = Grey1
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun CurtainCallFavoritePoster(
    model: Any?,
    title: String = "",
    isFavorite: Boolean = false,
    onFavoriteClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier.size(153.dp, 250.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(153f / 219)
                .clip(RoundedCornerShape(12.dp))
        ) {
            AsyncImage(
                model = model,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds,
                error = painterResource(R.drawable.ic_error_poster),
                placeholder = painterResource(R.drawable.ic_error_poster)
            )
            Icon(
                painter = painterResource(if (isFavorite) R.drawable.ic_like_filled else R.drawable.ic_like_unfilled),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 10.dp, end = 10.dp)
                    .size(28.dp)
                    .clickable { onFavoriteClick() },
                tint = Color.Unspecified
            )
        }
        Text(
            text = title,
            modifier = Modifier.padding(top = 10.dp),
            style = CurtainCallTheme.typography.body2.copy(
                fontWeight = FontWeight.SemiBold
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
