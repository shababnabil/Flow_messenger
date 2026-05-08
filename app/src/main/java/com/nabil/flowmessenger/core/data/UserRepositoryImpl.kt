package com.nabil.flowmessenger.core.data

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.nabil.flowmessenger.core.data.constants.DatabasePaths
import com.nabil.flowmessenger.core.data.dtos.UserDataDto
import com.nabil.flowmessenger.core.domain.UserRepository
import com.nabil.flowmessenger.core.domain.models.UserData
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class UserRepositoryImpl : UserRepository {

    private val database : FirebaseDatabase = FirebaseDatabase.getInstance()

    override suspend fun saveUserAuthToDatabase(userData: UserData) {
        val reference = database.getReference(DatabasePaths.USERS)
        reference.child(userData.userId!!).setValue(userData.toDto()).await()
    }

    override fun getUserList(): Flow<List<UserData>> = callbackFlow {
        val databaseReference : DatabaseReference = database.getReference(DatabasePaths.USERS)

        val listener = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val userList = snapshot.children.mapNotNull { it.toUserData() }.filter{ it.userId!= SessionManager.currentUser?.userId }
                trySend(userList)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        databaseReference.addValueEventListener(listener)

        awaitClose { databaseReference.removeEventListener(listener) }

    }

    fun UserData.toDto() : UserDataDto{
        return UserDataDto(userName, userEmail, userProfilePicUri)
    }
    private fun DataSnapshot.toUserData() : UserData{
        val dto = getValue(UserDataDto::class.java)
        return UserData(dto?.userName, dto?.userEmail, dto?.userProfilePicUri, key)
    }


}