package com.cmc.curtaincall.common.navigation.destination

import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.cmc.curtaincall.common.designsystem.R
import com.cmc.curtaincall.common.navigation.NavRouteLabel
import com.cmc.curtaincall.core.navigation.BottomDestination
import com.cmc.curtaincall.core.navigation.CurtainCallDestination

private const val MYPAGE_LABEL = "MY"
private const val NOTICE_ID_ARG = "noticeId"

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

    object Favorite : MyPageDestination() {
        override val route = NavRouteLabel.MYPAGE_FAVORITE
    }

    object Notice : MyPageDestination() {
        override val route = NavRouteLabel.MYPAGE_NOTICE
    }

    object NoticeDetail : MyPageDestination() {
        override val route = NavRouteLabel.MYPAGE_NOTICE_DETAIL
        const val noticeIdArg = NOTICE_ID_ARG
        val routeWithArgs = "$route/{$noticeIdArg}"
        val arguments = listOf(
            navArgument(noticeIdArg) {
                type = NavType.IntType
            }
        )
    }

    object FAQ : MyPageDestination() {
        override val route = NavRouteLabel.MYPAGE_FAQ
    }

    object Setting : MyPageDestination() {
        override val route = NavRouteLabel.MYPAGE_SETTING
    }
}
