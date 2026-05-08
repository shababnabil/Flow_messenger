package com.nabil.flowmessenger.featureAuth.presentation.models

import com.nabil.flowmessenger.core.domain.models.UserData

data class LoginState(
    val loginStateType: LoginStateType = LoginStateType.LOGGED_OUT,
    val userData: UserData? = null,
    val errorMessage : String? = null
)

