package com.shihab.studyabroad.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.shihab.studyabroad.R

enum class BottomBarItem(val route: Route, val icon: ImageVector, @StringRes val labelRes: Int) {
    HOME(Route.Home, Icons.Default.Home, R.string.nav_home),
    EXPLORE(Route.ExploreGraph, Icons.Default.Explore, R.string.nav_explore),
    TRACK(Route.TrackGraph, Icons.Default.AssignmentTurnedIn, R.string.nav_track),
    ME(Route.MeGraph, Icons.Default.Person, R.string.nav_me),
}
