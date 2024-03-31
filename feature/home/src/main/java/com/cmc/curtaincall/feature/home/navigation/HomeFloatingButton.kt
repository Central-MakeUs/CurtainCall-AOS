package com.cmc.curtaincall.feature.home.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.cmc.curtaincall.common.designsystem.R
import com.cmc.curtaincall.common.navigation.destination.HomeDestination
import com.cmc.curtaincall.common.navigation.destination.MyPageDestination
import com.cmc.curtaincall.common.navigation.destination.PartyMemberDestination
import com.cmc.curtaincall.common.navigation.destination.ShowDestination
import com.cmc.curtaincall.feature.livetalk.LiveTalkDestination

@Composable
fun HomeFloatingButton(navHostController: NavHostController) {
    val bottomDestinations = listOf(
        HomeDestination.Home,
        ShowDestination.Search,
        LiveTalkDestination.LiveTalk,
        PartyMemberDestination.PartyMember,
        MyPageDestination.MyPage
    )

    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val hasBottomNavigation = bottomDestinations.any { it.route == currentDestination?.route }

    if (hasBottomNavigation) {
        Image(
            painter = painterResource(R.drawable.ic_livetalk),
            contentDescription = LiveTalkDestination.LiveTalk.route,
            modifier = Modifier
                .offset(y = 60.dp)
                .clickable { navHostController.navigate(LiveTalkDestination.LiveTalk.route) }
        )
    }
}
