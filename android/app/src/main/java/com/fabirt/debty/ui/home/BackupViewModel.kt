package com.fabirt.debty.ui.home

import android.content.ContentResolver
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fabirt.debty.data.db.AppDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class BackupViewModel @Inject constructor(
    private val database: AppDatabase
) : ViewModel() {

    private val eventChannel = Channel<BackupEvent>()
    val eventFlow = eventChannel.receiveAsFlow()

    fun exportDatabase(
        databaseFile: File,
        inputUri: Uri,
        contentResolver: ContentResolver
    ) {
        try {
            database.close()
            databaseFile.inputStream().use { inputStream ->
                contentResolver.openOutputStream(inputUri).use { outputStream ->
                    val bytesCopied = inputStream.copyTo(outputStream!!)
                    Log.i("Export DB", "number of bytes copied: $bytesCopied")
                    viewModelScope.launch {
                        eventChannel.send(BackupEvent.DatabaseExported)
                    }
                }
            }
        } catch (e: IOException) {
            Log.e("Export DB", e.stackTraceToString())
        }
    }

    fun importDatabase(
        databaseFile: File,
        outputUri: Uri,
        contentResolver: ContentResolver
    ) {
        try {
            database.close()
            contentResolver.openInputStream(outputUri).use { inputStream ->
                databaseFile.outputStream().use { outputStream ->
                    inputStream!!.copyTo(outputStream)
                    viewModelScope.launch {
                        eventChannel.send(BackupEvent.DatabaseImported)
                    }
                }
            }
        } catch (e: IOException) {
            Log.e("Import DB", e.stackTraceToString())
        }
    }
}

sealed class BackupEvent {
    object DatabaseExported : BackupEvent()
    object DatabaseImported : BackupEvent()
}