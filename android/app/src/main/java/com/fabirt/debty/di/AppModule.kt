package com.fabirt.debty.di

import android.content.Context
import com.fabirt.debty.data.datastore.FeatureDiscoveryDataStore
import com.fabirt.debty.data.datastore.FeatureDiscoveryDataStoreImpl
import com.fabirt.debty.data.db.AppDatabase
import com.fabirt.debty.data.db.dao.MovementDao
import com.fabirt.debty.data.db.dao.PersonDao
import com.fabirt.debty.domain.repository.movement.MovementRepository
import com.fabirt.debty.domain.repository.movement.MovementRepositoryImpl
import com.fabirt.debty.domain.repository.person.PersonRepository
import com.fabirt.debty.domain.repository.person.PersonRepositoryImpl
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

    @Provides
    @Singleton
    fun provideFeatureDiscoveryDataStore(
        @ApplicationContext context: Context
    ): FeatureDiscoveryDataStore = FeatureDiscoveryDataStoreImpl(context)

    @Provides
    @Singleton
    fun providePersonRepository(
        personDao: PersonDao,
        movementDao: MovementDao
    ): PersonRepository = PersonRepositoryImpl(personDao, movementDao)

    @Provides
    @Singleton
    fun provideMovementRepository(
        movementDao: MovementDao
    ): MovementRepository = MovementRepositoryImpl(movementDao)
}