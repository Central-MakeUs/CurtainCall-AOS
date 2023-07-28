package com.cmc.curtaincall.common.design.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.cmc.curtaincall.common.design.R

@Composable
fun RatingBar(
    modifier: Modifier = Modifier,
    rating: Int = 0
) {
    Row(modifier) {
        Icon(
            painter = painterResource(
                if (rating >= 1) R.drawable.ic_star else R.drawable.ic_unfilled_star
            ),
            contentDescription = null,
            modifier = Modifier.size(14.dp),
            tint = Color.Unspecified
        )
        Icon(
            painter = painterResource(
                if (rating >= 2) R.drawable.ic_star else R.drawable.ic_unfilled_star
            ),
            contentDescription = null,
            modifier = Modifier.size(14.dp),
            tint = Color.Unspecified
        )
        Icon(
            painter = painterResource(
                if (rating >= 3) R.drawable.ic_star else R.drawable.ic_unfilled_star
            ),
            contentDescription = null,
            modifier = Modifier.size(14.dp),
            tint = Color.Unspecified
        )
        Icon(
            painter = painterResource(
                if (rating >= 4) R.drawable.ic_star else R.drawable.ic_unfilled_star
            ),
            contentDescription = null,
            modifier = Modifier.size(14.dp),
            tint = Color.Unspecified
        )
        Icon(
            painter = painterResource(
                if (rating == 5) R.drawable.ic_star else R.drawable.ic_unfilled_star
            ),
            contentDescription = null,
            modifier = Modifier.size(14.dp),
            tint = Color.Unspecified
        )
    }
}
