package com.cmc.curtaincall

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cmc.curtaincall.common.navigation.NavGraphLabel
import com.cmc.curtaincall.common.navigation.destination.AuthDestination
import com.cmc.curtaincall.common.navigation.destination.HomeDestination
import com.cmc.curtaincall.common.navigation.destination.RootDestination
import com.cmc.curtaincall.feature.auth.authNavGraph
import com.cmc.curtaincall.feature.home.navigation.HomeNavHost
import com.cmc.curtaincall.onboarding.OnBoardingScreen
import com.cmc.curtaincall.splash.SplashScreen
import io.getstream.chat.android.client.ChatClient

@Composable
fun AppNavHost(
    navHostController: NavHostController = rememberNavController(),
    chatClient: ChatClient
) {
    NavHost(
        navController = navHostController,
        startDestination = RootDestination.Splash.route,
        modifier = Modifier.fillMaxSize(),
        route = NavGraphLabel.ROOT
    ) {
        composable(route = RootDestination.Splash.route) {
            SplashScreen(
                onNavigateOnBoarding = {
                    navHostController.navigate(RootDestination.OnBoarding.route) {
                        popUpTo(RootDestination.Splash.route) {
                            inclusive = true
                        }
                    }
                },
                onNavigateOnLogin = {
                    navHostController.navigate(AuthDestination.Login.route) {
                        popUpTo(RootDestination.Splash.route) {
                            inclusive = true
                        }
                    }
                },
                onNavigateOnHome = {
                    navHostController.navigate(HomeDestination.Home.route) {
                        popUpTo(RootDestination.Splash.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(route = RootDestination.OnBoarding.route) {
            OnBoardingScreen {
                navHostController.navigate(AuthDestination.Login.route) {
                    popUpTo(RootDestination.OnBoarding.route) {
                        inclusive = true
                    }
                }
            }
        }

        composable(route = HomeDestination.Home.route) {
            HomeNavHost(
                chatClient = chatClient,
                onLogout = {
                    navHostController.navigate(AuthDestination.Login.route) {
                        popUpTo(HomeDestination.Home.route) {
                            inclusive = true
                        }
                    }
                },
                onDeleteMember = {
                    navHostController.navigate(AuthDestination.Login.route) {
                        popUpTo(HomeDestination.Home.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        authNavGraph(navHostController)
    }
}
