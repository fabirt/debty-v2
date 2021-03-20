package com.fabirt.debty.domain.repository.person

import com.fabirt.debty.data.db.dao.PersonDao
import com.fabirt.debty.data.db.entities.DBPerson
import com.fabirt.debty.data.db.entities.toDomainModel
import com.fabirt.debty.domain.model.Person
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PersonRepositoryImpl @Inject constructor(
    private val dao: PersonDao
) : PersonRepository {
    override fun requestAllPersons(): Flow<List<Person>> {
        return dao.getAllPersonsWithTotal().map { data ->
            data.map { it.toDomainModel() }
        }
    }

    override suspend fun createPerson(person: Person) = dao.insertPerson(DBPerson.from(person))

    override suspend fun updatePerson(person: Person) = dao.updatePerson(DBPerson.from(person))

    override suspend fun deletePerson(person: Person) = dao.deletePerson(DBPerson.from(person))
}