package com.fabirt.debty.data.db.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.fabirt.debty.TestUtil
import com.fabirt.debty.data.db.AppDatabase
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PersonDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var personDao: PersonDao
    private lateinit var movementDao: MovementDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()

        personDao = database.personDao()
        movementDao = database.movementDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertPerson() = runBlocking {
        val expectedId = 1234
        val actualId = personDao.insertPerson(TestUtil.createDBPerson(expectedId))
        assertThat(actualId).isEqualTo(expectedId)
    }

    @Test
    fun updatePerson() = runBlocking {
        val person = TestUtil.createDBPerson(1234)
        personDao.insertPerson(person)
        val updatedRows = personDao.updatePerson(person.copy(name = "Test 2"))
        assertThat(updatedRows).isEqualTo(1)
    }

    @Test
    fun deletePerson() = runBlocking {
        val person = TestUtil.createDBPerson(1234)
        personDao.insertPerson(person)
        val deletedRows = personDao.deletePerson(1234)
        assertThat(deletedRows).isEqualTo(1)
    }

    @Test
    fun getPerson() = runBlocking {
        val person = TestUtil.createDBPerson(1234)
        personDao.insertPerson(person)
        val result = personDao.getPerson(1234).first()
        assertThat(result).isEqualTo(person)
    }

    @Test
    fun getAllPersonsWithTotal() = runBlocking {
        val people = TestUtil.createMultipleDBPerson()
        val movements = TestUtil.createMultipleDBMovement()
        for (person in people) {
            personDao.insertPerson(person)
        }
        for (m in movements) {
            movementDao.insertMovement(m)
        }
        val result = personDao.getAllPersonsWithTotal().first()
        val expected = TestUtil.createMultipleDBPersonWithTotal()
        assertThat(result).isEqualTo(expected)
    }
}