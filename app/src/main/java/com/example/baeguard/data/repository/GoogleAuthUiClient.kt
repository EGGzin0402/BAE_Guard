package com.example.baeguard.data.repository

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.util.Log
import com.example.baeguard.R
import com.example.baeguard.data.model.SignInResult
import com.example.baeguard.data.model.UserData
import com.example.baeguard.util.FirestoreTables
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest.GoogleIdTokenRequestOptions
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CancellationException

private val TAG = "BAE GOOGLE AUTH"

class GoogleAuthUiClient(
    private val database: FirebaseFirestore,
    private val context: Context,
    private val oneTapClient: SignInClient
) {
    private val auth = Firebase.auth

    suspend fun signIn(): IntentSender?{
        val result = try {
            oneTapClient.beginSignIn(

                buildSignInRequest()

            ).await()
        }catch (e: Exception){
            e.printStackTrace()
            if(e is CancellationException) throw e
            null
        }
        return result?.pendingIntent?.intentSender
    }

    suspend fun signInWithIntent(intent: Intent): SignInResult {
        val credential = oneTapClient.getSignInCredentialFromIntent(intent)
        val googleIdToken = credential.googleIdToken
        val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)

        val user = auth.signInWithCredential(googleCredentials).await().user


        return try {
            val user = auth.signInWithCredential(googleCredentials).await().user
            database.collection(FirestoreTables.USUARIO).document(user!!.uid).get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                    } else {
                        val document = database.document(FirestoreTables.USUARIO+"/"+user.uid)
                        user?.run {
                            UserData(
                                userId = uid,
                                username = displayName,
                                profilePictureUrl = photoUrl?.toString(),
                                email = email
                            )
                        }?.let {
                            document
                                .set(
                                    it
                                ).addOnSuccessListener {
                                    Log.i(TAG, "Usuario criado com sucesso")
                                }.addOnFailureListener{
                                    Log.e(TAG, it.localizedMessage!!)
                                }
                        }
                    }
                }
            SignInResult(
                data = user?.run {
                    UserData(
                        userId = uid,
                        username = displayName,
                        profilePictureUrl = photoUrl?.toString(),
                        email = email
                    )
                },
                errorMessage = null
            )
        }catch (e: Exception){
            e.printStackTrace()
            if(e is CancellationException) throw e
            SignInResult(
                data = null,
                errorMessage = e.message
            )
        }
    }

    suspend fun signOut(){
        try {

            oneTapClient.signOut().await()
            auth.signOut()

        }catch (e: Exception){
            e.printStackTrace()
            if(e is CancellationException) throw e
        }
    }

    fun getSignedInUser(): UserData? = auth.currentUser?.run {
        UserData(
            userId = uid,
            username = displayName,
            profilePictureUrl = photoUrl?.toString(),
            email = email
        )
    }

    private fun buildSignInRequest(): BeginSignInRequest{
        return BeginSignInRequest.Builder()
            .setGoogleIdTokenRequestOptions(
                GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(context.getString(R.string.web_client))
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()
    }

}