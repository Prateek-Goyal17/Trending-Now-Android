package com.trending.now.app.feature.auth.data.repository

import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.trending.now.app.feature.auth.data.local.AuthSessionStore
import com.trending.now.app.feature.auth.domain.model.AuthUser
import com.trending.now.app.feature.auth.domain.model.NoGoogleCredentialException
import com.trending.now.app.feature.auth.domain.repository.FirebaseAuthRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseAuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val authSessionStore: AuthSessionStore,
) : FirebaseAuthRepository {
    override suspend fun signInWithGoogle(
        context: Context,
        serverClientId: String,
    ): Result<AuthUser> {
        return runCatching {
            Log.d(TAG, "Starting Google sign-in")
            val activityContext = context.findActivity() ?: context
            val credential = getGoogleCredential(
                context = activityContext,
                serverClientId = serverClientId,
                filterByAuthorizedAccounts = true,
            ) ?: getGoogleCredential(
                context = activityContext,
                serverClientId = serverClientId,
                filterByAuthorizedAccounts = false,
            )

            if (credential == null) throw NoGoogleCredentialException()

            val firebaseCredential = GoogleAuthProvider.getCredential(credential.idToken, null)
            val authResult = firebaseAuth.signInWithCredential(firebaseCredential).await()
            val firebaseUser = requireNotNull(authResult.user) {
                "Firebase did not return a signed-in user."
            }
            val firebaseIdToken = firebaseUser.getIdToken(true).await().token

            Log.d(TAG, "Google sign-in succeeded")
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
        Log.d(TAG, "Signing out")
        firebaseAuth.signOut()
        authSessionStore.clear()
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
            Log.d(TAG, "No Google credential available")
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

    private tailrec fun Context.findActivity(): ComponentActivity? {
        return when (this) {
            is ComponentActivity -> this
            is ContextWrapper -> baseContext.findActivity()
            else -> null
        }
    }

    private companion object {
        const val TAG = "FirebaseAuthRepository"
    }
}
