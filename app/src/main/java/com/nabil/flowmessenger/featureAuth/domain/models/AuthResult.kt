package com.nabil.flowmessenger.featureAuth.domain.models

import com.nabil.flowmessenger.core.domain.models.UserData

data class AuthResult(
    val userData: UserData? = null,
    val errorMessage : String? = null
)