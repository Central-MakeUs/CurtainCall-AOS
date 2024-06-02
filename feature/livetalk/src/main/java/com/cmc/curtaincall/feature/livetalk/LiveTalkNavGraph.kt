package com.cmc.curtaincall.feature.livetalk

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.cmc.curtaincall.common.navigation.NavGraphLabel
import com.cmc.curtaincall.common.navigation.destination.LiveTalkDestination
import com.cmc.curtaincall.core.navigation.CurtainCallDestination
import com.cmc.curtaincall.feature.livetalk.ui.LiveTalkScreen
import io.getstream.chat.android.client.ChatClient

// private const val LIVETALK_GRAPH = "livetalk_graph"
// const val LIVETALK = "livetalk"
// private const val LIVETALK_DETAIL = "livetalk_detail"

sealed interface LiveTalkDestination2 : CurtainCallDestination {
//    object LiveTalk : LiveTalkDestination2, BottomDestination {
//        override val route = LIVETALK
//        override val icon = R.drawable.ic_livetalk
//        override val selectIcon = R.drawable.ic_livetalk
//        override val label = ""
//    }
//
//    object Detail : LiveTalkDestination2 {
//        override val route = LIVETALK_DETAIL
//        const val showIdArg = "showId"
//        const val showNameArg = "showName"
//        const val showAtArg = "showAt"
//        val routeWithArg = "$route/{$showIdArg}/{$showNameArg}/{$showAtArg}"
//        val arguments = listOf(
//            navArgument(showIdArg) {
//                type = NavType.StringType
//            },
//            navArgument(showNameArg) {
//                type = NavType.StringType
//            },
//            navArgument(showAtArg) {
//                type = NavType.StringType
//            }
//        )
//    }
}

fun NavGraphBuilder.livetalkNavGraph(
    navHostController: NavHostController,
    chatClient: ChatClient
) {
    navigation(startDestination = LiveTalkDestination.LiveTalk.route, route = NavGraphLabel.LIVETALK) {
        composable(route = LiveTalkDestination.LiveTalk.route) {
            LiveTalkScreen(
                chatClient = chatClient,
                onBack = {
                    navHostController.popBackStack()
                }
            )
        }

//        composable(
//            route = LiveTalkDestination2.Detail.routeWithArg,
//            arguments = LiveTalkDestination2.Detail.arguments
//        ) { entry ->
//            val parentEntry = remember(entry) { navHostController.getBackStackEntry(LiveTalkDestination2.LiveTalk.route) }
//            val showId = entry.arguments?.getString(LiveTalkDestination2.Detail.showIdArg) ?: ""
//            val showName = entry.arguments?.getString(LiveTalkDestination2.Detail.showNameArg) ?: ""
//            val showAt = entry.arguments?.getString(LiveTalkDestination2.Detail.showAtArg) ?: ""
//            LiveTalkDetailScreen(
//                liveTalkViewModel = hiltViewModel(parentEntry),
//                chatClient = chatClient,
//                showId = showId,
//                showName = showName,
//                showAt = showAt,
//                onBack = { navHostController.popBackStack() }
//            )
//        }
    }
}
