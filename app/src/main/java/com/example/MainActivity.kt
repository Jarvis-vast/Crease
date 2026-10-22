package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ui.CricketViewModel
import com.example.ui.ScreenTab
import com.example.ui.components.CricketBottomNavigation
import com.example.ui.components.CricketTopAppBar
import com.example.ui.components.TopTabScrollRow
import com.example.ui.navigation.CreaseDestinations
import com.example.ui.screens.AnalyticsLabScreen
import com.example.ui.screens.IntegrationsScreen
import com.example.ui.screens.MatchCenterScreen
import com.example.ui.screens.OverviewScreen
import com.example.ui.screens.PlayersScreen
import com.example.ui.screens.ReportsScreen
import com.example.ui.screens.ScorecardDetailScreen
import com.example.ui.screens.SettingsAuditScreen
import com.example.ui.screens.TeamsTournamentsScreen
import com.example.ui.screens.UploadReviewScreen
import com.example.ui.theme.CricketNavyDark
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                CricketIntelligenceApp()
            }
        }
    }
}

@Composable
fun CricketIntelligenceApp(
    viewModel: CricketViewModel = viewModel()
) {
    val navController: NavHostController = rememberNavController()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Map current route to ScreenTab for synchronizing tab bar indicators
    val activeTab = when {
        currentRoute == CreaseDestinations.OVERVIEW -> ScreenTab.OVERVIEW
        currentRoute?.startsWith("scorecard") == true -> ScreenTab.SCORECARDS
        currentRoute == CreaseDestinations.PLAYERS -> ScreenTab.PLAYERS
        currentRoute == CreaseDestinations.MATCH_CENTER -> ScreenTab.MATCH_CENTER
        currentRoute == CreaseDestinations.TEAMS_TOURNAMENTS -> ScreenTab.TEAMS_TOURNAMENTS
        currentRoute == CreaseDestinations.ANALYTICS_LAB -> ScreenTab.ANALYTICS_LAB
        currentRoute == CreaseDestinations.UPLOAD_REVIEW -> ScreenTab.UPLOAD_REVIEW
        currentRoute == CreaseDestinations.REPORTS -> ScreenTab.REPORTS
        currentRoute == CreaseDestinations.INTEGRATIONS -> ScreenTab.INTEGRATIONS
        currentRoute == CreaseDestinations.SETTINGS_AUDIT -> ScreenTab.SETTINGS_AUDIT
        else -> ScreenTab.OVERVIEW
    }

    fun navigateToTab(tab: ScreenTab) {
        val targetRoute = when (tab) {
            ScreenTab.OVERVIEW -> CreaseDestinations.OVERVIEW
            ScreenTab.MATCH_CENTER -> CreaseDestinations.MATCH_CENTER
            ScreenTab.SCORECARDS -> CreaseDestinations.scorecardRoute(viewModel.selectedMatchId.value)
            ScreenTab.PLAYERS -> CreaseDestinations.PLAYERS
            ScreenTab.TEAMS_TOURNAMENTS -> CreaseDestinations.TEAMS_TOURNAMENTS
            ScreenTab.ANALYTICS_LAB -> CreaseDestinations.ANALYTICS_LAB
            ScreenTab.UPLOAD_REVIEW -> CreaseDestinations.UPLOAD_REVIEW
            ScreenTab.REPORTS -> CreaseDestinations.REPORTS
            ScreenTab.INTEGRATIONS -> CreaseDestinations.INTEGRATIONS
            ScreenTab.SETTINGS_AUDIT -> CreaseDestinations.SETTINGS_AUDIT
        }

        if (currentRoute != targetRoute) {
            navController.navigate(targetRoute) {
                popUpTo(CreaseDestinations.OVERVIEW) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        }
    }

    Scaffold(
        topBar = {
            Column {
                CricketTopAppBar(
                    searchQuery = searchQuery,
                    onSearchQueryChange = { viewModel.updateSearchQuery(it) },
                    onUploadClick = { navigateToTab(ScreenTab.UPLOAD_REVIEW) },
                    onBriefingClick = { navigateToTab(ScreenTab.INTEGRATIONS) }
                )
                TopTabScrollRow(
                    selectedTab = activeTab,
                    onTabSelected = { navigateToTab(it) }
                )
            }
        },
        bottomBar = {
            CricketBottomNavigation(
                selectedTab = activeTab,
                onTabSelected = { navigateToTab(it) }
            )
        },
        containerColor = CricketNavyDark
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(CricketNavyDark)
        ) {
            NavHost(
                navController = navController,
                startDestination = CreaseDestinations.OVERVIEW
            ) {
                composable(CreaseDestinations.OVERVIEW) {
                    OverviewScreen(
                        viewModel = viewModel,
                        onNavigateTab = { navigateToTab(it) },
                        onSelectMatch = { matchId ->
                            viewModel.selectMatch(matchId)
                            navController.navigate(CreaseDestinations.scorecardRoute(matchId))
                        },
                        onSelectPlayer = { playerId ->
                            viewModel.selectPlayer(playerId)
                            navController.navigate(CreaseDestinations.PLAYERS)
                        }
                    )
                }

                composable(CreaseDestinations.MATCH_CENTER) {
                    MatchCenterScreen(
                        viewModel = viewModel,
                        onSelectMatch = { matchId ->
                            viewModel.selectMatch(matchId)
                            navController.navigate(CreaseDestinations.scorecardRoute(matchId))
                        }
                    )
                }

                composable(
                    route = CreaseDestinations.SCORECARD,
                    arguments = listOf(
                        navArgument("matchId") {
                            type = NavType.StringType
                            nullable = true
                            defaultValue = null
                        }
                    )
                ) { backStackEntry ->
                    val matchIdArg = backStackEntry.arguments?.getString("matchId")
                    val effectiveMatchId = matchIdArg ?: viewModel.selectedMatchId.collectAsState().value

                    ScorecardDetailScreen(
                        viewModel = viewModel,
                        matchId = effectiveMatchId,
                        onBack = {
                            if (!navController.popBackStack()) {
                                navController.navigate(CreaseDestinations.MATCH_CENTER)
                            }
                        }
                    )
                }

                composable(CreaseDestinations.PLAYERS) {
                    PlayersScreen(viewModel = viewModel)
                }

                composable(CreaseDestinations.TEAMS_TOURNAMENTS) {
                    TeamsTournamentsScreen(viewModel = viewModel)
                }

                composable(CreaseDestinations.ANALYTICS_LAB) {
                    AnalyticsLabScreen(viewModel = viewModel)
                }

                composable(CreaseDestinations.UPLOAD_REVIEW) {
                    UploadReviewScreen(viewModel = viewModel)
                }

                composable(CreaseDestinations.REPORTS) {
                    ReportsScreen(viewModel = viewModel)
                }

                composable(CreaseDestinations.INTEGRATIONS) {
                    IntegrationsScreen(viewModel = viewModel)
                }

                composable(CreaseDestinations.SETTINGS_AUDIT) {
                    SettingsAuditScreen(viewModel = viewModel)
                }
            }
        }
    }
}
