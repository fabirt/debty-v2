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

    override suspend fun requestOneTimePerson(personId: Int): Person? {
        val result = runCatching {
            personDao.getPersonOneTime(personId)?.toDomainModel()
        }
        return result.getOrNull()
    }

    override suspend fun createPerson(person: Person): Long? {
        val result = runCatching {
            personDao.insertPerson(DBPerson.from(person))
        }
        return result.getOrNull()
    }

    override suspend fun updatePerson(person: Person) {
        runCatching {
            personDao.updatePerson(DBPerson.from(person))
        }
    }

    override suspend fun deleteAllPersonRelatedData(id: Int, inclusive: Boolean): Int {
        val result = runCatching {
            if (inclusive) {
                personDao.deletePerson(id)
            }
            movementDao.deleteAllPersonMovements(id)
        }

        return result.getOrDefault(0)
    }
}