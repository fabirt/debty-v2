package com.fabirt.debty.domain.repository.person

import com.fabirt.debty.domain.model.Person
import kotlinx.coroutines.flow.Flow

interface PersonRepository {

    /**
     * @return [Flow] of persons without total balance.
     */
    fun requestAllPersons(): Flow<List<Person>>

    /**
     * @return [Flow] of persons with its total balance calculated.
     */
    fun requestAllPersonsWithTotal(): Flow<List<Person>>

    /**
     * Search a person by its [personId] and listen changes.
     */
    fun requestPerson(personId: Int): Flow<Person?>

    /**
     * Search a person by its [personId].
     * @return `null` if the given [personId] doesn't exist.
     */
    suspend fun requestOneTimePerson(personId: Int): Person?

    /**
     * Insert a new person in the database.
     * @return ID of the created person.
     */
    suspend fun createPerson(person: Person): Long

    /**
     * @param person The entity to update
     */
    suspend fun updatePerson(person: Person)

    /**
     * Deletes all data related to the [Person] with the given [id] from the database
     * @param inclusive whether you want to delete the matching [Person] too.
     */
    suspend fun deleteAllPersonRelatedData(id: Int, inclusive: Boolean = true)
}