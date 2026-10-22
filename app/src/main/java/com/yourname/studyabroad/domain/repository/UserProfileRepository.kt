package com.yourname.studyabroad.domain.repository

import com.yourname.studyabroad.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface UserProfileRepository {
    fun getUserProfile(): Flow<UserProfile?>

    suspend fun updateUserProfile(profile: UserProfile)
}
