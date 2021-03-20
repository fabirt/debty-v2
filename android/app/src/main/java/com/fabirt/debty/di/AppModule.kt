package com.fabirt.debty.di

import android.content.Context
import com.fabirt.debty.data.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ) = AppDatabase.create(context)

    @Provides
    @Singleton
    fun providePersonDao(database: AppDatabase) = database.personDao()

    @Provides
    @Singleton
    fun provideMovementDao(database: AppDatabase) = database.movementDao()
}