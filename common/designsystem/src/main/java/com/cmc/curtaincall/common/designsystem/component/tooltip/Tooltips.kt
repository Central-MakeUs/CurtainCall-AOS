package com.cmc.curtaincall.common.designsystem.component.tooltip

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cmc.curtaincall.common.designsystem.R
import com.cmc.curtaincall.common.designsystem.theme.CurtainCallTheme
import com.cmc.curtaincall.common.designsystem.theme.Grey1

@Composable
fun CurtainCallCostEffectiveShowTooltip(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit = {}
) {
    Column(modifier.width(302.dp)) {
        Canvas(
            modifier = Modifier
                .padding(start = 50.dp)
                .size(10.dp, 5.dp)
        ) {
            drawPath(
                color = Grey1.copy(0.8f),
                path = Path().apply {
                    moveTo(5.dp.toPx(), 0.dp.toPx())
                    lineTo(0.dp.toPx(), 6.dp.toPx())
                    lineTo(10.dp.toPx(), 6.dp.toPx())
                }
            )
        }
        Box(
            modifier = Modifier
                .size(302.dp, 33.dp)
                .background(Grey1.copy(0.8f), RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = text,
                    style = CurtainCallTheme.typography.body5.copy(
                        color = CurtainCallTheme.colors.onPrimary
                    )
                )
                Icon(
                    painter = painterResource(R.drawable.ic_white_close),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .size(14.dp)
                        .clickable { onClick() },
                    tint = Color.Unspecified
                )
            }
        }
    }
}

@Composable
fun CurtainCallShowSortTooltip(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit = {}
) {
    Column(modifier.width(277.dp)) {
        Canvas(
            modifier = Modifier
                .padding(start = 240.dp)
                .size(10.dp, 5.dp)
        ) {
            drawPath(
                color = Grey1.copy(0.8f),
                path = Path().apply {
                    moveTo(5.dp.toPx(), 0.dp.toPx())
                    lineTo(0.dp.toPx(), 6.dp.toPx())
                    lineTo(10.dp.toPx(), 6.dp.toPx())
                }
            )
        }
        Box(
            modifier = Modifier
                .size(277.dp, 33.dp)
                .background(Grey1.copy(0.8f), RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = text,
                    style = CurtainCallTheme.typography.body5.copy(
                        color = CurtainCallTheme.colors.onPrimary
                    )
                )
                Icon(
                    painter = painterResource(R.drawable.ic_white_close),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .size(14.dp)
                        .clickable { onClick() },
                    tint = Color.Unspecified
                )
            }
        }
    }
}

@Composable
fun CurtainCallShowLiveTalkTooltip(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit = {}
) {
    Column(modifier.width(279.dp)) {
        Canvas(
            modifier = Modifier
                .padding(start = 135.dp)
                .size(10.dp, 5.dp)
        ) {
            drawPath(
                color = Grey1.copy(0.8f),
                path = Path().apply {
                    moveTo(5.dp.toPx(), 0.dp.toPx())
                    lineTo(0.dp.toPx(), 6.dp.toPx())
                    lineTo(10.dp.toPx(), 6.dp.toPx())
                }
            )
        }
        Box(
            modifier = Modifier
                .size(279.dp, 33.dp)
                .background(Grey1.copy(0.8f), RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = text,
                    style = CurtainCallTheme.typography.body5.copy(
                        color = CurtainCallTheme.colors.onPrimary
                    )
                )
                Icon(
                    painter = painterResource(R.drawable.ic_white_close),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .size(14.dp)
                        .clickable { onClick() },
                    tint = Color.Unspecified
                )
            }
        }
    }
}

@Composable
fun CurtainCallPartyTooltip(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit = {}
) {
    Column(modifier.width(215.dp)) {
        Canvas(
            modifier = Modifier
                .padding(start = 22.dp)
                .size(10.dp, 5.dp)
        ) {
            drawPath(
                color = Grey1.copy(0.8f),
                path = Path().apply {
                    moveTo(5.dp.toPx(), 0.dp.toPx())
                    lineTo(0.dp.toPx(), 6.dp.toPx())
                    lineTo(10.dp.toPx(), 6.dp.toPx())
                }
            )
        }
        Box(
            modifier = Modifier
                .size(215.dp, 33.dp)
                .background(Grey1.copy(0.8f), RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = text,
                    style = CurtainCallTheme.typography.body5.copy(
                        color = CurtainCallTheme.colors.onPrimary
                    )
                )
                Icon(
                    painter = painterResource(R.drawable.ic_white_close),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .size(14.dp)
                        .clickable { onClick() },
                    tint = Color.Unspecified
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 100)
@Composable
private fun CurtainCallShowSortTooltipPreview() {
    CurtainCallTheme {
        CurtainCallShowSortTooltip(
            text = "인기순은 현재 상영 중인 작품 50개만 볼 수 있어요!"
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 100)
@Composable
private fun CurtainCallShowLiveTalkTooltipPreview() {
    CurtainCallTheme {
        CurtainCallShowLiveTalkTooltip(
            text = "실시간으로 공연에 대한 기대감&후기를 공유해봐요!"
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 100)
@Composable
private fun CurtainCallPartyTooltipPreview() {
    CurtainCallTheme {
        CurtainCallPartyTooltip(
            text = "공연을 함께 볼 사람을 구할 수 있어요!"
        )
    }
}
