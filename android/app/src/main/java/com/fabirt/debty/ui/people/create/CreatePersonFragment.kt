package com.fabirt.debty.ui.people.create

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.fabirt.debty.R
import com.fabirt.debty.databinding.FragmentCreatePersonBinding
import com.fabirt.debty.util.clearFocusAndCloseKeyboard
import com.fabirt.debty.util.requestKeyboardFocus
import com.fabirt.debty.util.showGeneralDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CreatePersonFragment : Fragment() {

    private var _binding: FragmentCreatePersonBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CreatePersonViewModel by viewModels()
    private val readStoragePermission = Manifest.permission.READ_EXTERNAL_STORAGE
    private val imageContent = "image/*"
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var requestContentLauncher: ActivityResultLauncher<String>
    private val args: CreatePersonFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerForResults()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreatePersonBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imageCard.setOnClickListener { checkPermissions() }
        binding.btnSave.setOnClickListener(::validate)

        binding.editTextName.requestKeyboardFocus()

        viewModel.picture.observe(viewLifecycleOwner) {
            if (it != null) binding.image.setImageBitmap(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun validate(v: View) {
        viewModel.changeName(binding.editTextName.text?.toString()?.trim())
        lifecycleScope.launch {
            if (viewModel.validate()) {
                val argPersonId = args.personId?.toIntOrNull()
                val createdPersonId = viewModel.saveChanges()
                if (argPersonId != null && argPersonId < 0) {
                    val action = CreatePersonFragmentDirections.actionCreatePersonToCreateMovement(
                        createdPersonId.toString()
                    )
                    findNavController().navigate(action)
                } else {
                    v.clearFocusAndCloseKeyboard()
                    findNavController().popBackStack()
                }
            } else {
                binding.inputLayoutName.error = getString(R.string.invalid_name_error)
            }
        }
    }

    private fun checkPermissions() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                readStoragePermission
            ) == PackageManager.PERMISSION_GRANTED -> {
                requestContentLauncher.launch(imageContent)
            }
            shouldShowRequestPermissionRationale(readStoragePermission) -> {
                showGeneralDialog(
                    R.string.permission_alert_title,
                    getString(R.string.gallery_permission_reason),
                    R.string.ok,
                    R.string.cancel,
                    onConfirm = {
                        requestStoragePermissions()
                    }
                )
            }
            else -> {
                requestStoragePermissions()
            }
        }
    }

    private fun requestStoragePermissions() {
        requestPermissionLauncher.launch(readStoragePermission)
    }

    private fun registerForResults() {
        requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
                if (isGranted) {
                    requestContentLauncher.launch(imageContent)
                } else {
                    showGeneralDialog(
                        R.string.permission_alert_title,
                        getString(R.string.gallery_permission_reason),
                        R.string.ok,
                        R.string.cancel,
                    )
                }
            }

        requestContentLauncher =
            registerForActivityResult(ActivityResultContracts.GetContent(), ::handleImageUri)
    }

    private fun handleImageUri(uri: Uri?) {
        if (uri != null) {
            val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                ImageDecoder.decodeBitmap(
                    ImageDecoder.createSource(
                        requireContext().contentResolver,
                        uri
                    )
                )
            } else {
                MediaStore.Images.Media.getBitmap(requireContext().contentResolver, uri)
            }
            viewModel.changePicture(bitmap)
        }
    }
}