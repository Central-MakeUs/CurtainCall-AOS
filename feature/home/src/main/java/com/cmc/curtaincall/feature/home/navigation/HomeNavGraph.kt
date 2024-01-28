package com.cmc.curtaincall.feature.home.navigation

import android.os.Build
import android.os.Bundle
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cmc.curtaincall.common.navigation.NavGraphLabel
import com.cmc.curtaincall.common.navigation.destination.HomeDestination
import com.cmc.curtaincall.common.navigation.destination.ShowDestination
import com.cmc.curtaincall.domain.type.HomeGuideMenu
import com.cmc.curtaincall.domain.type.ReportType
import com.cmc.curtaincall.feature.home.HomeScreen
import com.cmc.curtaincall.feature.home.guide.HomeGuideScreen
import com.cmc.curtaincall.feature.home.report.HomeReportScreen
import com.cmc.curtaincall.feature.livetalk.livetalkNavGraph
import com.cmc.curtaincall.feature.mypage.mypageNavGraph
import com.cmc.curtaincall.feature.partymember.partymemberNavGraph
import com.cmc.curtaincall.feature.show.showNavGraph
import io.getstream.chat.android.client.ChatClient

@Composable
fun HomeNavHost(
    navHostController: NavHostController = rememberNavController(),
    chatClient: ChatClient,
    onLogout: () -> Unit = {},
    onDeleteMember: () -> Unit = {}
) {
    Scaffold(
        bottomBar = { HomeBottomBar(navHostController) },
        floatingActionButton = { HomeFloatingButton(navHostController) },
        floatingActionButtonPosition = FabPosition.Center
    ) { innerPadding ->
        NavHost(
            navController = navHostController,
            startDestination = HomeDestination.Home.route,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            route = NavGraphLabel.HOME
        ) {
            composable(route = HomeDestination.Home.route) {
                HomeScreen(
                    chatClient = chatClient,
                    onNavigatePerformanceDetail = {
                        navHostController.navigate("${ShowDestination.Detail.route}/$it")
                    }
                )
            }
            composable(
                route = HomeDestination.Guide.routeWithArgs,
                arguments = HomeDestination.Guide.arguments
            ) { entry ->
                val homeGuideMenu: HomeGuideMenu? = getGuideMenu(entry.arguments)
                HomeGuideScreen(
                    homeGuideMenu = homeGuideMenu,
                    onBack = { navHostController.popBackStack() }
                )
            }

            composable(
                route = HomeDestination.Report.routeWithArgs,
                arguments = HomeDestination.Report.arguments
            ) { entry ->
                val reportId = entry.arguments?.getInt(HomeDestination.Report.reportIdArg)
                val reportType: ReportType? = getReportType(entry.arguments)
                HomeReportScreen(
                    reportId = reportId,
                    reportType = reportType,
                    onNavigateHome = {
                        navHostController.navigate(HomeDestination.Home.route) {
                            popUpTo(HomeDestination.Home.route) {
                                inclusive = false
                            }
                            launchSingleTop = true
                        }
                    },
                    onBack = { navHostController.popBackStack() }
                )
            }

            showNavGraph(
                navHostController = navHostController,
                onNavigateReport = { id, type ->
                    navHostController.navigate("${HomeDestination.Report.route}/$id/$type")
                }
            )
            livetalkNavGraph(
                navHostController = navHostController,
                chatClient = chatClient
            )
            partymemberNavGraph(
                navHostController = navHostController,
                chatClient = chatClient,
                onNavigateReport = { id, type ->
                    navHostController.navigate("${HomeDestination.Report.route}/$id/$type")
                }
            )
            mypageNavGraph(
                navHostController = navHostController,
                onLogout = onLogout,
                onDeleteMember = onDeleteMember
            )
        }
    }
}

private fun getGuideMenu(bundle: Bundle?): HomeGuideMenu? =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        bundle?.getSerializable(HomeDestination.Guide.guideMenuArg, HomeGuideMenu::class.java)
    } else {
        bundle?.getSerializable(HomeDestination.Guide.guideMenuArg) as? HomeGuideMenu
    }

private fun getReportType(bundle: Bundle?): ReportType? =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        bundle?.getSerializable(HomeDestination.Report.reportTypeArrg, ReportType::class.java)
    } else {
        bundle?.getSerializable(HomeDestination.Report.reportTypeArrg) as? ReportType
    }
