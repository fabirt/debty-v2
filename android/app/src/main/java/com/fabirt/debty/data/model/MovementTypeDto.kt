package com.fabirt.debty.data.model

import com.fabirt.debty.domain.model.MovementType

data class MovementTypeDto(val id: Int) {

    companion object {
        fun from(movementType: MovementType): MovementTypeDto {
            return when (movementType) {
                MovementType.Balance -> MovementTypeDto(1)
                MovementType.ILent -> MovementTypeDto(2)
                MovementType.IPaid -> MovementTypeDto(3)
                MovementType.LentMe -> MovementTypeDto(4)
                MovementType.PaidMe -> MovementTypeDto(5)
            }
        }
    }

    fun toDomainModel(): MovementType {
        return when (id) {
            1 -> MovementType.Balance
            2 -> MovementType.ILent
            3 -> MovementType.IPaid
            4 -> MovementType.LentMe
            5 -> MovementType.PaidMe
            else -> MovementType.ILent
        }
    }
}