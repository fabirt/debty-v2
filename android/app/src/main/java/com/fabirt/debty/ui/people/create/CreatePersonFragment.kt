package com.fabirt.debty.ui.people.create

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.fabirt.debty.R
import com.fabirt.debty.databinding.FragmentCreatePersonBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CreatePersonFragment : Fragment() {

    private var _binding: FragmentCreatePersonBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CreatePersonViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreatePersonBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSave.setOnClickListener {
            viewModel.changeName(binding.editTextName.text?.toString())
            lifecycleScope.launch {
                val success = viewModel.saveChanges()
                if (success) {
                    findNavController().popBackStack()
                } else {
                    binding.inputLayoutName.error = getString(R.string.invalid_name_error)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}