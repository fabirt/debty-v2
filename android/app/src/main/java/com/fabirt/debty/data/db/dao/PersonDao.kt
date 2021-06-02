package com.fabirt.debty.data.db.dao

import androidx.room.*
import com.fabirt.debty.data.db.entities.DBPerson
import com.fabirt.debty.data.db.entities.DBPersonWithTotal
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonDao {
    @Query("SELECT * FROM persons ORDER BY id DESC")
    fun getAll(): Flow<List<DBPerson>>

    @Query(
        "SELECT p.id, p.name, p.created_at, p.picture, SUM(m.amount) AS total FROM persons p INNER JOIN movements m ON p.id = m.person_id GROUP BY p.id, p.name, p.created_at, p.picture HAVING total != 0 ORDER BY ABS(total) DESC"
    )
    fun getAllPersonsWithTotal(): Flow<List<DBPersonWithTotal>>

    @Query(
        "SELECT p.id, p.name, p.created_at, p.picture, SUM(m.amount) AS total FROM persons p INNER JOIN movements m ON p.id = m.person_id GROUP BY p.id, p.name, p.created_at, p.picture HAVING total != 0 ORDER BY ABS(total) DESC"
    )
    suspend fun getAllPersonsWithTotalOneTime(): List<DBPersonWithTotal>

    @Query(
        "SELECT * FROM persons WHERE id = :id"
    )
    fun getPerson(id: Int): Flow<DBPerson?>

    @Query(
        "SELECT * FROM persons WHERE id = :id"
    )
    suspend fun getPersonOneTime(id: Int): DBPerson?

    @Insert
    suspend fun insertPerson(person: DBPerson): Long

    @Update
    suspend fun updatePerson(person: DBPerson): Int

    @Query("DELETE FROM persons WHERE id = :id")
    suspend fun deletePerson(id: Int): Int

    @Query("SELECT * FROM persons WHERE name LIKE :name")
    fun searchPerson(name: String): Flow<List<DBPerson>>
}
