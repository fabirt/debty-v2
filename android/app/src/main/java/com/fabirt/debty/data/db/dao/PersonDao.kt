package com.fabirt.debty.data.db.dao

import androidx.room.*
import com.fabirt.debty.data.db.entities.DBPerson
import com.fabirt.debty.data.db.entities.DBPersonWithTotal
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonDao {
    @Query("SELECT * FROM persons")
    fun getAll(): Flow<List<DBPerson>>

    @Query(
        "SELECT p.id, p.name, SUM(m.amount) AS total FROM persons p INNER JOIN movements m ON p.id = m.person_id GROUP BY p.id, p.name"
    )
    fun getAllPersonsWithTotal(): Flow<List<DBPersonWithTotal>>

    @Query(
        "SELECT p.id, p.name, SUM(m.amount) AS total FROM persons p INNER JOIN movements m ON p.id = m.person_id WHERE person_id = :id GROUP BY p.id, p.name"
    )
    fun getPersonWithTotal(id: Int): Flow<DBPersonWithTotal?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPerson(person: DBPerson)

    @Update
    suspend fun updatePerson(person: DBPerson)

    @Delete
    suspend fun deletePerson(person: DBPerson)
}