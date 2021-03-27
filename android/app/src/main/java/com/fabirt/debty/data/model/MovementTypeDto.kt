package com.fabirt.debty.data.model

import com.fabirt.debty.domain.model.MovementType

data class MovementTypeDto(val id: Int) {

    companion object {
        fun from(movementType: MovementType): MovementTypeDto {
            return when (movementType) {
                MovementType.Incoming -> MovementTypeDto(1)
                MovementType.Outgoing -> MovementTypeDto(2)
                MovementType.Balance -> MovementTypeDto(3)
            }
        }
    }

    fun toDomainModel(): MovementType {
        return when (id) {
            1 -> MovementType.Incoming
            2 -> MovementType.Outgoing
            3 -> MovementType.Balance
            else -> MovementType.Outgoing
        }
    }
}