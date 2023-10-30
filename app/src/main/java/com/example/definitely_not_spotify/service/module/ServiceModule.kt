package com.example.definitely_not_spotify.service.module

import com.example.definitely_not_spotify.service.AccountService
import com.example.definitely_not_spotify.service.StorageService
import com.example.definitely_not_spotify.service.impl.AccountServiceImpl
import com.example.definitely_not_spotify.service.impl.StorageServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceModule {
    @Binds
    abstract fun provideStorageService(impl: StorageServiceImpl): StorageService

    @Binds
    abstract fun provideAccountService(impl: AccountServiceImpl): AccountService
}