package com.fabirt.debty.data.db.converter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream

class BitmapConverter {

    @TypeConverter
    fun toBitmap(bytes: ByteArray?): Bitmap? {
        return bytes?.let {
            BitmapFactory.decodeByteArray(it, 0, it.size)
        }
    }

    @TypeConverter
    fun fromBitmap(bitmap: Bitmap?): ByteArray? {
        return bitmap?.let {
            val outputStream = ByteArrayOutputStream()
            it.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.toByteArray()
        }
    }
}