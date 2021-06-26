package com.fabirt.debty.util

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.fabirt.debty.error.AppException
import kotlinx.coroutines.CompletableDeferred

class FilePicker(private val fragment: Fragment) {

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var openDocumentLauncher: ActivityResultLauncher<Array<String>>

    private val readStoragePermission = Manifest.permission.READ_EXTERNAL_STORAGE

    private var deferred = CompletableDeferred<Uri?>()
    private var contentType: Array<String>? = null

    init {
        registerForResults()
    }

    suspend fun pickFile(
        contentType: Array<String>,
        requestPermissionRationale: Boolean = true
    ): Uri? {
        this.contentType = contentType
        if (deferred.isCompleted) {
            deferred = CompletableDeferred()
        }
        checkPermissions(requestPermissionRationale)
        return deferred.await()
    }

    private fun registerForResults() {
        requestPermissionLauncher = fragment.registerForActivityResult(
            ActivityResultContracts.RequestPermission(),
            ::handlePermissions
        )

        openDocumentLauncher = fragment.registerForActivityResult(
            ActivityResultContracts.OpenDocument(),
            ::handleUri
        )
    }

    private fun checkPermissions(requestPermissionRationale: Boolean) {
        when {
            ContextCompat.checkSelfPermission(
                fragment.requireContext(),
                readStoragePermission
            ) == PackageManager.PERMISSION_GRANTED -> {
                openDocumentLauncher.launch(contentType!!)
            }
            fragment.shouldShowRequestPermissionRationale(readStoragePermission) -> {
                if (requestPermissionRationale) {
                    deferred.completeExceptionally(AppException.ShouldRequestPermissionRationaleException)
                } else {
                    requestPermissionLauncher.launch(readStoragePermission)
                }
            }
            else -> {
                requestPermissionLauncher.launch(readStoragePermission)
            }
        }
    }

    private fun handlePermissions(isGranted: Boolean) {
        if (isGranted) {
            openDocumentLauncher.launch(contentType!!)
        } else {
            deferred.completeExceptionally(AppException.PermissionNotGrantedException)
        }
    }

    private fun handleUri(uri: Uri?) {
        deferred.complete(uri)
    }
}