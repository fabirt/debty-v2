package com.fabirt.debty.data.db.entities

import android.graphics.Bitmap
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.fabirt.debty.data.db.converter.BitmapConverter
import com.fabirt.debty.domain.model.Person

@Entity(tableName = "persons")
@TypeConverters(BitmapConverter::class)
data class DBPerson(
    val name: String,
    val picture: Bitmap? = null,
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
) {
    companion object {
        fun from(person: Person) = DBPerson(person.name, person.picture, person.id)
    }
}

data class DBPersonWithTotal(
    @Embedded val person: DBPerson,
    val total: Double?
)

fun DBPerson.toDomainModel() = Person(id, name, picture)

fun DBPersonWithTotal.toDomainModel() = Person(person.id, person.name, person.picture, total)
