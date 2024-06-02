package com.cmc.curtaincall.common.navigation.destination

import com.cmc.curtaincall.common.navigation.NavRouteLabel
import com.cmc.curtaincall.core.navigation.CurtainCallDestination

sealed class LiveTalkDestination : CurtainCallDestination {
    object LiveTalk : LiveTalkDestination() {
        override val route = NavRouteLabel.LIVTALK
    }
}
