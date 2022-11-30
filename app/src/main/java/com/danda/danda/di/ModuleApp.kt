package com.danda.danda.di

import com.danda.danda.model.repository.addrecipe.AddRecipeRepository
import com.danda.danda.model.repository.addrecipe.AddRecipeRepositoryImp
import com.danda.danda.model.repository.banner.BannerRepository
import com.danda.danda.model.repository.banner.BannerRepositoryImp
import com.danda.danda.model.repository.home.HomeRepository
import com.danda.danda.model.repository.home.HomeRepositoryImp
import com.danda.danda.model.repository.profile.ProfileRepository
import com.danda.danda.model.repository.profile.ProfileRepositoryImpl
import com.danda.danda.model.repository.resepmasakanku.ResepMasakankuImp
import com.danda.danda.model.repository.resepmasakanku.ResepMasakankuRepository
import com.danda.danda.model.repository.user.UserRepository
import com.danda.danda.model.repository.user.UserRepositoryImp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
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
    fun provideDatabaseStorage(): FirebaseStorage {
        return FirebaseStorage.getInstance()
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

    @Provides
    @Singleton
    fun provideRepositoryResepMasakanku(databaseFirestore: FirebaseFirestore): ResepMasakankuRepository =
        ResepMasakankuImp(databaseFirestore)

    @Provides
    @Singleton
    fun provideRepositoryAddRecipe(databaseFirestore: FirebaseFirestore, databaseStorage: FirebaseStorage): AddRecipeRepository =
        AddRecipeRepositoryImp(databaseFirestore, databaseStorage)

    @Provides
    @Singleton
    fun provideRepositoryProfile(databaseAuth: FirebaseAuth
    ): ProfileRepository =
        ProfileRepositoryImpl(databaseAuth)
}