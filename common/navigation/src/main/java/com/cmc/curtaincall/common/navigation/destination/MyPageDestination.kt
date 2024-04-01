package com.cmc.curtaincall.common.navigation.destination

import com.cmc.curtaincall.common.navigation.NavRouteLabel
import com.cmc.curtaincall.common.designsystem.R
import com.cmc.curtaincall.core.navigation.BottomDestination
import com.cmc.curtaincall.core.navigation.CurtainCallDestination

private const val MYPAGE_LABEL = "MY"

sealed class MyPageDestination : CurtainCallDestination {
    object MyPage : MyPageDestination(), BottomDestination {
        override val route = NavRouteLabel.MYPAGE
        override val icon = R.drawable.ic_my
        override val selectIcon = R.drawable.ic_my_sel
        override val label = MYPAGE_LABEL
    }

    object Profile : MyPageDestination() {
        override val route = NavRouteLabel.MYPAGE_PROFILE
    }

    object Writing : MyPageDestination() {
        override val route = NavRouteLabel.MYPAGE_WRITING
    }
}
