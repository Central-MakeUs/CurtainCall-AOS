package com.cmc.curtaincall

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cmc.curtaincall.core.base.CurtainCallDestination
import com.cmc.curtaincall.feature.auth.AuthDestination
import com.cmc.curtaincall.feature.auth.authNavGraph
import com.cmc.curtaincall.feature.home.navigation.HomeDestination
import com.cmc.curtaincall.feature.home.navigation.HomeNavHost
import com.cmc.curtaincall.ui.OnBoardingScreen
import com.cmc.curtaincall.ui.SplashScreen

private const val ROOT_GRAPH = "root_graph"
private const val SPLASH = "splash"
private const val ONBOARDING = "onboarding"

object Splash : CurtainCallDestination {
    override val route = SPLASH
}

object OnBoarding : CurtainCallDestination {
    override val route = ONBOARDING
}

@Composable
internal fun AppNavHost(navHostController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navHostController,
        startDestination = Splash.route,
        modifier = Modifier.fillMaxSize(),
        route = ROOT_GRAPH
    ) {
        composable(route = Splash.route) {
            SplashScreen(
                onNavigateOnBoarding = {
                    navHostController.navigate(OnBoarding.route) {
                        popUpTo(Splash.route) {
                            inclusive = true
                        }
                    }
                },
                onNavigateOnHome = {
                    navHostController.navigate(HomeDestination.Home.route) {
                        popUpTo(Splash.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(route = OnBoarding.route) {
            OnBoardingScreen(
                onNavigateLoginUp = {
                    navHostController.navigate(AuthDestination.Login.route) {
                        popUpTo(OnBoarding.route) {
                            inclusive = true
                        }
                    }
                },
                onNavigateHome = {
                    navHostController.navigate(HomeDestination.Home.route) {
                        popUpTo(OnBoarding.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(route = HomeDestination.Home.route) {
            HomeNavHost(
                onLogout = {
                    navHostController.navigate(AuthDestination.Login.route) {
                        popUpTo(AuthDestination.Login.route) {
                            inclusive = true
                        }
                    }
                },
                onDeleteMember = {
                    navHostController.navigate(AuthDestination.Login.route) {
                        popUpTo(AuthDestination.Login.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        authNavGraph(navHostController)
    }
}
