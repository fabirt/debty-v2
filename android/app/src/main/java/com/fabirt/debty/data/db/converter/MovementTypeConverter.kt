package com.fabirt.debty.data.db.converter

import androidx.room.TypeConverter
import com.fabirt.debty.data.model.MovementTypeDto
import com.fabirt.debty.domain.model.MovementType

class MovementTypeConverter {

    @TypeConverter
    fun toMovementType(value: Int): MovementTypeDto {
        return MovementTypeDto(value)
    }

    @TypeConverter
    fun fromMovementType(value: MovementTypeDto): Int {
        return value.id
    }
}