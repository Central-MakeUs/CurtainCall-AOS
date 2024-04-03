package com.cmc.curtaincall.feature.mypage

import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.cmc.curtaincall.common.navigation.NavGraphLabel
import com.cmc.curtaincall.common.navigation.destination.MyPageDestination
import com.cmc.curtaincall.common.navigation.destination.ShowDestination
import com.cmc.curtaincall.core.navigation.CurtainCallDestination
import com.cmc.curtaincall.feature.mypage.faq.MyPageFAQScreen
import com.cmc.curtaincall.feature.mypage.favorite.MyPageFavoriteScreen
import com.cmc.curtaincall.feature.mypage.notice.MyPageNoticeDetailScreen
import com.cmc.curtaincall.feature.mypage.notice.MyPageNoticeScreen
import com.cmc.curtaincall.feature.mypage.profile.MyPageProfileScreen
import com.cmc.curtaincall.feature.mypage.setting.MyPageDeleteMemberScreen
import com.cmc.curtaincall.feature.mypage.setting.MyPageSettingScreen
import com.cmc.curtaincall.feature.mypage.writing.MyPageWritingScreen

private const val MYPAGE_DELETE_MEMBER = "mypage_delete_member"
private const val MYPAGE_RECRUITMENT = "mypage_recruitment"
private const val MYPAGE_PARTICIPATION = "mypage_participantion"

sealed interface MyPageDestination2 : CurtainCallDestination {
    object DeleteMember : MyPageDestination2 {
        override val route = MYPAGE_DELETE_MEMBER
    }

    object Recruitment : MyPageDestination2 {
        override val route = MYPAGE_RECRUITMENT
    }

    object Participation : MyPageDestination2 {
        override val route = MYPAGE_PARTICIPATION
    }
}

fun NavGraphBuilder.mypageNavGraph(
    navHostController: NavHostController,
    onLogout: () -> Unit,
    onDeleteMember: () -> Unit
) {
    navigation(startDestination = MyPageDestination.MyPage.route, NavGraphLabel.MYPAGE) {
        composable(MyPageDestination.MyPage.route) {
            MyPageScreen(
                onNavigateToProfile = {
                    navHostController.navigate(MyPageDestination.Profile.route)
                },
                onNavigateToWriting = {
                    navHostController.navigate(MyPageDestination.Writing.route)
                },
                onNavigateToFavorite = {
                    navHostController.navigate(MyPageDestination.Favorite.route)
                },
                onNavigateToNotice = {
                    navHostController.navigate(MyPageDestination.Notice.route)
                },
                onNavigateToFAQ = {
                    navHostController.navigate(MyPageDestination.FAQ.route)
                },
                onNavigateToSetting = {
                    navHostController.navigate(MyPageDestination.Setting.route)
                },
                onNavigateRecruitment = {
                    navHostController.navigate(MyPageDestination2.Recruitment.route)
                },
                onNavigateParticipation = {
                    navHostController.navigate(MyPageDestination2.Participation.route)
                }
            )
        }

        composable(route = MyPageDestination.Profile.route) {
            MyPageProfileScreen {
                navHostController.popBackStack()
            }
        }

        composable(route = MyPageDestination.Writing.route) {
            MyPageWritingScreen(
                onNavigateToReviewCreate = { showId, reviewId ->
                    navHostController.navigate("${ShowDestination.ReviewCreate.route}/$showId/$reviewId")
                },
                onBack = {
                    navHostController.popBackStack()
                }
            )
        }

        composable(route = MyPageDestination.Favorite.route) {
            MyPageFavoriteScreen {
                navHostController.popBackStack()
            }
        }

        composable(MyPageDestination.Notice.route) {
            MyPageNoticeScreen(
                onNavigateToNoticeDetail = { noticeId ->
                    navHostController.navigate("${MyPageDestination.NoticeDetail.route}/$noticeId")
                },
                onBack = {
                    navHostController.popBackStack()
                }
            )
        }

        composable(
            route = MyPageDestination.NoticeDetail.routeWithArgs,
            arguments = MyPageDestination.NoticeDetail.arguments
        ) { entry ->
            val parentEntry = remember(entry) { navHostController.getBackStackEntry(MyPageDestination.Notice.route) }
            val noticeId = entry.arguments?.getInt(MyPageDestination.NoticeDetail.noticeIdArg)
            MyPageNoticeDetailScreen(
                myPageNoticeViewModel = hiltViewModel(parentEntry),
                noticeId = noticeId,
                onBack = { navHostController.popBackStack() }
            )
        }

        composable(route = MyPageDestination.FAQ.route) {
            MyPageFAQScreen {
                navHostController.popBackStack()
            }
        }

        composable(MyPageDestination.Setting.route) {
            MyPageSettingScreen(
                onLogout = onLogout,
                onNavigateDeleteMember = {
                    navHostController.navigate(MyPageDestination2.DeleteMember.route)
                },
                onBack = {
                    navHostController.popBackStack()
                }
            )
        }

        composable(MyPageDestination2.DeleteMember.route) {
            MyPageDeleteMemberScreen(
                onDeleteMember = onDeleteMember,
                onBack = { navHostController.popBackStack() }
            )
        }

        composable(MyPageDestination2.Recruitment.route) { entry ->
//            val parentEntry = remember(entry) { navHostController.getBackStackEntry(MyPageDestination2.MyPage.route) }
//            MyPageRecruitmentScreen(
//                myPageViewModel = hiltViewModel(parentEntry),
//                onNavigateRecruitmentDetail = { partyType, partyId ->
//                    navHostController.navigate(
//                        PartyMemberDestination2.Detail.route + "?" +
//                            "${PartyMemberDestination2.Detail.partyIdArg}=$partyId" + "&" +
//                            "${PartyMemberDestination2.Detail.typeArg}=$partyType" + "&" +
//                            "${PartyMemberDestination2.Detail.myWritingArg}=true" + "&" +
//                            "${PartyMemberDestination2.Detail.fromRecruitmentArg}=true" + "&" +
//                            "${PartyMemberDestination2.Detail.fromParticipationArg}=false"
//                    )
//                },
//                onNavigatePartyMember = {
//                    navHostController.navigate("${PartyMemberDestination2.List.route}/$it")
//                },
//                onBack = {
//                    navHostController.popBackStack()
//                }
//            )
        }

        composable(MyPageDestination2.Participation.route) { entry ->
//            val parentEntry = remember(entry) { navHostController.getBackStackEntry(MyPageDestination2.MyPage.route) }
//            MyPageParticipationScreen(
//                myPageViewModel = hiltViewModel(parentEntry),
//                onNavigateParticipationDetail = { partyType, partyId ->
//                    navHostController.navigate(
//                        PartyMemberDestination2.Detail.route + "?" +
//                            "${PartyMemberDestination2.Detail.partyIdArg}=$partyId" + "&" +
//                            "${PartyMemberDestination2.Detail.typeArg}=$partyType" + "&" +
//                            "${PartyMemberDestination2.Detail.myWritingArg}=false" + "&" +
//                            "${PartyMemberDestination2.Detail.fromRecruitmentArg}=false" + "&" +
//                            "${PartyMemberDestination2.Detail.fromParticipationArg}=true"
//                    )
//                },
//                onNavigatePartyMember = {
//                    navHostController.navigate("${PartyMemberDestination2.List.route}/$it")
//                },
//                onBack = {
//                    navHostController.popBackStack()
//                }
//            )
        }
    }
}
