package com.fabirt.debty.domain.repository.movement

import com.fabirt.debty.domain.model.Movement
import kotlinx.coroutines.flow.Flow

interface MovementRepository {

    fun requestAllMovements(): Flow<List<Movement>>

    fun requestPersonMovements(personId: Int): Flow<List<Movement>>

    fun requestPersonMovementsSortedByDate(personId: Int): Flow<List<Movement>>

    fun requestPersonBalance(personId: Int): Flow<Double?>

    suspend fun createMovement(movement: Movement)

    suspend fun updateMovement(movement: Movement)

    suspend fun deleteMovement(movement: Movement)
}