package com.cmc.curtaincall.common.designsystem.component.sheets.bottom

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cmc.curtaincall.common.designsystem.R
import com.cmc.curtaincall.common.designsystem.theme.CurtainCallTheme
import com.cmc.curtaincall.common.designsystem.theme.Grey5
import com.cmc.curtaincall.common.designsystem.theme.NoRippleTheme
import com.cmc.curtaincall.domain.enums.ReviewSortType
import com.cmc.curtaincall.domain.enums.ShowSortType

private val ShowSortBottomSheetHeight = 250.dp
private val ShowSortBottomSheetRadius = 28.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurtainCallShowSortBottomSheet(
    showSortType: ShowSortType = ShowSortType.POPULAR,
    onSelectSortType: (ShowSortType) -> Unit = {},
    onDismissRequest: () -> Unit = {}
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = Modifier
            .fillMaxWidth()
            .height(ShowSortBottomSheetHeight),
        shape = RoundedCornerShape(
            topStart = ShowSortBottomSheetRadius,
            topEnd = ShowSortBottomSheetRadius
        ),
        containerColor = CurtainCallTheme.colors.background,
        dragHandle = null
    ) {
        Spacer(Modifier.size(20.dp))
        ShowSortType.values().forEach { sortType ->
            Row(
                modifier = Modifier
                    .padding(horizontal = 30.dp)
                    .fillMaxWidth()
                    .height(50.dp)
                    .clickable { onSelectSortType(sortType) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = sortType.value,
                    modifier = Modifier.weight(1f),
                    style = CurtainCallTheme.typography.body2.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = if (sortType == showSortType) {
                            CurtainCallTheme.colors.primary
                        } else {
                            Grey5
                        }
                    )
                )
                if (sortType == showSortType) {
                    Icon(
                        painter = painterResource(R.drawable.ic_check),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = CurtainCallTheme.colors.secondary
                    )
                }
            }
        }
        Spacer(Modifier.size(30.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurtainCallReviewSortBottomSheet(
    reviewSortType: ReviewSortType = ReviewSortType.RECENTLY,
    onSelectSortType: (ReviewSortType) -> Unit = {},
    onDismissRequest: () -> Unit = {}
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp),
        shape = RoundedCornerShape(
            topStart = ShowSortBottomSheetRadius,
            topEnd = ShowSortBottomSheetRadius
        ),
        containerColor = CurtainCallTheme.colors.background
    ) {
        ReviewSortType.values().forEach { sortType ->
            Row(
                modifier = Modifier
                    .padding(start = 30.dp, end = 15.dp)
                    .fillMaxWidth()
                    .height(50.dp)
                    .clickable { onSelectSortType(sortType) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = sortType.label,
                    modifier = Modifier.weight(1f),
                    style = CurtainCallTheme.typography.body2.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = if (sortType == reviewSortType) {
                            CurtainCallTheme.colors.primary
                        } else {
                            Grey5
                        }
                    )
                )
                if (sortType == reviewSortType) {
                    Icon(
                        painter = painterResource(R.drawable.ic_check),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = CurtainCallTheme.colors.secondary
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun CurtainCallShowSortBottomSheetPreview() {
    var sortType by remember { mutableStateOf(ShowSortType.POPULAR) }
    CurtainCallTheme {
        CompositionLocalProvider(LocalRippleTheme provides NoRippleTheme) {
            CurtainCallShowSortBottomSheet(
                showSortType = sortType,
                onSelectSortType = { sortType = it }
            )
        }
    }
}

@Preview
@Composable
private fun CurtainCallReviewSortBottomSheetPreview() {
    var sortType by remember { mutableStateOf(ReviewSortType.RECENTLY) }
    CurtainCallTheme {
        CompositionLocalProvider(LocalRippleTheme provides NoRippleTheme) {
            CurtainCallReviewSortBottomSheet(
                reviewSortType = sortType,
                onSelectSortType = { sortType = it }
            )
        }
    }
}
