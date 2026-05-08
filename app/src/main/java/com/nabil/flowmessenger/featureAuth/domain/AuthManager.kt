package com.nabil.flowmessenger.featureAuth.domain

import com.nabil.flowmessenger.featureAuth.domain.models.AuthResult
import com.nabil.flowmessenger.core.domain.models.UserData

interface AuthManager {
    suspend fun signIn() : AuthResult
    suspend fun signOut()

    fun getLoggedInUser() : UserData?
}