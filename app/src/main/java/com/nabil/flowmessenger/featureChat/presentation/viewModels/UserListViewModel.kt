package com.nabil.flowmessenger.featureChat.presentation.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nabil.flowmessenger.core.data.UserRepositoryImpl
import com.nabil.flowmessenger.core.domain.UserRepository
import com.nabil.flowmessenger.core.domain.models.UserData
import com.nabil.flowmessenger.featureAuth.data.AuthManagerImp
import com.nabil.flowmessenger.featureAuth.domain.AuthManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn

class UserListViewModel(application: Application) : AndroidViewModel(application) {

    private val authManager: AuthManager = AuthManagerImp(application.applicationContext)
    private val userRepository: UserRepository = UserRepositoryImpl()



    val userList: StateFlow<List<UserData>> = userRepository.getUserList()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            emptyList()
        )

    private val _selectedUserData : MutableStateFlow<UserData?> = MutableStateFlow(null)
    val selectedUserData : StateFlow<UserData?> = _selectedUserData.asStateFlow()

    fun selectUser(userData : UserData?){
        _selectedUserData.value = userData
    }

    fun isLoggedIn(): Boolean {
        return authManager.getLoggedInUser() != null
    }

}