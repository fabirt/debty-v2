package com.fabirt.debty.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.fabirt.debty.data.db.converter.MovementTypeConverter
import com.fabirt.debty.domain.model.MovementType

@Entity(tableName = "movements")
@TypeConverters(MovementTypeConverter::class)
data class DBMovement(
    @ColumnInfo(name = "person_id") val personId: Int,
    val amount: Double,
    @ColumnInfo(name = "epoch_milli") val epochMilli: Long,
    val description: String,
    val type: MovementType,
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
)
