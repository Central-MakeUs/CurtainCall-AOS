package com.cmc.curtaincall.feature.mypage

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.cmc.curtaincall.common.designsystem.R
import com.cmc.curtaincall.common.designsystem.component.basic.TopAppBarOnlySetting
import com.cmc.curtaincall.common.designsystem.extensions.toSp
import com.cmc.curtaincall.common.designsystem.theme.Arsenic
import com.cmc.curtaincall.common.designsystem.theme.Black
import com.cmc.curtaincall.common.designsystem.theme.Bright_Gray
import com.cmc.curtaincall.common.designsystem.theme.Cetacean_Blue
import com.cmc.curtaincall.common.designsystem.theme.Cultured
import com.cmc.curtaincall.common.designsystem.theme.Me_Pink
import com.cmc.curtaincall.common.designsystem.theme.Nero
import com.cmc.curtaincall.common.designsystem.theme.Roman_Silver
import com.cmc.curtaincall.common.designsystem.theme.Silver_Sand
import com.cmc.curtaincall.common.designsystem.theme.White
import com.cmc.curtaincall.common.designsystem.theme.spoqahansanseeo
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPageScreen(
    myPageViewModel: MyPageViewModel = hiltViewModel(),
    onNavigateSetting: () -> Unit,
    onNavigateProfileEdit: (String?) -> Unit,
    onNavigateRecruitment: () -> Unit,
    onNavigateParticipation: () -> Unit,
    onNavigateSavedPerformance: () -> Unit,
    onNavigateWrite: () -> Unit,
    onNavigateAnnouncement: () -> Unit,
    onNavigateQuestion: () -> Unit
) {
    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(White)

    LaunchedEffect(Unit) {
        myPageViewModel.requestFavoriteShows()
    }

    Scaffold(
        topBar = {
            TopAppBarOnlySetting(
                containerColor = White,
                contentColor = Roman_Silver,
                onClick = { onNavigateSetting() }
            )
        }
    ) { paddingValues ->
        MyPageContent(
            myPageViewModel = myPageViewModel,
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(White),
            onNavigateProfileEdit = onNavigateProfileEdit,
            onNavigateRecruitment = onNavigateRecruitment,
            onNavigateParticipation = onNavigateParticipation,
            onNavigateSavedPerformance = onNavigateSavedPerformance,
            onNavigateWrite = onNavigateWrite,
            onNavigateAnnouncement = onNavigateAnnouncement,
            onNavigateQuestion = onNavigateQuestion
        )
    }
}

@Composable
private fun MyPageContent(
    myPageViewModel: MyPageViewModel,
    modifier: Modifier = Modifier,
    onNavigateProfileEdit: (String?) -> Unit,
    onNavigateRecruitment: () -> Unit,
    onNavigateParticipation: () -> Unit,
    onNavigateSavedPerformance: () -> Unit,
    onNavigateWrite: () -> Unit,
    onNavigateAnnouncement: () -> Unit,
    onNavigateQuestion: () -> Unit
) {
    val verticalScrollState = rememberScrollState()
    val myPageUiState by myPageViewModel.uiState.collectAsStateWithLifecycle()
    Column(modifier.verticalScroll(verticalScrollState)) {
        MyPageProfile(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(top = 6.dp),
            profileUrl = myPageUiState.memberInfoModel.imageUrl,
            nickname = myPageUiState.memberInfoModel.nickname,
            recruitingNum = myPageUiState.memberInfoModel.recruitingNum,
            participationNum = myPageUiState.memberInfoModel.participationNum,
            onNavigateProfileEdit = onNavigateProfileEdit,
            onNavigateRecruitment = onNavigateRecruitment,
            onNavigateParticipation = onNavigateParticipation
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 26.dp)
                .padding(horizontal = 20.dp)
        ) {
            MyPageContentItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                icon = painterResource(R.drawable.ic_edit_square),
                title = stringResource(R.string.mypage_my_writing),
                onClick = { onNavigateWrite() }
            )
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Cultured)
            )
            MyPageContentItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                icon = painterResource(R.drawable.ic_border_all),
                title = stringResource(R.string.mypage_saved_performance_list),
                onClick = onNavigateSavedPerformance
            )
        }
        Spacer(
            modifier = Modifier
                .padding(top = 17.dp)
                .fillMaxWidth()
                .height(12.dp)
                .background(Cultured)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 29.dp)
                .padding(horizontal = 20.dp)
        ) {
            MyPageInfoItem(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(R.string.mypage_announcement),
                onClick = { onNavigateAnnouncement() }
            )
            MyPageInfoItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 22.dp),
                title = stringResource(R.string.mypage_the_most_frequently_question),
                onClick = { onNavigateQuestion() }
            )
            Spacer(Modifier.height(60.dp))
        }
        MyPageFooter(
            modifier = Modifier
                .fillMaxWidth()
                .background(Cultured)
        )
    }
}

