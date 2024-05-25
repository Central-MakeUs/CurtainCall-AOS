package com.cmc.curtaincall.feature.home.navigation

import android.os.Build
import android.os.Bundle
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cmc.curtaincall.common.designsystem.component.navigation.CurtainCallNavigationBar
import com.cmc.curtaincall.common.navigation.NavGraphLabel
import com.cmc.curtaincall.common.navigation.destination.HomeDestination
import com.cmc.curtaincall.common.navigation.destination.MyPageDestination
import com.cmc.curtaincall.common.navigation.destination.PartyMemberDestination
import com.cmc.curtaincall.common.navigation.destination.ShowDestination
import com.cmc.curtaincall.domain.type.ReportType
import com.cmc.curtaincall.feature.home.HomeScreen
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
        bottomBar = {
            CurtainCallNavigationBar(
                navHostController = navHostController,
                bottomDestinations = listOf(
                    HomeDestination.Home,
                    ShowDestination.Search,
                    PartyMemberDestination.PartyMember,
                    MyPageDestination.MyPage
                )
            )
        }
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
                    onNavigateToPerformanceDetail = {
                        navHostController.navigate("${ShowDestination.Detail.route}/$it")
                    },
                    onNavigateToMyParty = {
                        navHostController.navigate(MyPageDestination.MyParty.route)
                    },
                    onNavigateToPartyDetail = { partyId, showName ->
                        navHostController.navigate("${PartyMemberDestination.Detail.route}/$partyId/$showName")
                    }
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
                onNavigateToReport = { id, type ->
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
                onNavigateToReport = { id, type ->
                    navHostController.navigate("${HomeDestination.Report.route}/$id/$type")
                }
            )
            mypageNavGraph(
                navHostController = navHostController,
                chatClient = chatClient,
                onLogout = onLogout,
                onDeleteMember = onDeleteMember
            )
        }
    }
}

private fun getReportType(bundle: Bundle?): ReportType? =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        bundle?.getSerializable(HomeDestination.Report.reportTypeArrg, ReportType::class.java)
    } else {
        bundle?.getSerializable(HomeDestination.Report.reportTypeArrg) as? ReportType
    }
