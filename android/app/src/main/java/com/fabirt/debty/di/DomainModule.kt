package com.fabirt.debty.di

import com.fabirt.debty.domain.repository.movement.MovementRepository
import com.fabirt.debty.domain.repository.movement.MovementRepositoryImpl
import com.fabirt.debty.domain.repository.person.PersonRepository
import com.fabirt.debty.domain.repository.person.PersonRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DomainModule {

    @Binds
    abstract fun bindPersonRepository(impl: PersonRepositoryImpl): PersonRepository

    @Binds
    abstract fun bindMovementRepository(impl: MovementRepositoryImpl): MovementRepository
}