package com.fabirt.debty.data.db.entities

import android.graphics.Bitmap
import androidx.room.*
import com.fabirt.debty.data.db.converter.BitmapConverter
import com.fabirt.debty.domain.model.Person

@Entity(tableName = "persons")
@TypeConverters(BitmapConverter::class)
data class DBPerson(
    val name: String,
    @ColumnInfo(name = "created_at") val createdAt: Long,
    val picture: Bitmap? = null,
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
) {
    companion object {
        fun from(person: Person) =
            DBPerson(person.name, person.createdAt, person.picture, person.id)
    }
}

data class DBPersonWithTotal(
    @Embedded val person: DBPerson,
    val total: Double?
)

fun DBPerson.toDomainModel() = Person(id, name, createdAt, picture)

fun DBPersonWithTotal.toDomainModel() =
    Person(person.id, person.name, person.createdAt, person.picture, total)
