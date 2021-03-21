package com.fabirt.debty.domain.repository.person

import com.fabirt.debty.domain.model.Person
import kotlinx.coroutines.flow.Flow

interface PersonRepository {

    fun requestAllPersons(): Flow<List<Person>>

    fun requestAllPersonsWithTotal(): Flow<List<Person>>

    fun requestPerson(personId: Int): Flow<Person?>

    suspend fun createPerson(person: Person)

    suspend fun updatePerson(person: Person)

    suspend fun deletePerson(person: Person)
}