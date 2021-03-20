package com.fabirt.debty.data.db.dao

import androidx.room.*
import com.fabirt.debty.data.db.entities.DBMovement
import kotlinx.coroutines.flow.Flow

@Dao
interface MovementDao {

    @Query("SELECT * FROM movements")
    fun getAll(): Flow<List<DBMovement>>

    @Query("SELECT * FROM movements WHERE person_id = :personId")
    fun getPersonMovements(personId: Int): Flow<List<DBMovement>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovement(value: DBMovement)

    @Update
    suspend fun updateMovement(value: DBMovement)

    @Delete
    suspend fun deleteMovement(value: DBMovement)
}