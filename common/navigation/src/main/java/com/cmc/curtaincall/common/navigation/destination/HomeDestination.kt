package com.cmc.curtaincall.common.navigation.destination

import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.cmc.curtaincall.common.designsystem.R
import com.cmc.curtaincall.common.navigation.NavRouteLabel
import com.cmc.curtaincall.core.navigation.BottomDestination
import com.cmc.curtaincall.core.navigation.CurtainCallDestination
import com.cmc.curtaincall.domain.type.ReportType

private const val HOME_LABEL = "홈"
private const val REPORT_ID_ARG = "reportId"
private const val REPORT_TYPE_ARG = "reportType"

sealed class HomeDestination : CurtainCallDestination {

    object Home : HomeDestination(), BottomDestination {
        override val icon = R.drawable.ic_home
        override val selectIcon = R.drawable.ic_home_sel
        override val label = HOME_LABEL
        override val route = NavRouteLabel.HOME
    }

    object Report : HomeDestination() {
        override val route = NavRouteLabel.HOME_REPORT
        const val reportIdArg = REPORT_ID_ARG
        const val reportTypeArrg = REPORT_TYPE_ARG
        val routeWithArgs = "$route/{$reportIdArg}/{$reportTypeArrg}"
        val arguments = listOf(
            navArgument(reportIdArg) {
                type = NavType.IntType
            },
            navArgument(reportTypeArrg) {
                type = NavType.EnumType(ReportType::class.java)
            }
        )
    }
}
