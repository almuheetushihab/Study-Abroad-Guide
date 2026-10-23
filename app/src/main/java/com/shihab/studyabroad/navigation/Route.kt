package com.shihab.studyabroad.navigation

import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable data object Splash : Route

    @Serializable data object Onboarding : Route

    @Serializable data object Auth : Route

    @Serializable data object TrackSelect : Route

    @Serializable data class ProfileSetup(val isEditMode: Boolean = false) : Route

    // Top Level Tabs / Tab Graphs
    @Serializable data object Home : Route

    @Serializable data object ExploreGraph : Route

    @Serializable data object Explore : Route

    @Serializable data object TrackGraph : Route

    @Serializable data object Track : Route

    @Serializable data object MeGraph : Route

    @Serializable data object Me : Route

    // Sub Destinations
    @Serializable data object Scholarships : Route

    @Serializable data class ScholarshipDetail(val id: String) : Route

    @Serializable data object CountryList : Route

    @Serializable data class CountryDetail(val id: String) : Route

    @Serializable data object CountryComparison : Route

    @Serializable data object UniversityList : Route

    @Serializable data class UniversityDetail(val id: String) : Route

    @Serializable data object Documents : Route

    @Serializable data object Saved : Route

    @Serializable data object Notifications : Route

    @Serializable data object Profile : Route

    @Serializable data object Disclaimer : Route

    @Serializable data object ThemePreview : Route
}
