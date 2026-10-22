package com.shihab.studyabroad.data.repository

import com.shihab.studyabroad.domain.model.UserProfile
import com.shihab.studyabroad.domain.repository.UserProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeUserProfileRepository @Inject constructor() : UserProfileRepository {

    private val profileFlow = MutableStateFlow<UserProfile?>(SampleData.sampleUserProfile)

    override fun getUserProfile(): Flow<UserProfile?> = profileFlow.asStateFlow()

    override suspend fun updateUserProfile(profile: UserProfile) {
        profileFlow.value = profile
    }
}
