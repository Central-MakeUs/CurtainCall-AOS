package com.cmc.curtaincall.common.designsystem.custom.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cmc.curtaincall.common.designsystem.R
import com.cmc.curtaincall.common.designsystem.theme.Bright_Gray
import com.cmc.curtaincall.common.designsystem.theme.CurtainCallTheme

@Composable
fun CurtainCallRatingBar(
    modifier: Modifier = Modifier,
    rating: Int = 0,
    onClick: (Int) -> Unit = {}
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        for (index in 1..5) {
            Icon(
                painter = painterResource(R.drawable.ic_star),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f)
                    .clickable { onClick(index) },
                tint = if (rating >= index) Color.Unspecified else Bright_Gray
            )
        }
    }
}

@Preview
@Composable
private fun CurtainCallRatingBarPreview() {
    CurtainCallTheme {
        Column {
            CurtainCallRatingBar(
                modifier = Modifier.size(84.dp, 16.dp),
                rating = 3
            )
            CurtainCallRatingBar(
                modifier = Modifier.size(206.dp, 38.dp),
                rating = 5
            )
        }
    }
}
