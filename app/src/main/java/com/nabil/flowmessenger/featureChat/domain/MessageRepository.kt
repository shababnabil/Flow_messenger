package com.nabil.flowmessenger.featureChat.domain

import com.nabil.flowmessenger.featureChat.domain.models.MessageItem
import com.nabil.flowmessenger.featureChat.domain.models.MessageResult
import kotlinx.coroutines.flow.Flow

interface MessageRepository {

    fun getMessages(conversionId : String) : Flow<List<MessageItem>>

    suspend fun sendMessage(message: String, conversionId : String) : MessageResult

}