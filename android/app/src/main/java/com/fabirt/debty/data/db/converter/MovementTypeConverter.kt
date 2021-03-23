package com.fabirt.debty.data.db.converter

import androidx.room.TypeConverter
import com.fabirt.debty.domain.model.MovementType

class MovementTypeConverter {

    @TypeConverter
    fun toMovementType(value: Int): MovementType {
        return when (value) {
            1 -> MovementType.Incoming
            2 -> MovementType.Outgoing
            3 -> MovementType.Balance
            else -> MovementType.Outgoing
        }
    }

    @TypeConverter
    fun fromMovementType(value: MovementType): Int {
        return when (value) {
            MovementType.Incoming -> 1
            MovementType.Outgoing -> 2
            MovementType.Balance -> 3
        }
    }
}