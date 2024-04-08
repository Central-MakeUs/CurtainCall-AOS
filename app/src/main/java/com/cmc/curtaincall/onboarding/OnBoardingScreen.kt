package com.cmc.curtaincall.onboarding

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cmc.curtaincall.common.designsystem.R
import com.cmc.curtaincall.common.designsystem.component.basic.SystemUiStatusBar
import com.cmc.curtaincall.common.designsystem.component.buttons.common.CurtainCallFilledButton
import com.cmc.curtaincall.common.designsystem.component.indicators.pager.DynamicHorizontalPagerIndicator
import com.cmc.curtaincall.common.designsystem.theme.CurtainCallTheme
import kotlinx.coroutines.launch

private data class OnBoardingBannerInfo(
    val title: String,
    val description: String,
    val image: Painter
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun OnBoardingScreen(
    onNavigateLoginUp: () -> Unit = {}
) {
    val bannerInfos = listOf(
        OnBoardingBannerInfo(
            title = stringResource(R.string.onboarding_first_banner_title),
            description = stringResource(R.string.onboarding_first_banner_description),
            image = painterResource(R.drawable.ic_onboarding1)
        ),
        OnBoardingBannerInfo(
            title = stringResource(R.string.onboarding_second_banner_title),
            description = stringResource(R.string.onboarding_second_banner_description),
            image = painterResource(R.drawable.ic_onboarding2)
        ),
        OnBoardingBannerInfo(
            title = stringResource(R.string.onboarding_third_banner_title),
            description = stringResource(R.string.onboarding_third_banner_description),
            image = painterResource(R.drawable.ic_onboarding3)
        )
    )

    val pagerState = rememberPagerState { bannerInfos.size }
    SystemUiStatusBar(CurtainCallTheme.colors.primary)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CurtainCallTheme.colors.primary)
    ) {
        HorizontalPager(state = pagerState) { position ->
            OnBoardingContent(
                modifier = Modifier.fillMaxSize(),
                bannerInfo = bannerInfos[position],
                pagerState = pagerState,
                onNavigateLoginUp = onNavigateLoginUp
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun OnBoardingContent(
    modifier: Modifier = Modifier,
    onBoardingViewModel: OnBoardingViewModel = hiltViewModel(),
    bannerInfo: OnBoardingBannerInfo,
    pagerState: PagerState,
    onNavigateLoginUp: () -> Unit = {}
) {
    val coroutineScope = rememberCoroutineScope()
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.weight(112f))
        Image(
            painter = bannerInfo.image,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(360 / 230f),
            contentScale = ContentScale.FillBounds
        )
        Spacer(Modifier.weight(84f))
        Text(
            text = bannerInfo.title,
            style = CurtainCallTheme.typography.subTitle2.copy(
                color = CurtainCallTheme.colors.secondary
            ),
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.weight(20f))
        Text(
            text = bannerInfo.description,
            style = CurtainCallTheme.typography.body4.copy(
                color = CurtainCallTheme.colors.secondary,
                lineHeight = (20.8).sp
            ),
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.weight(60f))
        DynamicHorizontalPagerIndicator(
            pagerState = pagerState,
            pageCount = pagerState.pageCount,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            activeColor = CurtainCallTheme.colors.secondary,
            activeIndicatorWidth = 6.dp,
            activeIndicatorHeight = 6.dp,
            inactiveColor = CurtainCallTheme.colors.secondary.copy(0.3f),
            inactiveIndicatorWidth = 6.dp,
            spacing = 8.dp
        )
        Spacer(Modifier.weight(70f))
        CurtainCallFilledButton(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .padding(bottom = 30.dp)
                .fillMaxWidth()
                .height(51.dp),
            text = stringResource(
                if (pagerState.currentPage + 1 == pagerState.pageCount) {
                    R.string.onboarding_next_login
                } else {
                    R.string.next
                }
            ),
            containerColor = CurtainCallTheme.colors.secondary,
            contentColor = CurtainCallTheme.colors.primary,
            textStyle = CurtainCallTheme.typography.body2.copy(
                fontWeight = FontWeight.SemiBold
            ),
            onClick = {
                if (pagerState.currentPage + 1 == pagerState.pageCount) {
                    onBoardingViewModel.setIsFirstEntryOnBoarding()
                    onNavigateLoginUp()
                } else {
                    coroutineScope.launch {
                        pagerState.scrollToPage(pagerState.currentPage + 1)
                    }
                }
            }
        )
    }
}
