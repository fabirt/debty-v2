package com.fabirt.debty.ui.people.create

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.fabirt.debty.R
import com.fabirt.debty.databinding.FragmentCreatePersonBinding
import com.fabirt.debty.error.AppException
import com.fabirt.debty.ui.common.showSnackBar
import com.fabirt.debty.util.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CreatePersonFragment : Fragment() {

    private var _binding: FragmentCreatePersonBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CreatePersonViewModel by viewModels()
    private val args: CreatePersonFragmentArgs by navArgs()
    private lateinit var imagePicker: ImagePicker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        imagePicker = ImagePicker(this)
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

        // Window Insets animation
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            binding.btnSave.setWindowInsetsAnimationCallback(
                TranslateDeferringInsetsAnimationCallback(
                    view = binding.btnSave,
                    persistentInsetTypes = WindowInsets.Type.systemBars(),
                    deferredInsetTypes = WindowInsets.Type.ime(),
                    dispatchMode = WindowInsetsAnimation.Callback.DISPATCH_MODE_CONTINUE_ON_SUBTREE
                )
            )
        }

        binding.imageCard.setOnClickListener { pickImage() }
        binding.btnSave.setOnClickListener(::validate)
        binding.editTextName.requestKeyboardFocus()
        viewModel.picture.observe(viewLifecycleOwner) {
            if (it != null) binding.image.setImageBitmap(it)
        }

        lifecycleScope.launch {
            viewModel.requestInitialPerson(args.personId)?.let { person ->
                binding.title.text = getString(R.string.edit_person)
                binding.editTextName.setText(person.name)
                binding.editTextName.setSelection(person.name.length)
                if (person.picture != null) {
                    viewModel.changePicture(person.picture)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun pickImage(requestPermissionRationale: Boolean = true) {
        lifecycleScope.launch {
            try {
                val image = imagePicker.pickImage(requestPermissionRationale)
                if (image != null) {
                    viewModel.changePicture(image)
                }
            } catch (e: Exception) {
                when (e) {
                    AppException.ShouldRequestPermissionRationaleException -> {
                        showGeneralDialog(
                            R.string.permission_alert_title,
                            getString(R.string.gallery_permission_reason),
                            onConfirm = { pickImage(false) }
                        )
                    }
                    else -> {
                        showGeneralDialog(
                            R.string.permission_alert_title,
                            getString(R.string.gallery_permission_reason),
                        )
                    }
                }

            }
        }
    }

    private fun validate(v: View) {
        viewModel.changeName(binding.editTextName.text?.toString()?.trim())
        lifecycleScope.launch {
            if (!viewModel.validate()) {
                binding.inputLayoutName.error = getString(R.string.invalid_name_error)
                return@launch
            } else {
                binding.inputLayoutName.error = null
            }

            val argPersonId = args.personId?.toIntOrNull()

            if (argPersonId != null && argPersonId >= 0) {
                viewModel.updatePerson(argPersonId)
            } else if (argPersonId != null && argPersonId < 0) {
                val createdPersonId = viewModel.createPerson()
                if (createdPersonId != null) {
                    val action = CreatePersonFragmentDirections.actionCreatePersonToCreateMovement(
                        createdPersonId.toString()
                    )
                    findNavController().navigate(action)
                } else {
                    showSnackBar(getString(R.string.unexpected_error_message))
                }
                return@launch
            } else {
                viewModel.createPerson()
            }

            v.clearFocusAndCloseKeyboard()
            findNavController().popBackStack()
        }
    }
}