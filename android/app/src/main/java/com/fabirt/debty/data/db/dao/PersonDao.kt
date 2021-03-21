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
        "SELECT p.id, p.name, p.picture, SUM(m.amount) AS total FROM persons p INNER JOIN movements m ON p.id = m.person_id GROUP BY p.id, p.name, p.picture UNION SELECT id, name, picture, NULL AS total FROM persons WHERE id NOT IN (SELECT person_id FROM movements GROUP BY person_id) ORDER BY total DESC"
    )
    fun getAllPersonsWithTotal(): Flow<List<DBPersonWithTotal>>

    @Query(
        "SELECT * FROM persons WHERE id = :id"
    )
    fun getPerson(id: Int): Flow<DBPerson?>

    @Insert
    suspend fun insertPerson(person: DBPerson)

    @Update
    suspend fun updatePerson(person: DBPerson)

    @Delete
    suspend fun deletePerson(person: DBPerson)
}