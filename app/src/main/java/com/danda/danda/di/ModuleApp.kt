package com.danda.danda.di

import com.danda.danda.model.repository.user.UserRepository
import com.danda.danda.model.repository.user.UserRepositoryImp
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object ModuleApp {

    @Provides
    @Singleton
    fun provideFirebase(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideNoteRepository(
        database: FirebaseFirestore
    ): UserRepository {
        return UserRepositoryImp(database)
    }
}