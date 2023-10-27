package com.example.baeguard.data.repository

import com.example.baeguard.data.model.SignInResult
import com.example.baeguard.data.model.UserData
import com.example.baeguard.util.FirestoreTables
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class AuthRepositoryImpl @Inject constructor(
    private val database: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {

    override suspend fun loginUser(email: String, password: String): SignInResult {
        return try {
            val user = firebaseAuth.signInWithEmailAndPassword(email, password).await().user
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


        }
        catch(e: Exception) {
            e.printStackTrace()
            if(e is java.util.concurrent.CancellationException) throw e
            SignInResult(
                data = null,
                errorMessage = e.message
            )
        }
    }

    override suspend fun registerUser(email: String, password: String): SignInResult {

        return try {
            val user = firebaseAuth.createUserWithEmailAndPassword(email, password).await().user
            val document = database.document(FirestoreTables.USUARIO+"/"+user!!.uid)
            user.run {
                UserData(
                    userId = uid,
                    username = displayName,
                    profilePictureUrl = photoUrl?.toString(),
                    email = email
                )
            }.let {
                document
                    .set(
                        it
                    )
            }
            SignInResult(
                data = user.run {
                    UserData(
                        userId = uid,
                        username = displayName,
                        profilePictureUrl = photoUrl?.toString(),
                        email = email
                    )
                },
                errorMessage = null
            )

        }
        catch(e: Exception) {
            e.printStackTrace()
            if(e is java.util.concurrent.CancellationException) throw e
            SignInResult(
                data = null,
                errorMessage = e.message
            )
        }
    }
}