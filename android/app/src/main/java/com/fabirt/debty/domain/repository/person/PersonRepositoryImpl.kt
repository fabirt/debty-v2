package com.fabirt.debty.domain.repository.person

import com.fabirt.debty.data.db.dao.MovementDao
import com.fabirt.debty.data.db.dao.PersonDao
import com.fabirt.debty.data.db.entities.DBPerson
import com.fabirt.debty.data.db.entities.toDomainModel
import com.fabirt.debty.domain.model.Person
import com.fabirt.debty.domain.repository.BaseRepository
import com.fabirt.debty.error.Failure
import com.fabirt.debty.util.Either
import com.fabirt.debty.util.getOrElse
import com.fabirt.debty.util.right
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class PersonRepositoryImpl(
    private val personDao: PersonDao,
    private val movementDao: MovementDao
) : BaseRepository(), PersonRepository {

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

    override suspend fun oneTimeRequestAllPersonsWithTotal(): List<Person> {
        val result = runCatching {
            val data = personDao.getAllPersonsWithTotalOneTime().map { it.toDomainModel() }
            right(data)
        }

        return result.getOrElse(listOf())
    }

    override fun requestPerson(personId: Int): Flow<Person?> {
        return personDao.getPerson(personId).map { value ->
            value?.toDomainModel()
        }
    }

    override suspend fun requestOneTimePerson(personId: Int): Person? {
        val result = runCatching {
            val person = personDao.getPersonOneTime(personId)?.toDomainModel()
            right(person)
        }
        return result.getOrNull()
    }

    override suspend fun createPerson(person: Person): Either<Failure, Long> {
        return runCatching {
            val id = personDao.insertPerson(DBPerson.from(person))
            right(id)
        }
    }

    override suspend fun updatePerson(person: Person): Either<Failure, Unit> {
        return runCatching {
            personDao.updatePerson(DBPerson.from(person))
            right(Unit)
        }
    }

    override suspend fun deleteAllPersonRelatedData(id: Int, inclusive: Boolean): Int {
        val result = runCatching {
            if (inclusive) {
                personDao.deletePerson(id)
            }
            val count = movementDao.deleteAllPersonMovements(id)
            right(count)
        }
        return result.getOrElse(0)
    }

    override suspend fun searchPerson(name: String): List<Person> {
        return personDao
            .searchPerson("%$name%")
            .first()
            .map { it.toDomainModel() }
    }
}