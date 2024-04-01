package com.cmc.curtaincall.feature.mypage

import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.cmc.curtaincall.common.designsystem.R
import com.cmc.curtaincall.common.navigation.NavGraphLabel
import com.cmc.curtaincall.common.navigation.destination.MyPageDestination
import com.cmc.curtaincall.common.navigation.destination.ShowDestination
import com.cmc.curtaincall.core.navigation.BottomDestination
import com.cmc.curtaincall.core.navigation.CurtainCallDestination
import com.cmc.curtaincall.feature.mypage.favorite.MyPageFavoriteScreen
import com.cmc.curtaincall.feature.mypage.notice.MyPageNoticeDetailScreen
import com.cmc.curtaincall.feature.mypage.notice.MyPageNoticeScreen
import com.cmc.curtaincall.feature.mypage.party.participation.MyPageParticipationScreen
import com.cmc.curtaincall.feature.mypage.party.recruitment.MyPageRecruitmentScreen
import com.cmc.curtaincall.feature.mypage.profile.MyPageProfileScreen
import com.cmc.curtaincall.feature.mypage.question.MyPageQuestionScreen
import com.cmc.curtaincall.feature.mypage.setting.MyPageDeleteMemberScreen
import com.cmc.curtaincall.feature.mypage.setting.MyPageSettingScreen
import com.cmc.curtaincall.feature.mypage.writing.MyPageWritingScreen
import com.cmc.curtaincall.feature.partymember.PartyMemberDestination2

private const val MYPAGE_GRAPH = "mypage_graph"
const val MYPAGE = "mypage"
private const val MYPAGE_LABEL = "MY"
private const val MYPAGE_SETTING = "mypage_setting"
private const val MYPAGE_DELETE_MEMBER = "mypage_delete_member"
private const val MYPAGE_NOTICE = "mypage_notice"
private const val MYPAGE_NOTICE_DETAIL = "mypage_notice_detail"
private const val MYPAGE_RECRUITMENT = "mypage_recruitment"
private const val MYPAGE_PARTICIPATION = "mypage_participantion"
private const val MYPAGE_QUESTIONS = "mypage_questions"

sealed interface MyPageDestination2 : CurtainCallDestination {
    object MyPage : MyPageDestination2, BottomDestination {
        override val route = MYPAGE
        override val icon = R.drawable.ic_my
        override val selectIcon = R.drawable.ic_my_sel
        override val label = MYPAGE_LABEL
    }

    object Setting : MyPageDestination2 {
        override val route = MYPAGE_SETTING
    }

    object DeleteMember : MyPageDestination2 {
        override val route = MYPAGE_DELETE_MEMBER
    }

    object Notice : MyPageDestination2 {
        override val route = MYPAGE_NOTICE
    }

    object NoticeDetail : MyPageDestination2 {
        override val route = MYPAGE_NOTICE_DETAIL
        const val noticeIdArg = "noticeId"
        val routeWithArgs = "$route/{$noticeIdArg}"
        val arguments = listOf(
            navArgument(noticeIdArg) {
                type = NavType.IntType
            }
        )
    }

    object Recruitment : MyPageDestination2 {
        override val route = MYPAGE_RECRUITMENT
    }

    object Participation : MyPageDestination2 {
        override val route = MYPAGE_PARTICIPATION
    }

    object Questions : MyPageDestination2 {
        override val route = MYPAGE_QUESTIONS
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
                onNavigateSetting = {
                    navHostController.navigate(MyPageDestination2.Setting.route)
                },
                onNavigateRecruitment = {
                    navHostController.navigate(MyPageDestination2.Recruitment.route)
                },
                onNavigateParticipation = {
                    navHostController.navigate(MyPageDestination2.Participation.route)
                },
                onNavigateAnnouncement = {
                    navHostController.navigate(MyPageDestination2.Notice.route)
                },
                onNavigateQuestion = {
                    navHostController.navigate(MyPageDestination2.Questions.route)
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

        composable(MyPageDestination2.Setting.route) {
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

        composable(MyPageDestination2.Notice.route) {
            MyPageNoticeScreen(
                onNavigateNoticeDetail = {
                    navHostController.navigate("${MyPageDestination2.NoticeDetail.route}/$it")
                },
                onBack = { navHostController.popBackStack() }
            )
        }

        composable(
            route = MyPageDestination2.NoticeDetail.routeWithArgs,
            arguments = MyPageDestination2.NoticeDetail.arguments
        ) { entry ->
            val parentEntry = remember(entry) { navHostController.getBackStackEntry(MyPageDestination2.Notice.route) }
            val noticeId = entry.arguments?.getInt(MyPageDestination2.NoticeDetail.noticeIdArg) ?: 0
            MyPageNoticeDetailScreen(
                myPageNoticeViewModel = hiltViewModel(parentEntry),
                noticeId = noticeId,
                onBack = { navHostController.popBackStack() }
            )
        }

        composable(MyPageDestination2.Recruitment.route) { entry ->
            val parentEntry = remember(entry) { navHostController.getBackStackEntry(MyPageDestination2.MyPage.route) }
            MyPageRecruitmentScreen(
                myPageViewModel = hiltViewModel(parentEntry),
                onNavigateRecruitmentDetail = { partyType, partyId ->
                    navHostController.navigate(
                        PartyMemberDestination2.Detail.route + "?" +
                            "${PartyMemberDestination2.Detail.partyIdArg}=$partyId" + "&" +
                            "${PartyMemberDestination2.Detail.typeArg}=$partyType" + "&" +
                            "${PartyMemberDestination2.Detail.myWritingArg}=true" + "&" +
                            "${PartyMemberDestination2.Detail.fromRecruitmentArg}=true" + "&" +
                            "${PartyMemberDestination2.Detail.fromParticipationArg}=false"
                    )
                },
                onNavigatePartyMember = {
                    navHostController.navigate("${PartyMemberDestination2.List.route}/$it")
                },
                onBack = {
                    navHostController.popBackStack()
                }
            )
        }

        composable(MyPageDestination2.Participation.route) { entry ->
            val parentEntry = remember(entry) { navHostController.getBackStackEntry(MyPageDestination2.MyPage.route) }
            MyPageParticipationScreen(
                myPageViewModel = hiltViewModel(parentEntry),
                onNavigateParticipationDetail = { partyType, partyId ->
                    navHostController.navigate(
                        PartyMemberDestination2.Detail.route + "?" +
                            "${PartyMemberDestination2.Detail.partyIdArg}=$partyId" + "&" +
                            "${PartyMemberDestination2.Detail.typeArg}=$partyType" + "&" +
                            "${PartyMemberDestination2.Detail.myWritingArg}=false" + "&" +
                            "${PartyMemberDestination2.Detail.fromRecruitmentArg}=false" + "&" +
                            "${PartyMemberDestination2.Detail.fromParticipationArg}=true"
                    )
                },
                onNavigatePartyMember = {
                    navHostController.navigate("${PartyMemberDestination2.List.route}/$it")
                },
                onBack = {
                    navHostController.popBackStack()
                }
            )
        }

        composable(MyPageDestination2.Questions.route) {
            MyPageQuestionScreen { navHostController.popBackStack() }
        }
    }
}
