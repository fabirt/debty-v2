package com.fabirt.debty.domain.repository.movement

import com.fabirt.debty.domain.model.Movement
import kotlinx.coroutines.flow.Flow

interface MovementRepository {

    fun requestAllMovements(): Flow<List<Movement>>
}