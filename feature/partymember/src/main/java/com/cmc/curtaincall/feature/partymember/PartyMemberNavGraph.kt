package com.cmc.curtaincall.feature.partymember

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.cmc.curtaincall.common.navigation.NavGraphLabel
import com.cmc.curtaincall.common.navigation.destination.LiveTalkDestination
import com.cmc.curtaincall.common.navigation.destination.PartyMemberDestination
import com.cmc.curtaincall.domain.type.ReportType
import com.cmc.curtaincall.feature.partymember.detail.PartyMemberDetailScreen
import com.cmc.curtaincall.feature.partymember.edit.PartyMemberEditScreen
import com.cmc.curtaincall.feature.partymember.recruit.screen.PartyMemberRecruitScreen
import io.getstream.chat.android.client.ChatClient

fun NavGraphBuilder.partymemberNavGraph(
    navHostController: NavHostController,
    chatClient: ChatClient,
    onNavigateToReport: (Int, ReportType) -> Unit
) {
    navigation(startDestination = PartyMemberDestination.PartyMember.route, route = NavGraphLabel.PARTY_MEMBER) {
        composable(route = PartyMemberDestination.PartyMember.route) {
            PartyMemberScreen(
                onNavigateToDetail = { partyId, showName ->
                    navHostController.navigate("${PartyMemberDestination.Detail.route}/$partyId/$showName")
                },
                onNavigateToRecruit = {
                    navHostController.navigate(PartyMemberDestination.Recruit.route)
                }
            )
        }

        composable(route = PartyMemberDestination.Recruit.route) {
            PartyMemberRecruitScreen {
                navHostController.popBackStack()
            }
        }

        composable(
            route = PartyMemberDestination.Detail.routeWithArgs,
            arguments = PartyMemberDestination.Detail.arguments
        ) { entry ->
            val partyIdArg = entry.arguments?.getInt(PartyMemberDestination.Detail.partyIdArg)
            val showNameArg = entry.arguments?.getString(PartyMemberDestination.Detail.showNameArg)
            PartyMemberDetailScreen(
                partyId = partyIdArg,
                showName = showNameArg,
                onNavigateToEdit = { partyId, showName ->
                    navHostController.navigate("${PartyMemberDestination.Edit.route}/$partyId/$showName")
                },
                onNavigateToReport = onNavigateToReport,
                onNavigateToLiveTalk = { showId, showName, partyId, showAt ->
                    navHostController.navigate("${LiveTalkDestination.LiveTalk.route}/$showId/$showName/$partyId/$showAt")
                },
                onBack = {
                    navHostController.popBackStack()
                }
            )
        }

        composable(
            route = PartyMemberDestination.Edit.routeWithArgs,
            arguments = PartyMemberDestination.Edit.arguments
        ) { entry ->
            val partyIdArg = entry.arguments?.getInt(PartyMemberDestination.Edit.partyIdArg)
            val showNameArg = entry.arguments?.getString(PartyMemberDestination.Edit.showNameArg)
            PartyMemberEditScreen(
                partyId = partyIdArg,
                showName = showNameArg,
                onBack = {
                    navHostController.popBackStack()
                }
            )
        }
    }
}
