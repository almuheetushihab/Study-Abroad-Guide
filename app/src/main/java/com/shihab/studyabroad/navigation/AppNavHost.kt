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
import androidx.navigation.toRoute
import com.shihab.studyabroad.BuildConfig
import com.shihab.studyabroad.feature.auth.LoginRoute
import com.shihab.studyabroad.feature.country.CountryComparisonRoute
import com.shihab.studyabroad.feature.country.CountryDetailRoute
import com.shihab.studyabroad.feature.country.CountryListRoute
import com.shihab.studyabroad.feature.debug.ThemePreviewScreen
import com.shihab.studyabroad.feature.documents.DocumentsRoute
import com.shihab.studyabroad.feature.explore.ExploreScreen
import com.shihab.studyabroad.feature.home.HomeRoute
import com.shihab.studyabroad.feature.notifications.NotificationsRoute
import com.shihab.studyabroad.feature.onboarding.OnboardingRoute
import com.shihab.studyabroad.feature.profile.DisclaimerScreen
import com.shihab.studyabroad.feature.profile.ProfileRoute
import com.shihab.studyabroad.feature.profilesetup.ProfileSetupRoute
import com.shihab.studyabroad.feature.saved.SavedRoute
import com.shihab.studyabroad.feature.scholarship.ScholarshipDetailRoute
import com.shihab.studyabroad.feature.scholarship.ScholarshipListRoute
import com.shihab.studyabroad.feature.splash.SplashRoute
import com.shihab.studyabroad.feature.track.TrackScreen
import com.shihab.studyabroad.feature.trackselect.TrackSelectRoute
import com.shihab.studyabroad.feature.university.UniversityDetailRoute
import com.shihab.studyabroad.feature.university.UniversityListRoute

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
        Route.UniversityList::class,
    )

    val shouldShowBottomBar = currentDestination?.hierarchy?.any { destination ->
        rootTabDestinations.any { routeClass -> destination.hasRoute(routeClass) }
    } ?: false

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
            startDestination = Route.Splash,
            modifier = Modifier.padding(innerPadding),
        ) {
            // Start / Onboarding Flow
            composable<Route.Splash> {
                SplashRoute(
                    onNavigateToDestination = { target ->
                        navController.navigate(target) {
                            popUpTo(Route.Splash) { inclusive = true }
                        }
                    },
                )
            }

            composable<Route.Onboarding> {
                OnboardingRoute(
                    onOnboardingComplete = {
                        navController.navigate(Route.Auth) {
                            popUpTo(Route.Onboarding) { inclusive = true }
                        }
                    },
                )
            }

            composable<Route.Auth> {
                LoginRoute(
                    onLoginSuccess = {
                        navController.navigate(Route.TrackSelect) {
                            popUpTo(Route.Auth) { inclusive = true }
                        }
                    },
                )
            }

            composable<Route.TrackSelect> {
                TrackSelectRoute(
                    onTrackSelectComplete = {
                        navController.navigate(Route.ProfileSetup(isEditMode = false)) {
                            popUpTo(Route.TrackSelect) { inclusive = true }
                        }
                    },
                )
            }

            composable<Route.ProfileSetup> { backStackEntry ->
                val args = backStackEntry.toRoute<Route.ProfileSetup>()
                ProfileSetupRoute(
                    isEditMode = args.isEditMode,
                    onProfileSetupComplete = {
                        if (args.isEditMode) {
                            navController.popBackStack()
                        } else {
                            navController.navigate(Route.Home) {
                                popUpTo(Route.ProfileSetup(isEditMode = false)) { inclusive = true }
                            }
                        }
                    },
                )
            }

            // Tab 1: Home
            composable<Route.Home> {
                HomeRoute(
                    onNavigateToScholarshipDetail = { id -> navController.navigate(Route.ScholarshipDetail(id)) },
                    onNavigateToCountryDetail = { id -> navController.navigate(Route.CountryDetail(id)) },
                )
            }

            // Tab 2: Explore Graph
            navigation<Route.ExploreGraph>(startDestination = Route.Explore) {
                composable<Route.Explore> {
                    ExploreScreen(
                        onNavigateToCountries = { navController.navigate(Route.CountryList) },
                        onNavigateToScholarships = { navController.navigate(Route.Scholarships) },
                        onNavigateToUniversities = { navController.navigate(Route.UniversityList) },
                        onNavigateToCountryComparison = { navController.navigate(Route.CountryComparison) },
                    )
                }

                composable<Route.CountryList> {
                    CountryListRoute(
                        onNavigateToCountryDetail = { id -> navController.navigate(Route.CountryDetail(id)) },
                        onNavigateToCountryComparison = { navController.navigate(Route.CountryComparison) },
                    )
                }

                composable<Route.CountryComparison> {
                    CountryComparisonRoute(
                        onBack = { navController.popBackStack() },
                    )
                }

                composable<Route.CountryDetail> {
                    CountryDetailRoute(
                        onBack = { navController.popBackStack() },
                        onNavigateToScholarshipDetail = { id -> navController.navigate(Route.ScholarshipDetail(id)) },
                        onNavigateToUniversityDetail = { id -> navController.navigate(Route.UniversityDetail(id)) },
                    )
                }

                composable<Route.UniversityList> {
                    UniversityListRoute(
                        onNavigateToUniversityDetail = { id -> navController.navigate(Route.UniversityDetail(id)) },
                    )
                }

                composable<Route.UniversityDetail> {
                    UniversityDetailRoute(
                        onBack = { navController.popBackStack() },
                    )
                }

                composable<Route.Scholarships> {
                    ScholarshipListRoute(
                        onNavigateToScholarshipDetail = { id -> navController.navigate(Route.ScholarshipDetail(id)) },
                    )
                }

                composable<Route.ScholarshipDetail> {
                    ScholarshipDetailRoute(
                        onBack = { navController.popBackStack() },
                    )
                }
            }

            // Tab 3: Track Graph
            navigation<Route.TrackGraph>(startDestination = Route.Track) {
                composable<Route.Track> {
                    TrackScreen(
                        onNavigateToCountryDetail = { id -> navController.navigate(Route.CountryDetail(id)) },
                        onNavigateToUniversityDetail = { id -> navController.navigate(Route.UniversityDetail(id)) },
                        onNavigateToScholarshipDetail = { id -> navController.navigate(Route.ScholarshipDetail(id)) },
                    )
                }
                composable<Route.Documents> {
                    DocumentsRoute()
                }
                composable<Route.Saved> {
                    SavedRoute(
                        onNavigateToCountryDetail = { id -> navController.navigate(Route.CountryDetail(id)) },
                        onNavigateToUniversityDetail = { id -> navController.navigate(Route.UniversityDetail(id)) },
                        onNavigateToScholarshipDetail = { id -> navController.navigate(Route.ScholarshipDetail(id)) },
                    )
                }
            }

            // Tab 4: Me Graph
            navigation<Route.MeGraph>(startDestination = Route.Me) {
                composable<Route.Me> {
                    ProfileRoute(
                        onNavigateToEditProfile = {
                            navController.navigate(Route.ProfileSetup(isEditMode = true))
                        },
                        onNavigateToNotificationSettings = {
                            navController.navigate(Route.Notifications)
                        },
                        onNavigateToDisclaimer = {
                            navController.navigate(Route.Disclaimer)
                        },
                        onNavigateToAuth = {
                            navController.navigate(Route.Auth) {
                                popUpTo(0) { inclusive = true }
                            }
                        },
                        onNavigateToThemePreview = if (BuildConfig.DEBUG) {
                            { navController.navigate(Route.ThemePreview) }
                        } else {
                            null
                        },
                    )
                }

                composable<Route.Notifications> {
                    NotificationsRoute(
                        onBack = { navController.popBackStack() },
                    )
                }

                composable<Route.Disclaimer> {
                    DisclaimerScreen(
                        onBack = { navController.popBackStack() },
                    )
                }
            }

            // Theme Preview Destination (Debug only)
            if (BuildConfig.DEBUG) {
                composable<Route.ThemePreview> {
                    ThemePreviewScreen(
                        onBack = { navController.popBackStack() },
                    )
                }
            }
        }
    }
}
