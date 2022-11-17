package com.danda.danda.di

import com.danda.danda.model.repository.banner.BannerRepository
import com.danda.danda.model.repository.banner.BannerRepositoryImp
import com.danda.danda.model.repository.home.HomeRepository
import com.danda.danda.model.repository.home.HomeRepositoryImp
import com.danda.danda.model.repository.user.UserRepository
import com.danda.danda.model.repository.user.UserRepositoryImp
import com.google.firebase.auth.FirebaseAuth
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
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideDatabaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideRepositoryUser(auth: FirebaseAuth): UserRepository = UserRepositoryImp(auth)

    @Provides
    @Singleton
    fun provideRepositoryBanner(databaseFirestore: FirebaseFirestore): BannerRepository =
        BannerRepositoryImp(databaseFirestore)

    @Provides
    @Singleton
    fun provideRepositoryHome(databaseFirestore: FirebaseFirestore): HomeRepository =
        HomeRepositoryImp(databaseFirestore)
}