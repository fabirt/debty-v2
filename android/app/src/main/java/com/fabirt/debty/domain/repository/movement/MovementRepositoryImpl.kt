package com.fabirt.debty.domain.repository.movement

import com.fabirt.debty.data.db.dao.MovementDao
import com.fabirt.debty.data.db.entities.DBMovement
import com.fabirt.debty.data.db.entities.toDomainModel
import com.fabirt.debty.domain.model.Movement
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MovementRepositoryImpl @Inject constructor(
    private val dao: MovementDao
) : MovementRepository {

    override fun requestAllMovements(): Flow<List<Movement>> = dao.getAll().map { list ->
        list.map { it.toDomainModel() }
    }

    override fun requestAllMovementsSortedByDate(): Flow<List<Movement>> =
        dao.getAllSortedByDate().map { list ->
            list.map { it.toDomainModel() }
        }

    override fun requestPersonMovements(personId: Int): Flow<List<Movement>> {
        return dao.getPersonMovements(personId).map { value ->
            value.map { it.toDomainModel() }
        }
    }

    override fun requestPersonMovementsSortedByDate(personId: Int): Flow<List<Movement>> {
        return dao.getPersonMovementsSortedByDate(personId).map { value ->
            value.map { it.toDomainModel() }
        }
    }

    override fun requestPersonBalance(personId: Int): Flow<Double?> = dao.getPersonTotal(personId)

    override suspend fun createMovement(movement: Movement) =
        dao.insertMovement(DBMovement.from(movement))

    override suspend fun updateMovement(movement: Movement) =
        dao.updateMovement(DBMovement.from(movement))

    override suspend fun deleteMovement(movement: Movement) =
        dao.deleteMovement(DBMovement.from(movement))
}