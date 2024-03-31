package com.cmc.curtaincall.feature.partymember

import android.os.Build
import android.os.Bundle
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.cmc.curtaincall.common.designsystem.R
import com.cmc.curtaincall.common.designsystem.component.card.PartyType
import com.cmc.curtaincall.common.navigation.NavGraphLabel
import com.cmc.curtaincall.common.navigation.destination.PartyMemberDestination
import com.cmc.curtaincall.core.navigation.BottomDestination
import com.cmc.curtaincall.core.navigation.CurtainCallDestination
import com.cmc.curtaincall.domain.type.ReportType
import com.cmc.curtaincall.feature.partymember.ui.PartyMemberScreen
import com.cmc.curtaincall.feature.partymember.ui.create.screen.PartyMemberCreateScreen
import com.cmc.curtaincall.feature.partymember.ui.detail.PartyMemberDetailScreen
import com.cmc.curtaincall.feature.partymember.ui.list.PartyMemberListScreen
import com.cmc.curtaincall.feature.partymember.ui.livetalk.PartyMemberLiveTalkScreen
import com.cmc.curtaincall.feature.partymember.ui.recruit.screen.PartyMemberRecruitScreen
import io.getstream.chat.android.client.ChatClient

private const val PARTYMEMBER_GRAPH = "partymember_graph"
const val PARTYMEMBER = "partymember"
private const val PARTYMEMBER_LABEL = "파티원"
private const val PARTYMEMBER_LIST = "partymember_list"
private const val PARTYMEMBER_DETAIL = "partymember_detail"
private const val PARTYMEMBER_CREATE = "partymember_create"
private const val PARTYMEMBER_LIVETALK = "partymember_livetalk"

sealed interface PartyMemberDestination2 : CurtainCallDestination {
    object PartyMember : PartyMemberDestination2, BottomDestination {
        override val route = PARTYMEMBER
        override val icon = R.drawable.ic_partymember
        override val selectIcon = R.drawable.ic_partymember_sel
        override val label = PARTYMEMBER_LABEL
    }

    object List : PartyMemberDestination2 {
        override val route = PARTYMEMBER_LIST
        const val typeArg = "type"
        val routeWithArgs = "$route/{$typeArg}"
        val arguments = listOf(
            navArgument(typeArg) {
                type = NavType.EnumType(PartyType::class.java)
            }
        )
    }

    object Detail : PartyMemberDestination2 {
        override val route = PARTYMEMBER_DETAIL
        const val typeArg = "type"
        const val partyIdArg = "partyId"
        const val myWritingArg = "myWriting"
        const val fromRecruitmentArg = "fromRecruitment"
        const val fromParticipationArg = "fromParticipation"
        val routeWithArgs = "$route?" +
            "$partyIdArg={$partyIdArg}&" +
            "$myWritingArg={$myWritingArg}&" +
            "$typeArg={$typeArg}&" +
            "$fromRecruitmentArg={$fromRecruitmentArg}&" +
            "$fromParticipationArg={$fromParticipationArg}"

        val arguments = listOf(
            navArgument(partyIdArg) {
                type = NavType.IntType
            },
            navArgument(myWritingArg) {
                type = NavType.BoolType
            },
            navArgument(typeArg) {
                type = NavType.EnumType(PartyType::class.java)
                defaultValue = PartyType.PERFORMANCE
            },
            navArgument(fromRecruitmentArg) {
                type = NavType.BoolType
                defaultValue = false
            },
            navArgument(fromParticipationArg) {
                type = NavType.BoolType
                defaultValue = false
            }
        )
    }

    object Create : PartyMemberDestination2 {
        override val route = PARTYMEMBER_CREATE
        const val typeArg = "type"
        val routeWithArgs = "$route/{$typeArg}"
        val arguments = listOf(
            navArgument(typeArg) {
                type = NavType.EnumType(PartyType::class.java)
            }
        )
    }

    object LiveTalk : PartyMemberDestination2 {
        override val route = PARTYMEMBER_LIVETALK
    }
}

