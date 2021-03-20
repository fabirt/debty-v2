package com.fabirt.debty.data.db.dao

import androidx.room.*
import com.fabirt.debty.data.db.entities.DBPerson

@Dao
interface PersonDao {
    @Query("SELECT * FROM persons")
    fun getAll(): List<DBPerson>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPerson(person: DBPerson)

    @Delete
    suspend fun deletePerson(person: DBPerson)
}