package com.thrive.app.ui

import androidx.activity.compose.BackHandler
import android.app.Activity
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.thrive.feature.auth.SignInScreen
import com.thrive.feature.auth.SignUpScreen
import com.thrive.feature.challenge.ChallengeDetailScreen
import com.thrive.feature.challenge.ChallengeScreen
import com.thrive.feature.home.HomeScreen
import com.thrive.feature.home.ProfileScreen
import com.thrive.feature.timer.TimerScreen
import com.thrive.feature.summary.AnalyticsScreen
import com.thrive.feature.summary.LeaderboardScreen

sealed class Screen(val route: String) {
    object SignIn : Screen("sign_in")
    object SignUp : Screen("sign_up")
    object Onboarding : Screen("onboarding")
    object Home : Screen("home")
    object Timer : Screen("timer")
    object Challenge : Screen("challenge")
    object ChallengeDetail : Screen("challenge_detail/{challengeId}") {
        fun createRoute(id: String) = "challenge_detail/$id"
    }
    object Analytics : Screen("analytics")
    object Leaderboard : Screen("leaderboard")
    object Profile : Screen("profile")
}

private data class NavItem(val screen: Screen, val label: String, val iconText: String)

private val navItems = listOf(
    NavItem(Screen.Home, "Home", "🏠"),
    NavItem(Screen.Challenge, "Challenges", "⭐"),
    NavItem(Screen.Analytics, "Analytics", "📊"),
    NavItem(Screen.Leaderboard, "Rank", "🏆"),
)

@Composable
fun ThriveNavGraph(startDestination: String = Screen.SignIn.route) {
    val navController = rememberNavController()
    val backstackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backstackEntry?.destination?.route
    val showBottomBar = navItems.any { it.screen.route == currentRoute }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    navItems.forEach { item ->
                        NavigationBarItem(
                            selected = currentRoute == item.screen.route,
                            onClick = {
                                if (item.screen.route == currentRoute) return@NavigationBarItem

                                if (item.screen.route == Screen.Home.route) {
                                    navController.popBackStack(Screen.Home.route, inclusive = false)
                                } else {
                                    navController.navigate(item.screen.route) {
                                        popUpTo(Screen.Home.route) {
                                            saveState = true
                                            inclusive = false
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                            icon = { Text(item.iconText) },
                            label = { Text(item.label) }
                        )
                    }
                }
            }
        }
    ) { padding ->
        val context = LocalContext.current
        val resolvedStartDestination = remember(startDestination) {
            val extra = (context as? Activity)?.intent?.getStringExtra("start_destination")
            when (extra) {
                "home" -> Screen.Home.route
                "sign_in" -> Screen.SignIn.route
                else -> startDestination
            }
        }

        NavHost(
            navController = navController,
            startDestination = resolvedStartDestination,
            modifier = Modifier.padding(padding)
        ) {
            composable(Screen.SignIn.route) {
                SignInScreen(
                    onSignedIn = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.SignIn.route) { inclusive = true }
                        }
                    },
                    onNavigateToSignUp = {
                        navController.navigate(Screen.SignUp.route)
                    }
                )
            }
            composable(Screen.SignUp.route) {
                SignUpScreen(
                    onSignedUp = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.SignIn.route) { inclusive = true }
                        }
                    },
                    onBack = { navController.popBackStack() }
                )
            }
            composable(Screen.Home.route) {
                HomeScreen(
                    onStartSession = {
                        navController.navigate(Screen.Timer.route)
                    },
                    onOpenProfile = {
                        navController.navigate(Screen.Profile.route)
                    }
                )
            }
            composable(Screen.Profile.route) {
                ProfileScreen(
                    onBack = { navController.popBackStack() },
                    onLoggedOut = {
                        navController.navigate(Screen.SignIn.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
            composable(
                route = Screen.ChallengeDetail.route,
                arguments = listOf(navArgument("challengeId") { type = NavType.StringType })
            ) { backStack ->
                val challengeId = backStack.arguments?.getString("challengeId") ?: ""
                ChallengeDetailScreen(
                    challengeId = challengeId,
                    onBack = { navController.popBackStack() },
                    onStartSession = { navController.navigate(Screen.Timer.route) }
                )
            }
            composable(Screen.Challenge.route) {
                ChallengeScreen(
                    onChallengeClick = { challenge ->
                        navController.navigate(Screen.ChallengeDetail.createRoute(challenge.id.toString()))
                    }
                )
            }
            composable(Screen.Timer.route) {
                BackHandler {
                    navController.popBackStack()
                }
                TimerScreen()
            }
            composable(Screen.Analytics.route) { AnalyticsScreen() }
            composable(Screen.Leaderboard.route) { LeaderboardScreen() }
        }
    }
}

