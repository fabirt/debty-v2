package com.fabirt.debty.domain.repository.person

import com.fabirt.debty.data.db.dao.MovementDao
import com.fabirt.debty.data.db.dao.PersonDao
import com.fabirt.debty.data.db.entities.DBPerson
import com.fabirt.debty.data.db.entities.toDomainModel
import com.fabirt.debty.domain.model.Person
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PersonRepositoryImpl @Inject constructor(
    private val personDao: PersonDao,
    private val movementDao: MovementDao
) : PersonRepository {

    override fun requestAllPersons(): Flow<List<Person>> {
        return personDao.getAll().map { data ->
            data.map { it.toDomainModel() }
        }
    }

    override fun requestAllPersonsWithTotal(): Flow<List<Person>> {
        return personDao.getAllPersonsWithTotal().map { data ->
            data.map { it.toDomainModel() }
        }
    }

    override fun requestPerson(personId: Int): Flow<Person?> {
        return personDao.getPerson(personId).map { value ->
            value?.toDomainModel()
        }
    }

    override suspend fun requestOneTimePerson(personId: Int) = personDao.getPersonOneTime(personId)?.toDomainModel()

    override suspend fun createPerson(person: Person) = personDao.insertPerson(DBPerson.from(person))

    override suspend fun updatePerson(person: Person) = personDao.updatePerson(DBPerson.from(person))

    override suspend fun deleteAllPersonRelatedData(id: Int, inclusive: Boolean): Int {
        if (inclusive) {
            personDao.deletePerson(id)
        }
        return movementDao.deleteAllPersonMovements(id)
    }
}