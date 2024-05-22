package com.cmc.curtaincall.feature.auth

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.cmc.curtaincall.common.navigation.NavGraphLabel
import com.cmc.curtaincall.common.navigation.destination.AuthDestination
import com.cmc.curtaincall.common.navigation.destination.HomeDestination
import com.cmc.curtaincall.feature.auth.login.LoginScreen
import com.cmc.curtaincall.feature.auth.signup.input.SignUpInputScreen
import com.cmc.curtaincall.feature.auth.signup.terms.SignUpTermsScreen
import io.getstream.chat.android.client.ChatClient

fun NavGraphBuilder.authNavGraph(
    navHostController: NavHostController,
    chatClient: ChatClient
) {
    navigation(
        startDestination = AuthDestination.Login.route,
        route = NavGraphLabel.AUTH
    ) {
        composable(route = AuthDestination.Login.route) {
            LoginScreen(
                chatClient = chatClient,
                onNavigateSignUpTerms = {
                    navHostController.navigate(AuthDestination.SignUpTerms.route)
                },
                onNavigateHome = {
                    navHostController.navigate(HomeDestination.Home.route) {
                        popUpTo(AuthDestination.Login.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(route = AuthDestination.SignUpTerms.route) {
            SignUpTermsScreen(
                onNavigateToSignUpInput = {
                    navHostController.navigate(AuthDestination.SignUpInput.route)
                },
                onBack = {
                    navHostController.popBackStack()
                }
            )
        }

        composable(route = AuthDestination.SignUpInput.route) {
            SignUpInputScreen(
                chatClient = chatClient,
                onNavigateToHome = {
                    navHostController.navigate(HomeDestination.Home.route) {
                        popUpTo(AuthDestination.Login.route) {
                            inclusive = true
                        }
                    }
                },
                onBack = {
                    navHostController.popBackStack()
                }
            )
        }
    }
}
