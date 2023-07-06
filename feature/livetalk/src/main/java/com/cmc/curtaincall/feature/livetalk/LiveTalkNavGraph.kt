package com.cmc.curtaincall.feature.livetalk

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.cmc.curtaincall.core.base.CurtainCallDestination
import com.cmc.curtaincall.feature.livetalk.ui.LiveTalkDetailScreen
import com.cmc.curtaincall.feature.livetalk.ui.LiveTalkScreen

private const val LIVETALK_GRAPH = "livetalk_graph"
const val LIVETALK = "livetalk"
private const val LIVETALK_DETAIL = "livetalk_detail"

sealed interface LiveTalkDestination : CurtainCallDestination {
    object LiveTalk : LiveTalkDestination {
        override val route = LIVETALK
    }

    object Detail : LiveTalkDestination {
        override val route = LIVETALK_DETAIL
    }
}

fun NavGraphBuilder.livetalkNavGraph(navHostController: NavHostController) {
    navigation(startDestination = LiveTalkDestination.LiveTalk.route, route = LIVETALK_GRAPH) {
        composable(route = LiveTalkDestination.LiveTalk.route) {
            LiveTalkScreen {
                navHostController.navigate(LiveTalkDestination.Detail.route)
            }
        }

        composable(route = LiveTalkDestination.Detail.route) {
            LiveTalkDetailScreen()
        }
    }
}
