package com.fabirt.debty.domain.repository.movement

import com.fabirt.debty.data.db.dao.MovementDao
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
}