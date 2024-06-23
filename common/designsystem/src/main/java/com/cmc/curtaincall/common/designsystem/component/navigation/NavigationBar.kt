package com.cmc.curtaincall.common.designsystem.component.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.cmc.curtaincall.common.designsystem.component.divider.HorizontalDivider
import com.cmc.curtaincall.common.designsystem.theme.CurtainCallTheme
import com.cmc.curtaincall.common.designsystem.theme.Grey7
import com.cmc.curtaincall.common.designsystem.theme.Grey8
import com.cmc.curtaincall.common.designsystem.theme.White
import com.cmc.curtaincall.core.navigation.BottomDestination
import com.cmc.curtaincall.core.navigation.CurtainCallDestination

private val NavigationBarHeight = 72.dp

@Composable
fun CurtainCallNavigationBar(
    modifier: Modifier = Modifier,
    navHostController: NavHostController = rememberNavController(),
    bottomDestinations: List<BottomDestination> = listOf()
) {
    check(bottomDestinations.all { it is CurtainCallDestination })

    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val hasBottomNavigation = bottomDestinations.any { (it as CurtainCallDestination).route == currentDestination?.route }

    if (hasBottomNavigation) {
        Column(modifier) {
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                background = Grey8
            )
            NavigationBar(
                containerColor = White,
                modifier = Modifier.height(NavigationBarHeight)
            ) {
                bottomDestinations.forEach { bottomDestination ->
                    CurtainCallNavigationBarItem(
                        bottomDestination = (bottomDestination as CurtainCallDestination),
                        currentDestination = currentDestination,
                        navHostController = navHostController
                    )
                }
            }
        }
    }
}

@Composable
fun RowScope.CurtainCallNavigationBarItem(
    bottomDestination: CurtainCallDestination,
    currentDestination: NavDestination?,
    navHostController: NavHostController
) {
    require(bottomDestination is BottomDestination)
    val selected = bottomDestination.route == currentDestination?.route

    NavigationBarItem(
        selected = selected,
        onClick = {
            navHostController.navigate(bottomDestination.route) {
                popUpTo(bottomDestination.route) {
                    inclusive = false
                }
                launchSingleTop = true
            }
        },
        icon = {
            Icon(
                painter = painterResource(
                    if (selected) {
                        bottomDestination.selectIcon
                    } else {
                        bottomDestination.icon
                    }
                ),
                contentDescription = null,
                tint = Color.Unspecified
            )
        },
        modifier = Modifier.background(Color.Transparent),
        label = {
            if (bottomDestination.label.isNotEmpty()) {
                Text(
                    text = bottomDestination.label,
                    style = CurtainCallTheme.typography.body5.copy(
                        color = if (selected) {
                            CurtainCallTheme.colors.primary
                        } else {
                            Grey7
                        }
                    )
                )
            }
        },
        alwaysShowLabel = true,
        colors = NavigationBarItemDefaults.colors(indicatorColor = White)
    )
}
