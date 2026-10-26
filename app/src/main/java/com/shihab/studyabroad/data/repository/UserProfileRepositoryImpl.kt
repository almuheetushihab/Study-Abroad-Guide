package com.shihab.studyabroad.data.repository

import com.shihab.studyabroad.domain.model.UserProfile
import com.shihab.studyabroad.domain.repository.UserPreferencesRepository
import com.shihab.studyabroad.domain.repository.UserProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserProfileRepositoryImpl @Inject constructor(private val userPreferencesRepository: UserPreferencesRepository) :
    UserProfileRepository {

    override fun getUserProfile(): Flow<UserProfile?> = userPreferencesRepository.userProfile

    override suspend fun updateUserProfile(profile: UserProfile) {
        userPreferencesRepository.saveUserProfile(profile)
    }
}
