package com.cmc.curtaincall.common.navigation.destination

import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.cmc.curtaincall.common.navigation.NavRouteLabel
import com.cmc.curtaincall.core.navigation.CurtainCallDestination

private const val SHOW_ID_ARG = "showId"
private const val SHOW_NAME_ARG = "showName"
private const val PARTY_ID_ARG = "partyId"
private const val PARTY_AT_ARG = "partyAt"

sealed class LiveTalkDestination : CurtainCallDestination {
    object LiveTalk : LiveTalkDestination() {
        override val route = NavRouteLabel.LIVTALK
        const val showIdArg = SHOW_ID_ARG
        const val showNameArg = SHOW_NAME_ARG
        const val partyIdArg = PARTY_ID_ARG
        const val partyAtArg = PARTY_AT_ARG
        val routeWithArgs = "$route/{$showIdArg}/{$showNameArg}/{$partyIdArg}/{$partyAtArg}"
        val arguments = listOf(
            navArgument(showIdArg) {
                type = NavType.StringType
            },
            navArgument(showNameArg) {
                type = NavType.StringType
            },
            navArgument(partyIdArg) {
                type = NavType.IntType
            },
            navArgument(partyAtArg) {
                type = NavType.StringType
            }
        )
    }
}
