package com.nabil.flowmessenger.featureChat.domain.utils

object ConversationIdGenerator {

    fun generateConversationId(userId1 : String, userId2 : String): String{
        val sortedIds = listOf(userId1, userId2).sorted()
        return sortedIds.joinToString(separator = "_")
    }

}