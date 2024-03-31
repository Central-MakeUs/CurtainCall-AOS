package com.cmc.curtaincall.common.designsystem.component.basic

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.cmc.curtaincall.common.designsystem.R
import com.cmc.curtaincall.common.designsystem.theme.Bright_Grey
import com.cmc.curtaincall.common.designsystem.theme.CurtainCallTheme

@Composable
fun CurtainCallSnackbarHost(
    snackbarHostState: SnackbarHostState,
    content: @Composable BoxScope.(SnackbarData) -> Unit
) {
    SnackbarHost(
        hostState = snackbarHostState
    ) { snackbarData ->
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            content(snackbarData)
        }
    }
}

@Composable
fun BoxScope.CurtainCallSnackbar(
    modifier: Modifier = Modifier,
    snackbarData: SnackbarData,
    painter: Painter = painterResource(R.drawable.ic_complete_green)
) {
    Box(
        modifier = modifier
            .padding(bottom = 30.dp)
            .fillMaxWidth()
            .height(48.dp)
            .padding(horizontal = 20.dp)
            .background(Bright_Grey.copy(0.9f), RoundedCornerShape(12.dp))
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painter,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = Color.Unspecified
            )
            Text(
                text = snackbarData.visuals.message,
                style = CurtainCallTheme.typography.body3.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = CurtainCallTheme.colors.background
                ),
                modifier = Modifier.padding(start = 10.dp)
            )
        }
    }
}
