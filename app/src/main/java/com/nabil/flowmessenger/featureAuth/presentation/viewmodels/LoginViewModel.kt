package com.nabil.flowmessenger.featureAuth.presentation.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nabil.flowmessenger.core.data.SessionManager
import com.nabil.flowmessenger.core.data.UserRepositoryImpl
import com.nabil.flowmessenger.core.domain.UserRepository
import com.nabil.flowmessenger.featureAuth.data.AuthManagerImp
import com.nabil.flowmessenger.featureAuth.domain.AuthManager
import com.nabil.flowmessenger.featureAuth.presentation.models.LoginState
import com.nabil.flowmessenger.featureAuth.presentation.models.LoginStateType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val application: Application
) : AndroidViewModel(application) {
    private val authManager: AuthManager = AuthManagerImp(application.applicationContext)
    private val userRepository : UserRepository = UserRepositoryImpl()
    private val _loginState: MutableStateFlow<LoginState> = MutableStateFlow(LoginState())
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    fun logIn() {

        viewModelScope.launch {

            _loginState.value = LoginState(LoginStateType.LOADING)

            val result = authManager.signIn()

            result.errorMessage?.let {
                _loginState.value = LoginState(loginStateType = LoginStateType.ERROR, errorMessage = it)
                return@launch
            }

            result.userData?.let {
                userRepository.saveUserAuthToDatabase(it)
            }

            _loginState.value = LoginState(loginStateType = LoginStateType.LOGGED_IN, userData = result.userData)
        }

    }

}