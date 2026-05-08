package com.nabil.flowmessenger.featureChat.data

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.database.ValueEventListener
import com.nabil.flowmessenger.core.data.SessionManager
import com.nabil.flowmessenger.core.data.constants.DatabasePaths
import com.nabil.flowmessenger.featureChat.domain.MessageRepository
import com.nabil.flowmessenger.featureChat.domain.models.MessageItem
import com.nabil.flowmessenger.featureChat.domain.models.MessageResult
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class MessageRepositoryImpl : MessageRepository {
    private val firebaseDatabase: FirebaseDatabase by lazy { FirebaseDatabase.getInstance() }
    private val reference: DatabaseReference by lazy { firebaseDatabase.getReference(DatabasePaths.CONVERSATIONS) }

    override fun getMessages(conversionId: String): Flow<List<MessageItem>> = callbackFlow {

        val conversationChild = reference.child(conversionId)

        val messageListener = object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val messages = snapshot.children.mapNotNull { it.toMessageItem() }
                trySend(messages)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }

        }

        conversationChild.addValueEventListener(messageListener)

        awaitClose {
            conversationChild.removeEventListener(messageListener)
        }

    }

    override suspend fun sendMessage(message: String, conversionId: String): MessageResult {

        try {
            val conversationChild = reference.child(conversionId)
            val messageId = conversationChild.push().key

            if (messageId != null) {
                conversationChild.child(messageId).setValue(
                    MessageItemDto(
                        message,
                        SessionManager.getCurrentUserId()!!,
                        ServerValue.TIMESTAMP
                    )
                ).await()
                return MessageResult(true)
            }

        } catch (exception: Exception) {

        }

        return MessageResult(false, "Error sending message")
    }

    private fun DataSnapshot.toMessageItem(): MessageItem? {
        val messageItemDto = this.getValue(MessageItemDto::class.java)
        messageItemDto?.apply {
            return MessageItem(
                messageId = key.toString(),
                message = message,
                isMine = senderId.also {
                    Log.d("qwerty", "senderId: $it")
                } == SessionManager.currentUser?.userId!!.also { Log.d("qwerty", "toMessageItem: $it") } ,
                timeStamp = timeStamp as Long
            )
        }
        return null
    }

}