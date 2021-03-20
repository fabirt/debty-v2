package com.fabirt.debty.data.db.entities

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.fabirt.debty.data.db.converter.BitmapConverter

@Entity(tableName = "persons")
@TypeConverters(BitmapConverter::class)
data class DBPerson(
    val name: String,
    val picture: Bitmap?,
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
)