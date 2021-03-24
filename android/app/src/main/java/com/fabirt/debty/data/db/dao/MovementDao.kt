package com.fabirt.debty.data.db.dao

import androidx.room.*
import com.fabirt.debty.data.db.entities.DBMovement
import com.fabirt.debty.data.db.entities.DBPersonWithTotal
import kotlinx.coroutines.flow.Flow

@Dao
interface MovementDao {

    @Query("SELECT * FROM movements")
    fun getAll(): Flow<List<DBMovement>>

    @Query("SELECT * FROM movements ORDER BY epoch_milli ASC")
    fun getAllSortedByDate(): Flow<List<DBMovement>>

    @Query("SELECT * FROM movements WHERE person_id = :personId")
    fun getPersonMovements(personId: Int): Flow<List<DBMovement>>

    @Query("SELECT * FROM movements WHERE person_id = :personId ORDER BY epoch_milli DESC")
    fun getPersonMovementsSortedByDate(personId: Int): Flow<List<DBMovement>>

    @Query("SELECT SUM(amount) FROM movements WHERE person_id = :personId")
    fun getPersonTotal(personId: Int): Flow<Double?>

    @Insert
    suspend fun insertMovement(value: DBMovement)

    @Update
    suspend fun updateMovement(value: DBMovement)

    @Delete
    suspend fun deleteMovement(value: DBMovement)

    @Query(
        "SELECT SUM(amount) AS total FROM movements WHERE amount > 0"
    )
    suspend fun getTotalOweMe(): Double?

    @Query(
        "SELECT SUM(amount) AS total FROM movements WHERE amount < 0"
    )
    suspend fun getTotalIOwe(): Double?
}