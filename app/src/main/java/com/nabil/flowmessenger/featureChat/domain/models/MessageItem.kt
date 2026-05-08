package com.nabil.flowmessenger.featureChat.domain.models

data class MessageItem(
    val messageId: String,
    val message: String,
    val isMine: Boolean,
    val timeStamp : Long = 0
)