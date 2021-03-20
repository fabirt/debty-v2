package com.fabirt.debty.data.db.entities

import android.graphics.Bitmap
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.fabirt.debty.data.db.converter.BitmapConverter

@Entity(tableName = "persons")
@TypeConverters(BitmapConverter::class)
data class DBPerson(
    val name: String,
    val picture: Bitmap? = null,
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
)

data class DBPersonWithTotal(
    @Embedded val person: DBPerson,
    val total: Double
)