package com.example.baeguard.di

import android.content.Context
import com.example.baeguard.data.repository.AuthRepository
import com.example.baeguard.data.repository.AuthRepositoryImpl
import com.example.baeguard.data.repository.GoogleAuthUiClient
import com.example.baeguard.data.repository.Repository
import com.example.baeguard.data.repository.RepositoryImp
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Provides
    @Singleton
    fun provideRepository(
        database: FirebaseFirestore
    ): Repository{
        return RepositoryImp(database)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        firebaseFirestore: FirebaseFirestore,
        firebaseAuth: FirebaseAuth
    ): AuthRepository{
        return AuthRepositoryImpl(firebaseFirestore, firebaseAuth)
    }

    @Provides
    @Singleton
    fun provideGoogleAuthRepository(
        firebaseFirestore: FirebaseFirestore,
        @ApplicationContext context: Context
    ): GoogleAuthUiClient{
        return GoogleAuthUiClient(
            database = firebaseFirestore,
            context = context,
            oneTapClient = Identity.getSignInClient(context)
        )
    }

}