@Composable
private fun MyPageFooter(
    modifier: Modifier = Modifier
) {
    Column(modifier.padding(horizontal = 20.dp)) {
        Text(
            text = stringResource(R.string.mypage_curtaincall_customer_center),
            modifier = Modifier.padding(top = 24.dp),
            color = Arsenic,
            fontSize = 12.dp.toSp(),
            fontWeight = FontWeight.Medium,
            fontFamily = spoqahansanseeo
        )
        Text(
            text = stringResource(R.string.mypage_curtaincall_email),
            modifier = Modifier.padding(top = 2.dp),
            color = Arsenic,
            fontSize = 12.dp.toSp(),
            fontWeight = FontWeight.Medium,
            fontFamily = spoqahansanseeo
        )
        Spacer(
            modifier = Modifier
                .padding(vertical = 20.dp)
                .fillMaxWidth()
                .height(1.dp)
                .background(Bright_Gray)
        )
        Text(
            text = stringResource(R.string.mypage_source_of_performance_information),
            color = Roman_Silver,
            fontSize = 12.dp.toSp(),
            fontWeight = FontWeight.Normal,
            fontFamily = spoqahansanseeo,
            lineHeight = 17.dp.toSp()
        )
        Text(
            text = stringResource(R.string.mypage_copyright_information),
            modifier = Modifier.padding(
                top = 12.dp,
                bottom = 30.dp
            ),
            color = Roman_Silver,
            fontSize = 12.dp.toSp(),
            fontWeight = FontWeight.Normal,
            fontFamily = spoqahansanseeo,
            lineHeight = 17.dp.toSp()
        )
    }
}

@Composable
private fun MyPageInfoItem(
    modifier: Modifier = Modifier,
    title: String,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = modifier.clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            modifier = Modifier.weight(1f),
            color = Roman_Silver,
            fontSize = 16.dp.toSp(),
            fontWeight = FontWeight.Medium,
            fontFamily = spoqahansanseeo
        )
        Icon(
            painter = painterResource(R.drawable.ic_arrow_right_pink),
            contentDescription = null,
            modifier = Modifier.size(12.dp),
            tint = Arsenic
        )
    }
}

@Composable
private fun MyPageContentItem(
    modifier: Modifier = Modifier,
    icon: Painter,
    title: String,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = modifier.clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = Color.Unspecified
        )
        Text(
            text = title,
            modifier = Modifier.padding(start = 12.dp),
            color = Nero,
            fontSize = 16.dp.toSp(),
            fontWeight = FontWeight.Medium,
            fontFamily = spoqahansanseeo
        )
    }
}

@Composable
private fun MyPageProfile(
    modifier: Modifier = Modifier,
    profileUrl: String? = null,
    nickname: String,
    recruitingNum: Int,
    participationNum: Int,
    onNavigateProfileEdit: (String?) -> Unit,
    onNavigateRecruitment: () -> Unit,
    onNavigateParticipation: () -> Unit
) {
    Column(modifier) {
        Text(
            text = stringResource(R.string.mypage_title),
            color = Black,
            fontSize = 24.dp.toSp(),
            fontWeight = FontWeight.Bold,
            fontFamily = spoqahansanseeo
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 40.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(Modifier.size(92.dp)) {
                    AsyncImage(
                        model = profileUrl,
                        error = painterResource(R.drawable.ic_default_profile),
                        contentDescription = null,
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .size(80.dp)
                            .clip(RoundedCornerShape(26.dp))
                            .clickable { onNavigateProfileEdit(profileUrl) },
                        contentScale = ContentScale.FillBounds
                    )
                    IconButton(
                        onClick = { onNavigateProfileEdit(profileUrl) },
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .background(Cetacean_Blue, CircleShape)
                            .size(32.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_pen),
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = Color.Unspecified
                        )
                    }
                }
                Text(
                    text = "${nickname}님",
                    modifier = Modifier.padding(top = 10.dp),
                    color = Black,
                    fontSize = 18.dp.toSp(),
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = spoqahansanseeo
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Cultured, RoundedCornerShape(15.dp)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .padding(top = 18.dp, bottom = 14.dp)
                    .weight(1f)
                    .clickable { onNavigateRecruitment() },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.mypage_my_gathering_tab),
                    color = Nero,
                    fontSize = 16.dp.toSp(),
                    fontWeight = FontWeight.Bold,
                    fontFamily = spoqahansanseeo
                )
                Text(
                    text = recruitingNum.toString(),
                    modifier = Modifier.padding(top = 8.dp),
                    color = Me_Pink,
                    fontSize = 22.dp.toSp(),
                    fontWeight = FontWeight.Bold,
                    fontFamily = spoqahansanseeo
                )
            }
            Spacer(
                modifier = Modifier
                    .background(Silver_Sand)
                    .size(1.dp, 30.dp)
            )
            Column(
                modifier = Modifier
                    .padding(top = 18.dp, bottom = 14.dp)
                    .weight(1f)
                    .clickable { onNavigateParticipation() },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.mypage_my_participation_tab),
                    color = Nero,
                    fontSize = 16.dp.toSp(),
                    fontWeight = FontWeight.Bold,
                    fontFamily = spoqahansanseeo
                )
                Text(
                    text = participationNum.toString(),
                    modifier = Modifier.padding(top = 8.dp),
                    color = Me_Pink,
                    fontSize = 24.dp.toSp(),
                    fontWeight = FontWeight.Bold,
                    fontFamily = spoqahansanseeo
                )
            }
        }
    }
}
