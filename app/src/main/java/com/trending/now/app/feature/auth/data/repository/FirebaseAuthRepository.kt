package com.trending.now.app.feature.auth.data.repository

import android.content.Context
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.trending.now.app.feature.auth.domain.model.AuthUser
import com.trending.now.app.feature.auth.domain.repository.AuthRepository
import kotlinx.coroutines.tasks.await

class FirebaseAuthRepository(
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance(),
) : AuthRepository {
    override suspend fun signInWithGoogle(
        context: Context,
        serverClientId: String,
    ): Result<AuthUser> {
        return runCatching {
            val credential = getGoogleCredential(
                context = context,
                serverClientId = serverClientId,
                filterByAuthorizedAccounts = true,
            ) ?: getGoogleCredential(
                context = context,
                serverClientId = serverClientId,
                filterByAuthorizedAccounts = false,
            )

            requireNotNull(credential) {
                "No Google account credential was selected."
            }

            val firebaseCredential = GoogleAuthProvider.getCredential(credential.idToken, null)
            val authResult = firebaseAuth.signInWithCredential(firebaseCredential).await()
            val firebaseUser = requireNotNull(authResult.user) {
                "Firebase did not return a signed-in user."
            }
            val firebaseIdToken = firebaseUser.getIdToken(true).await().token

            AuthUser(
                uid = firebaseUser.uid,
                email = firebaseUser.email,
                displayName = firebaseUser.displayName,
                photoUrl = firebaseUser.photoUrl?.toString(),
                firebaseIdToken = firebaseIdToken,
            )
        }
    }

    override suspend fun signOut(context: Context) {
        firebaseAuth.signOut()
        CredentialManager.create(context).clearCredentialState(ClearCredentialStateRequest())
    }

    private suspend fun getGoogleCredential(
        context: Context,
        serverClientId: String,
        filterByAuthorizedAccounts: Boolean,
    ): GoogleIdTokenCredential? {
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(filterByAuthorizedAccounts)
            .setServerClientId(serverClientId)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        val response = try {
            CredentialManager.create(context).getCredential(context, request)
        } catch (_: NoCredentialException) {
            return null
        }

        val credential = response.credential
        if (
            credential is CustomCredential &&
            credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
        ) {
            return GoogleIdTokenCredential.createFrom(credential.data)
        }

        error("Credential response was not a Google ID token.")
    }
}
