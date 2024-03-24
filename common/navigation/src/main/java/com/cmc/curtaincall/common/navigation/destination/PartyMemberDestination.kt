package com.cmc.curtaincall.common.navigation.destination

import com.cmc.curtaincall.common.designsystem.R
import com.cmc.curtaincall.common.navigation.NavRouteLabel
import com.cmc.curtaincall.core.navigation.BottomDestination
import com.cmc.curtaincall.core.navigation.CurtainCallDestination

private const val PARTY_MEMBER_LABEL = "파티원"

sealed class PartyMemberDestination : CurtainCallDestination {

    object PartyMember : PartyMemberDestination(), BottomDestination {
        override val route = NavRouteLabel.PARTY_MEMBER
        override val icon = R.drawable.ic_partymember
        override val selectIcon = R.drawable.ic_partymember_sel
        override val label = PARTY_MEMBER_LABEL
    }

    object Recruit : PartyMemberDestination() {
        override val route = NavRouteLabel.PARTY_MEMBER_RECRUIT
    }
}
