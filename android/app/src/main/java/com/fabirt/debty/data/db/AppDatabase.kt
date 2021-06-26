package com.fabirt.debty.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.fabirt.debty.constant.K
import com.fabirt.debty.data.db.dao.MovementDao
import com.fabirt.debty.data.db.dao.PersonDao
import com.fabirt.debty.data.db.entities.DBMovement
import com.fabirt.debty.data.db.entities.DBPerson

@Database(
    version = 1,
    exportSchema = true,
    entities = [DBPerson::class, DBMovement::class],
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun personDao(): PersonDao

    abstract fun movementDao(): MovementDao

    companion object {
        fun create(context: Context) = Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            K.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}