package com.cmc.curtaincall.design.ui.screen

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.cmc.curtaincall.common.designsystem.component.basic.TopAppBarWithBack
import com.cmc.curtaincall.common.designsystem.theme.Cetacean_Blue
import com.cmc.curtaincall.common.designsystem.theme.White
import com.cmc.curtaincall.common.designsystem.R
import com.cmc.curtaincall.common.designsystem.extensions.toSp
import com.cmc.curtaincall.common.designsystem.theme.spoqahansanseeo

@Composable
internal fun IllustrationScreen(
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBarWithBack(
                title = "Illustration",
                containerColor = Cetacean_Blue,
                contentColor = White,
                onClick = onBack
            )
        }
    ) { paddingValues ->
        IllustrationContent(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(Cetacean_Blue)
        )
    }
}

@Composable
private fun IllustrationContent(
    modifier: Modifier = Modifier
) {
    val illustrations = listOf(
        R.drawable.ic_welcome_anim to "커튼콜 환영",
        R.drawable.ic_dictionary to "알쏭달쏭 용어 사전",
        R.drawable.ic_ticket to "티켓팅 안내",
        R.drawable.ic_gift to "할인 꿀팁",
        R.drawable.ic_partymember_performance to "공연 관람",
        R.drawable.ic_partymember_food to "식사/카페",
        R.drawable.ic_partymember_etc to "기타"
    )
    Column(modifier) {
        LazyColumn(
            modifier = Modifier
                .padding(top = 10.dp)
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            items(illustrations) {
                IllustrationItem(
                    modifier = Modifier.fillMaxWidth(),
                    imageRes = it.first,
                    title = it.second
                )
            }
        }
    }
}

@Composable
private fun IllustrationItem(
    modifier: Modifier = Modifier,
    @DrawableRes imageRes: Int,
    title: String
) {
    Box(
        modifier = modifier
            .border(1.dp, White, RoundedCornerShape(10.dp))
            .padding(vertical = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                painter = painterResource(imageRes),
                contentDescription = null,
                tint = Color.Unspecified
            )
            Text(
                text = title,
                modifier = Modifier.padding(top = 10.dp),
                color = White,
                fontSize = 15.dp.toSp(),
                fontWeight = FontWeight.Medium,
                fontFamily = spoqahansanseeo
            )
        }
    }
}
