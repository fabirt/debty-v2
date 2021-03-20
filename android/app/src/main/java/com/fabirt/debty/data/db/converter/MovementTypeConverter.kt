package com.fabirt.debty.data.db.converter

import androidx.room.TypeConverter
import com.fabirt.debty.domain.model.MovementType

class MovementTypeConverter {

    @TypeConverter
    fun toMovementType(value: Int): MovementType {
        return when (value) {
            1 -> MovementType.Get
            2 -> MovementType.Give
            3 -> MovementType.Balance
            else -> MovementType.Give
        }
    }

    @TypeConverter
    fun fromMovementType(value: MovementType): Int {
        return when (value) {
            MovementType.Get -> 1
            MovementType.Give -> 2
            MovementType.Balance -> 3
        }
    }
}