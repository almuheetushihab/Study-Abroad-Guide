package com.shihab.studyabroad.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.shihab.studyabroad.feature.country.CountryScreen
import com.shihab.studyabroad.feature.debug.ThemePreviewScreen
import com.shihab.studyabroad.feature.documents.DocumentsScreen
import com.shihab.studyabroad.feature.explore.ExploreScreen
import com.shihab.studyabroad.feature.home.HomeRoute
import com.shihab.studyabroad.feature.profile.ProfileScreen
import com.shihab.studyabroad.feature.saved.SavedScreen
import com.shihab.studyabroad.feature.scholarship.ScholarshipScreen
import com.shihab.studyabroad.feature.track.TrackScreen
import com.shihab.studyabroad.feature.university.UniversityScreen

@Composable
fun AppNavHost(modifier: Modifier = Modifier, navController: NavHostController = rememberNavController()) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val rootTabDestinations = setOf(
        Route.Home::class,
        Route.Explore::class,
        Route.Track::class,
        Route.Me::class,
        Route.CountryList::class,
        Route.Scholarships::class,
    )

    val shouldShowBottomBar = currentDestination?.hierarchy?.any { destination ->
        rootTabDestinations.any { routeClass -> destination.hasRoute(routeClass) }
    } ?: true

    Scaffold(
        modifier = modifier,
        bottomBar = {
            if (shouldShowBottomBar) {
                NavigationBar {
                    BottomBarItem.entries.forEach { item ->
                        val isSelected = currentDestination?.hierarchy?.any {
                            it.hasRoute(item.route::class)
                        } == true

                        NavigationBarItem(
                            selected = isSelected,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = stringResource(item.labelRes),
                                )
                            },
                            label = {
                                Text(
                                    text = stringResource(item.labelRes),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                )
                            },
                        )
                    }
                }
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Route.Home,
            modifier = Modifier.padding(innerPadding),
        ) {
            // Tab 1: Home
            composable<Route.Home> {
                HomeRoute()
            }

            // Tab 2: Explore Graph
            navigation<Route.ExploreGraph>(startDestination = Route.Explore) {
                composable<Route.Explore> {
                    ExploreScreen(
                        onNavigateToCountries = { navController.navigate(Route.CountryList) },
                        onNavigateToScholarships = { navController.navigate(Route.Scholarships) },
                        onNavigateToUniversities = { navController.navigate(Route.UniversityDetail("all")) },
                    )
                }
                composable<Route.CountryList> {
                    CountryScreen()
                }
                composable<Route.Scholarships> {
                    ScholarshipScreen()
                }
                composable<Route.UniversityDetail> {
                    UniversityScreen()
                }
            }

            // Tab 3: Track Graph
            navigation<Route.TrackGraph>(startDestination = Route.Track) {
                composable<Route.Track> {
                    TrackScreen()
                }
                composable<Route.Documents> {
                    DocumentsScreen()
                }
                composable<Route.Saved> {
                    SavedScreen()
                }
            }

            // Tab 4: Me Graph
            navigation<Route.MeGraph>(startDestination = Route.Me) {
                composable<Route.Me> {
                    ProfileScreen(
                        onNavigateToThemePreview = { navController.navigate(Route.ThemePreview) },
                    )
                }
            }

            // Theme Preview Destination
            composable<Route.ThemePreview> {
                ThemePreviewScreen(
                    onBack = { navController.popBackStack() },
                )
            }
        }
    }
}
