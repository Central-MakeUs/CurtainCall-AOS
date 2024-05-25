package com.cmc.curtaincall.feature.mypage

import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.cmc.curtaincall.common.navigation.NavGraphLabel
import com.cmc.curtaincall.common.navigation.destination.MyPageDestination
import com.cmc.curtaincall.common.navigation.destination.PartyMemberDestination
import com.cmc.curtaincall.common.navigation.destination.ShowDestination
import com.cmc.curtaincall.feature.mypage.faq.MyPageFAQScreen
import com.cmc.curtaincall.feature.mypage.favorite.MyPageFavoriteScreen
import com.cmc.curtaincall.feature.mypage.notice.MyPageNoticeDetailScreen
import com.cmc.curtaincall.feature.mypage.notice.MyPageNoticeScreen
import com.cmc.curtaincall.feature.mypage.party.MyPagePartyScreen
import com.cmc.curtaincall.feature.mypage.profile.MyPageProfileScreen
import com.cmc.curtaincall.feature.mypage.setting.MyPageDeleteMemberScreen
import com.cmc.curtaincall.feature.mypage.setting.MyPageSettingScreen
import com.cmc.curtaincall.feature.mypage.writing.MyPageWritingScreen
import io.getstream.chat.android.client.ChatClient

fun NavGraphBuilder.mypageNavGraph(
    navHostController: NavHostController,
    chatClient: ChatClient,
    onLogout: () -> Unit,
    onDeleteMember: () -> Unit
) {
    navigation(startDestination = MyPageDestination.MyPage.route, NavGraphLabel.MYPAGE) {
        composable(MyPageDestination.MyPage.route) {
            MyPageScreen(
                onNavigateToProfile = {
                    navHostController.navigate(MyPageDestination.Profile.route)
                },
                onNavigateToMyParty = {
                    navHostController.navigate(MyPageDestination.MyParty.route)
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
                onNavigateToReviewCreate = { showId, reviewId, grade, content ->
                    navHostController.navigate(
                        "${ShowDestination.ReviewCreate.route}?" +
                            "${ShowDestination.ReviewCreate.showIdArg}=$showId&" +
                            "${ShowDestination.ReviewCreate.reviewIdArg}=$reviewId&" +
                            "${ShowDestination.ReviewCreate.gradeArg}=$grade&" +
                            "${ShowDestination.ReviewCreate.contentArg}=$content"
                    )
                },
                onBack = {
                    navHostController.popBackStack()
                }
            )
        }

        composable(route = MyPageDestination.Favorite.route) {
            MyPageFavoriteScreen(
                onNavigateToShowDetail = { showId ->
                    navHostController.navigate("${ShowDestination.Detail.route}/$showId")
                },
                onBack = {
                    navHostController.popBackStack()
                }
            )
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
                chatClient = chatClient,
                onLogout = onLogout,
                onNavigateDeleteMember = {
                    navHostController.navigate(MyPageDestination.DeleteMember.route)
                },
                onBack = {
                    navHostController.popBackStack()
                }
            )
        }

        composable(MyPageDestination.DeleteMember.route) {
            MyPageDeleteMemberScreen(
                onDeleteMember = onDeleteMember,
                onBack = { navHostController.popBackStack() }
            )
        }

        composable(MyPageDestination.MyParty.route) {
            MyPagePartyScreen(
                onNavigateToParty = {
                    navHostController.navigate(PartyMemberDestination.PartyMember.route) {
                        popUpTo(MyPageDestination.MyPage.route) {
                            inclusive = false
                        }
                    }
                },
                onNavigateToPartyDetail = { partyId, showName ->
                    navHostController.navigate("${PartyMemberDestination.Detail.route}/$partyId/$showName")
                },
                onNavigateToPartyEdit = { partyId, showName ->
                    navHostController.navigate("${PartyMemberDestination.Edit.route}/$partyId/$showName")
                },
                onBack = { navHostController.popBackStack() }
            )
        }
    }
}
