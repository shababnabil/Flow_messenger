package com.nabil.flowmessenger.core.data

import com.nabil.flowmessenger.core.domain.models.UserData

object SessionManager {

    var currentUser : UserData? = null

    fun getCurrentUserId() : String?{
        return currentUser?.userId
    }


}