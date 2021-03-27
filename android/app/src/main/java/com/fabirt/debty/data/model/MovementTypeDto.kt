package com.fabirt.debty.data.model

import com.fabirt.debty.domain.model.MovementType

data class MovementTypeDto(val id: Int) {

    companion object {
        fun from(movementType: MovementType): MovementTypeDto {
            return when (movementType) {
                MovementType.ILent -> MovementTypeDto(1)
                MovementType.IPaid -> MovementTypeDto(2)
                MovementType.LentMe -> MovementTypeDto(3)
                MovementType.PaidMe -> MovementTypeDto(4)
                MovementType.IOwedSettled -> MovementTypeDto(5)
                MovementType.OwedMeSettled -> MovementTypeDto(6)
            }
        }
    }

    fun toDomainModel(): MovementType {
        return when (id) {
            1 -> MovementType.ILent
            2 -> MovementType.IPaid
            3 -> MovementType.LentMe
            4 -> MovementType.PaidMe
            5 -> MovementType.IOwedSettled
            6 -> MovementType.OwedMeSettled
            else -> MovementType.ILent
        }
    }
}