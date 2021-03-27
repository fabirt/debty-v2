package com.fabirt.debty.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.fabirt.debty.data.db.converter.MovementTypeConverter
import com.fabirt.debty.data.model.MovementTypeDto
import com.fabirt.debty.domain.model.Movement

@Entity(tableName = "movements")
@TypeConverters(MovementTypeConverter::class)
data class DBMovement(
    @ColumnInfo(name = "person_id") val personId: Int,
    val amount: Double,
    val date: Long,
    val description: String,
    val type: MovementTypeDto,
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
) {
    companion object {
        fun from(movement: Movement) = DBMovement(
            personId = movement.personId,
            amount = movement.amount,
            date = movement.date,
            description = movement.description,
            type = MovementTypeDto.from(movement.type),
            id = movement.id
        )
    }
}

fun DBMovement.toDomainModel() = Movement(
    id = id,
    personId = personId,
    amount = amount,
    date = date,
    description = description,
    type = type.toDomainModel()
)
