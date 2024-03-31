package com.cmc.curtaincall.feature.mypage.saveperformance

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cmc.curtaincall.common.designsystem.R
import com.cmc.curtaincall.common.designsystem.component.basic.TopAppBarWithBack
import com.cmc.curtaincall.common.designsystem.component.card.PerformanceDetailCard
import com.cmc.curtaincall.common.designsystem.extensions.toSp
import com.cmc.curtaincall.common.designsystem.theme.Arsenic
import com.cmc.curtaincall.common.designsystem.theme.Black_Coral
import com.cmc.curtaincall.common.designsystem.theme.Cetacean_Blue
import com.cmc.curtaincall.common.designsystem.theme.Chinese_Black
import com.cmc.curtaincall.common.designsystem.theme.Cultured
import com.cmc.curtaincall.common.designsystem.theme.Nero
import com.cmc.curtaincall.common.designsystem.theme.Roman_Silver
import com.cmc.curtaincall.common.designsystem.theme.White
import com.cmc.curtaincall.common.designsystem.theme.spoqahansanseeo
import com.cmc.curtaincall.domain.enums.ShowDay
import com.cmc.curtaincall.common.utility.extensions.toChangeDate
import com.cmc.curtaincall.common.utility.extensions.toRunningTime
import com.cmc.curtaincall.feature.mypage.MyPageViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MyPageSavedPerformanceScreen(
    myPageViewModel: MyPageViewModel = hiltViewModel(),
    onNavigateShowDetail: (String) -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBarWithBack(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                title = stringResource(R.string.mypage_saved_performance_list),
                containerColor = White,
                contentColor = Nero,
                onClick = onBack
            )
        }
    ) { paddingValues ->
        MyPageSavedPerformanceContent(
            myPageViewModel = myPageViewModel,
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(White),
            onNavigateShowDetail = onNavigateShowDetail
        )
    }
}

@Composable
private fun MyPageSavedPerformanceContent(
    myPageViewModel: MyPageViewModel,
    modifier: Modifier = Modifier,
    onNavigateShowDetail: (String) -> Unit
) {
    val myFavoriteItems by myPageViewModel.favoriteShowList.collectAsStateWithLifecycle()
    LazyColumn(
        modifier = modifier
            .padding(top = 2.dp, bottom = 16.dp)
            .padding(horizontal = 20.dp)
    ) {
        itemsIndexed(myFavoriteItems) { index, myFavoriteItem ->
            PerformanceDetailCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                imageUrl = myFavoriteItem.poster,
                painter = painterResource(R.drawable.ic_error_poster),
                title = myFavoriteItem.name,
                rate = if (myFavoriteItem.reviewCount == 0) 0.0f else (myFavoriteItem.reviewGradeSum / myFavoriteItem.reviewCount.toFloat()),
                numberOfTotal = myFavoriteItem.reviewCount,
                period = "${myFavoriteItem.startDate.toChangeDate()}-${myFavoriteItem.endDate.toChangeDate()}",
                runningTime = if (myFavoriteItem.runtime.isEmpty()) "해당 정보 없음" else "${myFavoriteItem.runtime.toRunningTime()}분",
                date = myFavoriteItem.showTimes.map {
                    when (it.dayOfWeek) {
                        ShowDay.Monday.dayOfWeek -> ShowDay.Monday
                        ShowDay.Tuesday.dayOfWeek -> ShowDay.Tuesday
                        ShowDay.Wednesday.dayOfWeek -> ShowDay.Wednesday
                        ShowDay.Thursday.dayOfWeek -> ShowDay.Thursday
                        ShowDay.Friday.dayOfWeek -> ShowDay.Friday
                        ShowDay.Saturday.dayOfWeek -> ShowDay.Saturday
                        else -> ShowDay.Sunday
                    }
                }.sortedBy { it.id }.toSet().joinToString(", ") { it.label },
                location = myFavoriteItem.facilityName,
                onClick = { onNavigateShowDetail(myFavoriteItem.id) },
                isFavorite = myFavoriteItem.favorite,
                onFavorite = {
                    myPageViewModel.requestFavoriteShow(myFavoriteItem.id)
                },
                onDisFavorite = {
                    myPageViewModel.deleteFavoriteShow(myFavoriteItem.id)
                }
            )
            if (index != myFavoriteItems.lastIndex) {
                Spacer(
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Cultured)
                )
            }
        }
    }
}

@Composable
private fun SavedPerformanceItem(
    modifier: Modifier = Modifier,
    title: String,
    rate: Float,
    numberOfRate: Int,
    description: String
) {
    Row(modifier) {
        Image(
            painter = painterResource(R.drawable.img_poster),
            contentDescription = null,
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(111 / 148f),
            contentScale = ContentScale.FillBounds
        )
        Column(
            modifier = Modifier
                .padding(start = 20.dp)
                .fillMaxWidth()
        ) {
            Row {
                Column(Modifier.weight(1f)) {
                    Text(
                        text = title,
                        color = Chinese_Black,
                        fontSize = 16.dp.toSp(),
                        fontWeight = FontWeight.Bold,
                        fontFamily = spoqahansanseeo
                    )
                    Row(
                        modifier = Modifier.padding(top = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_star),
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = Color.Unspecified
                        )
                        Text(
                            text = rate.toString(),
                            modifier = Modifier.padding(start = 2.dp),
                            color = Arsenic,
                            fontSize = 14.dp.toSp(),
                            fontWeight = FontWeight.Medium,
                            fontFamily = spoqahansanseeo
                        )
                        Text(
                            text = String.format("(%d)", numberOfRate),
                            modifier = Modifier.padding(start = 2.dp),
                            color = Roman_Silver,
                            fontSize = 14.dp.toSp(),
                            fontWeight = FontWeight.Medium,
                            fontFamily = spoqahansanseeo
                        )
                    }
                }
                IconButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(26.dp),
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = Cetacean_Blue
                    )
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_bookmark_sel),
                        contentDescription = null,
                        tint = Color.Unspecified
                    )
                }
            }
            Text(
                text = description,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 8.dp),
                color = Black_Coral,
                fontSize = 12.dp.toSp(),
                fontWeight = FontWeight.Normal,
                fontFamily = spoqahansanseeo,
                lineHeight = 18.dp.toSp(),
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
