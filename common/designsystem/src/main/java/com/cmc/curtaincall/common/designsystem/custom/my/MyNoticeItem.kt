package com.cmc.curtaincall.common.designsystem.custom.my

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.cmc.curtaincall.common.designsystem.R
import com.cmc.curtaincall.common.designsystem.component.divider.HorizontalDivider
import com.cmc.curtaincall.common.designsystem.theme.Black
import com.cmc.curtaincall.common.designsystem.theme.CurtainCallTheme
import com.cmc.curtaincall.common.designsystem.theme.Grey5
import com.cmc.curtaincall.common.designsystem.theme.Grey8

@Composable
fun MyNoticeContent(
    title: String,
    createdAt: String,
    isLast: Boolean = false,
    isShowArrow: Boolean = true,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .padding(top = 20.dp)
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 18.dp),
                style = CurtainCallTheme.typography.body2.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (isShowArrow) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_right_pink),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = Black
                )
            }
        }
        Text(
            text = createdAt,
            modifier = Modifier.padding(top = 8.dp),
            style = CurtainCallTheme.typography.body3.copy(
                color = Grey5
            )
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
        )
        if (isLast.not()) {
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                background = Grey8
            )
        }
    }
}
