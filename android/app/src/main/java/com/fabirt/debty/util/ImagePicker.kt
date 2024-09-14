package com.fabirt.debty.util

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.fabirt.debty.error.AppException
import kotlinx.coroutines.CompletableDeferred

class ImagePicker(private val fragment: Fragment) {

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var requestContentLauncher: ActivityResultLauncher<String>

    private val readStoragePermission: String
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
    private val imageContent = "image/*"

    private var deferred = CompletableDeferred<Bitmap?>()

    init {
        registerForResults()
    }

    suspend fun pickImage(requestPermissionRationale: Boolean = true): Bitmap? {
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

        requestContentLauncher = fragment.registerForActivityResult(
            ActivityResultContracts.GetContent(),
            ::handleImageUri
        )
    }

    private fun checkPermissions(requestPermissionRationale: Boolean) {
        when {
            ContextCompat.checkSelfPermission(
                fragment.requireContext(),
                readStoragePermission
            ) == PackageManager.PERMISSION_GRANTED -> {
                requestContentLauncher.launch(imageContent)
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
            requestContentLauncher.launch(imageContent)
        } else {
            deferred.completeExceptionally(AppException.PermissionNotGrantedException)
        }
    }

    private fun handleImageUri(uri: Uri?) {
        val image = uri?.let {
            val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                ImageDecoder.decodeBitmap(
                    ImageDecoder.createSource(
                        fragment.requireContext().contentResolver,
                        uri
                    )
                )
            } else {
                MediaStore.Images.Media.getBitmap(fragment.requireContext().contentResolver, uri)
            }
            bitmap
        }

        deferred.complete(image)
    }
}