package com.fabirt.debty.data.db.dao

import androidx.room.*
import com.fabirt.debty.data.db.entities.DBMovement

@Dao
interface MovementDao {

    @Query("SELECT * FROM movements")
    fun getAll(): List<DBMovement>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovement(value: DBMovement)

    @Delete
    suspend fun deleteMovement(value: DBMovement)
}