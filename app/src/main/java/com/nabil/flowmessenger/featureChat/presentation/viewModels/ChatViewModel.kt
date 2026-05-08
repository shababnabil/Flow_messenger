package com.nabil.flowmessenger.featureChat.presentation.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nabil.flowmessenger.core.data.SessionManager
import com.nabil.flowmessenger.core.domain.models.UserData
import com.nabil.flowmessenger.featureChat.data.MessageRepositoryImpl
import com.nabil.flowmessenger.featureChat.domain.MessageRepository
import com.nabil.flowmessenger.featureChat.domain.utils.ConversationIdGenerator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ChatViewModel(application: Application) : AndroidViewModel(application) {

    private val messageRepository: MessageRepository = MessageRepositoryImpl()
    private val _selectedUserData: MutableStateFlow<UserData?> = MutableStateFlow(null)

    private val _inputText : MutableStateFlow<String> = MutableStateFlow("")
    val inputText : StateFlow<String> = _inputText.asStateFlow()

    val messageList = _selectedUserData
        .filterNotNull()
        .flatMapLatest { user ->
            messageRepository.getMessages(
                ConversationIdGenerator.generateConversationId(
                    user.userId!!,
                    SessionManager.currentUser?.userId!!
                )
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun selectUser(userData: UserData?) {
        _selectedUserData.value = userData
    }

    fun setInputText(text : String){
        _inputText.value = text
    }
    fun sendMessage(message: String) {
        _selectedUserData.value?.userId?.let {
            viewModelScope.launch {
                messageRepository.sendMessage(
                    message,
                    ConversationIdGenerator.generateConversationId(
                        it,
                        SessionManager.currentUser?.userId!!
                    )
                )
            }
        }
    }

}