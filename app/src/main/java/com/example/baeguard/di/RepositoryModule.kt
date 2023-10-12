package com.example.baeguard.di

import com.example.baeguard.data.repository.Repository
import com.example.baeguard.data.repository.RepositoryImp
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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

}