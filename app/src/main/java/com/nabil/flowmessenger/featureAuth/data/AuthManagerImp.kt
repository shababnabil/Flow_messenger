package com.nabil.flowmessenger.featureAuth.data

import android.content.Context
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.nabil.flowmessenger.core.data.SessionManager
import com.nabil.flowmessenger.featureAuth.domain.AuthManager
import com.nabil.flowmessenger.featureAuth.domain.models.AuthResult
import com.nabil.flowmessenger.core.domain.models.UserData
import kotlinx.coroutines.tasks.await

class AuthManagerImp(private val context: Context) : AuthManager {
    private val firebaseAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val credentialManager: CredentialManager by lazy { CredentialManager.create(context) }


    private val getGoogleIdOption: GetGoogleIdOption by lazy {
        GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId("212230282505-tgej4jnvdodmril7t6r8p3ph1cbj0hop.apps.googleusercontent.com")
            .build()
    }

    private val getCredentialRequest: GetCredentialRequest by lazy {
        GetCredentialRequest.Builder()
            .addCredentialOption(getGoogleIdOption)
            .build()
    }

    override suspend fun signIn(): AuthResult {
        try {
            val credential =
                credentialManager.getCredential(context, getCredentialRequest).credential

            if (credential is CustomCredential
                && credential.type == GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
            ) {

                val googleIdTokenCredential =
                    GoogleIdTokenCredential.Companion.createFrom(credential.data)
                val idToken = googleIdTokenCredential.idToken //JWT

                val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                firebaseAuth.signInWithCredential(firebaseCredential).await()

                googleIdTokenCredential.apply {
                    val userData = UserData(
                        userName = displayName,
                        userEmail = id,
                        userProfilePicUri = profilePictureUri.toString(),
                        firebaseAuth.uid
                    )
                    SessionManager.currentUser = userData
                    val authResult = AuthResult(userData)
                    return authResult
                }

            } else {
                return AuthResult(errorMessage = "Invalid credential")
            }

        } catch (exception: Exception) {
            return AuthResult(errorMessage = exception.toString())
        }
    }

    override suspend fun signOut() {
        firebaseAuth.signOut()
        credentialManager.clearCredentialState(ClearCredentialStateRequest())
    }

    override fun getLoggedInUser(): UserData? {
        val currentUser = firebaseAuth.currentUser

        currentUser?.let {
            val userData = UserData(
                it.displayName,
                it.email,
                it.photoUrl.toString(),
                it.uid
            )
            SessionManager.currentUser = userData
            return userData
        }

        return null
    }

}