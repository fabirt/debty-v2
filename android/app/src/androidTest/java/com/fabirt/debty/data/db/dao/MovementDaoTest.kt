package com.fabirt.debty.data.db.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.fabirt.debty.TestUtil
import com.fabirt.debty.data.db.AppDatabase
import com.fabirt.debty.data.db.entities.DBMovement
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MovementDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var dao: MovementDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()

        dao = database.movementDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertMovement() = runBlocking {
        val expectedId = 1234
        val actualId = dao.insertMovement(TestUtil.createDBMovement(expectedId))
        assertThat(actualId).isEqualTo(expectedId)
    }

    @Test
    fun updateMovement() = runBlocking {
        val movement = TestUtil.createDBMovement(1234)
        dao.insertMovement(movement)
        val updatedRows = dao.updateMovement(movement.copy(amount = 50_000.0))
        assertThat(updatedRows).isEqualTo(1)
    }

    @Test
    fun deleteMovement() = runBlocking {
        val movement = TestUtil.createDBMovement(1234)
        dao.insertMovement(movement)
        val deletedRows = dao.deleteMovement(movement)
        assertThat(deletedRows).isEqualTo(1)
    }

    @Test
    fun getMovement() = runBlocking {
        val movement = TestUtil.createDBMovement(1234)
        dao.insertMovement(movement)
        val result = dao.getMovement(movement.id)
        assertThat(result).isEqualTo(movement)
    }

    @Test
    fun deleteAllPersonMovements() = runBlocking {
        val list = TestUtil.createDBMovementListForSinglePerson(1234)
        for (movement in list) {
            dao.insertMovement(movement)
        }
        val result = dao.deleteAllPersonMovements(1234)
        assertThat(result).isEqualTo(list.count())
    }

    @Test
    fun getPersonTotal() = runBlocking {
        val list = TestUtil.createDBMovementListForSinglePerson(1234)
        for (movement in list) {
            dao.insertMovement(movement)
        }
        val result = dao.getPersonTotal(1234).first()
        assertThat(result).isEqualTo(60_000)
    }

    @Test
    fun getPersonMovementsSortedByDate() = runBlocking {
        val list = TestUtil.createDBMovementListForSinglePersonUnsorted(1234)
        for (movement in list) {
            dao.insertMovement(movement)
        }
        val result = dao.getPersonMovementsSortedByDate(1234).first()
        assertThat(result).isInOrder(Comparator<DBMovement> { o1, o2 ->  o2.date.compareTo(o1.date)})
    }

    @Test
    fun getPersonMovements() = runBlocking {
        val list = TestUtil.createDBMovementListForSinglePerson(1234)
        for (movement in list) {
            dao.insertMovement(movement)
        }
        val result = dao.getPersonMovements(1234).first()
        assertThat(result).isEqualTo(list)
    }
}