package com.nabil.flowmessenger.core.domain

import com.nabil.flowmessenger.core.domain.models.UserData
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun saveUserAuthToDatabase(userData: UserData)

    fun getUserList() : Flow<List<UserData>>

}