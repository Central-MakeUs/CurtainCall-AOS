package com.cmc.curtaincall.feature.show

import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.cmc.curtaincall.common.navigation.NavGraphLabel
import com.cmc.curtaincall.common.navigation.destination.DEFAULT_REVIEW_ID
import com.cmc.curtaincall.common.navigation.destination.LiveTalkDestination
import com.cmc.curtaincall.common.navigation.destination.ShowDestination
import com.cmc.curtaincall.domain.type.ReportType
import com.cmc.curtaincall.feature.show.detail.ShowDetailScreen
import com.cmc.curtaincall.feature.show.lostproperty.ShowLostPropertyScreen
import com.cmc.curtaincall.feature.show.lostproperty.create.ShowLostPropertyCreateScreen
import com.cmc.curtaincall.feature.show.lostproperty.detail.ShowLostPropertyDetailScreen
import com.cmc.curtaincall.feature.show.review.ShowReviewScreen
import com.cmc.curtaincall.feature.show.review.create.ShowReviewCreateScreen
import com.cmc.curtaincall.feature.show.search.ShowSearchScreen

fun NavGraphBuilder.showNavGraph(
    navHostController: NavHostController,
    onNavigateToReport: (Int, ReportType) -> Unit
) {
    navigation(
        startDestination = ShowDestination.Search.route,
        route = NavGraphLabel.SHOW
    ) {
        composable(route = ShowDestination.Search.route) {
            ShowSearchScreen { showId ->
                navHostController.navigate("${ShowDestination.Detail.route}/$showId")
            }
        }

        composable(
            route = ShowDestination.Detail.routeWithArgs,
            arguments = ShowDestination.Detail.arguments
        ) { entry ->
            val showIdArg = entry.arguments?.getString(ShowDestination.Detail.showIdArg)
            ShowDetailScreen(
                showId = showIdArg,
                onNavigateToReview = { showId ->
                    navHostController.navigate("${ShowDestination.Review.route}/$showId")
                },
                onNavigateToReviewCreate = {
                    navHostController.navigate(
                        "${ShowDestination.ReviewCreate.route}?" +
                            "${ShowDestination.ReviewCreate.showIdArg}=$showIdArg&" +
                            "${ShowDestination.ReviewCreate.reviewIdArg}=$DEFAULT_REVIEW_ID"
                    )
                },
                onNavigateToLiveTalk = { showId, showName ->
                    navHostController.navigate("${LiveTalkDestination.LiveTalk.route}/$showId/$showName/${Int.MIN_VALUE}/null")
                },
                onBack = {
                    navHostController.popBackStack()
                }
            )
        }

        composable(
            route = ShowDestination.Review.routeWithArgs,
            arguments = ShowDestination.Review.arguments
        ) { entry ->
            val showIdArg = entry.arguments?.getString(ShowDestination.Review.showIdArg)
            ShowReviewScreen(
                showId = showIdArg,
                onNavigateToReviewCreate = { reviewId, grade, content ->
                    navHostController.navigate(
                        "${ShowDestination.ReviewCreate.route}?" +
                            "${ShowDestination.ReviewCreate.showIdArg}=$showIdArg&" +
                            "${ShowDestination.ReviewCreate.reviewIdArg}=$reviewId&" +
                            "${ShowDestination.ReviewCreate.gradeArg}=$grade&" +
                            "${ShowDestination.ReviewCreate.contentArg}=$content"
                    )
                },
                onNavigateToReport = onNavigateToReport,
                onBack = {
                    navHostController.popBackStack()
                }
            )
        }
        composable(
            route = ShowDestination.ReviewCreate.routeWithArgs,
            arguments = ShowDestination.ReviewCreate.arguments
        ) { entry ->
            val showIdArg = entry.arguments?.getString(ShowDestination.ReviewCreate.showIdArg)
            val reviewIdArg = entry.arguments?.getInt(ShowDestination.ReviewCreate.reviewIdArg)
            val gradeArg = entry.arguments?.getInt(ShowDestination.ReviewCreate.gradeArg)
            val contentArg = entry.arguments?.getString(ShowDestination.ReviewCreate.contentArg)

            if (navHostController.previousBackStackEntry?.destination?.route == ShowDestination.Review.routeWithArgs) {
                val showDetailEntry = remember(entry) { navHostController.getBackStackEntry(ShowDestination.Detail.routeWithArgs) }
                val reviewEntry = remember(entry) { navHostController.getBackStackEntry(ShowDestination.Review.routeWithArgs) }
                ShowReviewCreateScreen(
                    showDetailViewModel = hiltViewModel(showDetailEntry),
                    showReviewViewModel = hiltViewModel(reviewEntry),
                    showId = showIdArg,
                    reviewId = reviewIdArg,
                    grade = gradeArg,
                    content = contentArg,
                    onBack = { navHostController.popBackStack() }
                )
            } else if (navHostController.previousBackStackEntry?.destination?.route == ShowDestination.Detail.routeWithArgs) {
                val showDetailEntry = remember(entry) { navHostController.getBackStackEntry(ShowDestination.Detail.routeWithArgs) }
                ShowReviewCreateScreen(
                    showDetailViewModel = hiltViewModel(showDetailEntry),
                    showId = showIdArg,
                    reviewId = reviewIdArg,
                    grade = gradeArg,
                    content = contentArg,
                    onBack = { navHostController.popBackStack() }
                )
            } else {
                ShowReviewCreateScreen(
                    showId = showIdArg,
                    reviewId = reviewIdArg,
                    grade = gradeArg,
                    content = contentArg,
                    onBack = { navHostController.popBackStack() }
                )
            }
        }
        composable(
            route = ShowDestination.LostProperty.routeWithArgs,
            arguments = ShowDestination.LostProperty.arguments
        ) { entry ->
            val facilityIdArg = entry.arguments?.getString(ShowDestination.LostProperty.facilityIdArg)
            val facilityNameArg = entry.arguments?.getString(ShowDestination.LostProperty.facilityNameArg)
            ShowLostPropertyScreen(
                facilityId = facilityIdArg,
                facilityName = facilityNameArg,
                onNavigateToLostPropertyDetail = { lostPropertyId, fromCreateArg ->
                    navHostController.navigate("${ShowDestination.LostPropertyDetail.route}/$lostPropertyId/$fromCreateArg")
                },
                onNavigateToLostPropertyCreate = { facilityId, facilityName ->
                    navHostController.navigate(
                        "${ShowDestination.LostPropertyCreate.route}?" +
                            "${ShowDestination.LostPropertyCreate.facilityIdArg}=$facilityId&" +
                            "${ShowDestination.LostPropertyCreate.facilityNameArg}=$facilityName"
                    )
                },
                onBack = { navHostController.popBackStack() }
            )
        }
        composable(
            route = ShowDestination.LostPropertyDetail.routeWithArg,
            arguments = ShowDestination.LostPropertyDetail.arguments
        ) { entry ->
            val lostPropertyIdArg = entry.arguments?.getInt(ShowDestination.LostPropertyDetail.lostPropertyIdArg)
            val fromCreateArg = entry.arguments?.getBoolean(ShowDestination.LostPropertyDetail.fromCreateArg)
            ShowLostPropertyDetailScreen(
                lostPropertyId = lostPropertyIdArg,
                fromCreate = fromCreateArg,
                onNavigateReport = onNavigateToReport,
                onBack = { navHostController.popBackStack() }
            )
        }
        composable(
            route = ShowDestination.LostPropertyCreate.routeWithArg,
            arguments = ShowDestination.LostPropertyCreate.arguments
        ) { entry ->
            val lostPropertyIdArg = entry.arguments?.getInt(ShowDestination.LostPropertyCreate.lostPropertyIdArg)
            val facilityIdArg = entry.arguments?.getString(ShowDestination.LostPropertyCreate.facilityIdArg)
            val facilityNameArg = entry.arguments?.getString(ShowDestination.LostPropertyCreate.facilityNameArg)
            ShowLostPropertyCreateScreen(
                lostPropertyId = lostPropertyIdArg,
                facilityId = facilityIdArg,
                facilityName = facilityNameArg,
                onNavigateDetail = { lostPropertyId, fromCreateArg ->
                    navHostController.navigate("${ShowDestination.LostPropertyDetail.route}/$lostPropertyId/$fromCreateArg") {
                        navHostController.popBackStack(
                            route = ShowDestination.LostPropertyCreate.routeWithArg,
                            inclusive = true
                        )
                    }
                },
                onBack = { navHostController.popBackStack() }
            )
        }
    }
}