fun NavGraphBuilder.partymemberNavGraph(
    navHostController: NavHostController,
    chatClient: ChatClient,
    onNavigateReport: (Int, ReportType) -> Unit
) {
    navigation(startDestination = PartyMemberDestination.PartyMember.route, route = NavGraphLabel.PARTY_MEMBER) {
        composable(route = PartyMemberDestination.PartyMember.route) {
            PartyMemberScreen {
                navHostController.navigate(PartyMemberDestination.Recruit.route)
            }
        }

        composable(route = PartyMemberDestination.Recruit.route) {
            PartyMemberRecruitScreen {
                navHostController.popBackStack()
            }
        }

        composable(
            route = PartyMemberDestination2.List.routeWithArgs,
            arguments = PartyMemberDestination2.List.arguments
        ) { entry ->
            val partyType: PartyType? = getPartyType(entry.arguments)
            if (partyType != null) {
                PartyMemberListScreen(
                    partyType = partyType,
                    onNavigateDetail = { partyType, partyId, myWriting ->
                        navHostController.navigate(
                            PartyMemberDestination2.Detail.route + "?" +
                                "${PartyMemberDestination2.Detail.partyIdArg}=$partyId" + "&" +
                                "${PartyMemberDestination2.Detail.typeArg}=$partyType" + "&" +
                                "${PartyMemberDestination2.Detail.myWritingArg}=$myWriting" + "&" +
                                "${PartyMemberDestination2.Detail.fromRecruitmentArg}=false" + "&" +
                                "${PartyMemberDestination2.Detail.fromParticipationArg}=false"
                        )
                    },
                    onNavigateCreate = { navHostController.navigate("${PartyMemberDestination2.Create.route}/$it") },
                    onBack = { navHostController.popBackStack() }
                )
            }
        }

        composable(
            route = PartyMemberDestination2.Detail.routeWithArgs,
            arguments = PartyMemberDestination2.Detail.arguments
        ) { entry ->
            val partyType: PartyType? = getPartyType(entry.arguments)
            val partyId = entry.arguments?.getInt(PartyMemberDestination2.Detail.partyIdArg) ?: Int.MIN_VALUE
            val myWriting = entry.arguments?.getBoolean(PartyMemberDestination2.Detail.myWritingArg) ?: false
            val fromRecruitment = entry.arguments?.getBoolean(PartyMemberDestination2.Detail.fromRecruitmentArg) ?: false
            val fromParticipation = entry.arguments?.getBoolean(PartyMemberDestination2.Detail.fromParticipationArg) ?: false
            if (partyType != null) {
                PartyMemberDetailScreen(
                    partyId = partyId,
                    myWriting = myWriting,
                    fromRecruitment = fromRecruitment,
                    fromParticipation = fromParticipation,
                    partyType = partyType,
                    onNavigateReport = onNavigateReport,
                    onNavigateLiveTalk = { navHostController.navigate(PartyMemberDestination2.LiveTalk.route) },
                    onBack = { navHostController.popBackStack() }
                )
            } else {
                PartyMemberDetailScreen(
                    partyId = partyId,
                    myWriting = myWriting,
                    fromRecruitment = fromRecruitment,
                    fromParticipation = fromParticipation,
                    partyType = PartyType.PERFORMANCE,
                    onNavigateReport = onNavigateReport,
                    onNavigateLiveTalk = { navHostController.navigate(PartyMemberDestination2.LiveTalk.route) },
                    onBack = { navHostController.popBackStack() }
                )
            }
        }

        composable(
            route = PartyMemberDestination2.Create.routeWithArgs,
            arguments = PartyMemberDestination2.Create.arguments
        ) { entry ->
            val partyType: PartyType? = getPartyType(entry.arguments)
            if (partyType != null) {
                PartyMemberCreateScreen(
                    partyType = partyType,
                    onBack = { navHostController.popBackStack() }
                )
            }
        }

        composable(route = PartyMemberDestination2.LiveTalk.route) { entry ->
            val parentEntry = remember(entry) { navHostController.getBackStackEntry(PartyMemberDestination2.Detail.routeWithArgs) }
            PartyMemberLiveTalkScreen(
                partyMemberDetailViewModel = hiltViewModel(parentEntry),
                chatClient = chatClient,
                onBack = { navHostController.popBackStack() }
            )
        }
    }
}

fun getPartyType(bundle: Bundle?): PartyType? =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        bundle?.getSerializable(PartyMemberDestination2.List.typeArg, PartyType::class.java)
    } else {
        bundle?.getSerializable(PartyMemberDestination2.List.typeArg) as? PartyType?
    }
