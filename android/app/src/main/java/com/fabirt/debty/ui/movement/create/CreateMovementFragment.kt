package com.fabirt.debty.ui.movement.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.fabirt.debty.R
import com.fabirt.debty.databinding.FragmentCreateMovementBinding
import com.fabirt.debty.domain.model.movementTypeOptions
import com.fabirt.debty.util.*
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

@AndroidEntryPoint
class CreateMovementFragment : Fragment() {

    private var _binding: FragmentCreateMovementBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CreateMovementViewModel by viewModels()
    private val args: CreateMovementFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateMovementBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.menu_list_item,
            movementTypeOptions.map { getString(it.name) })

        binding.autoTextViewMovement.setAdapter(adapter)

        binding.autoTextViewMovement.setOnItemClickListener { _, _, position, _ ->
            viewModel.changeMovementType(movementTypeOptions[position])
        }

        binding.editTextDate.setOnClickListener {
            openDatePicker()
        }

        binding.editTextAmount.requestKeyboardFocus()

        binding.editTextAmount.addTextChangedListener(CurrencyTextWatcher(binding.editTextAmount))

        binding.btnSave.setOnClickListener {
            validateChanges(it)
        }

        viewModel.date.observe(viewLifecycleOwner) {
            binding.editTextDate.setText(it.toDateString(SimpleDateFormat.SHORT))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun openDatePicker() {
        val initialSelection =
            viewModel.date.value?.toUtcTime() ?: MaterialDatePicker.todayInUtcMilliseconds()

        val calendarConstraints =
            CalendarConstraints.Builder()
                .setValidator(DateValidatorPointBackward.now())
                .build()

        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText(getString(R.string.date_picker_title))
                .setSelection(initialSelection)
                .setCalendarConstraints(calendarConstraints)
                .build()

        datePicker.show(childFragmentManager, "mtrl_date_picker")

        datePicker.addOnPositiveButtonClickListener { time ->
            time.utcTimeToLocaleTime()?.let {
                viewModel.changeDate(it)
            }
        }
        datePicker.addOnNegativeButtonClickListener {
            // Respond to negative button click.
        }
        datePicker.addOnCancelListener {
            // Respond to cancel button click.
        }
        datePicker.addOnDismissListener {
            // Respond to dismiss events.
        }
    }

    private fun validateChanges(v: View) {
        val amount = binding.editTextAmount.text.toString().replace(",", "")
        val description = binding.editTextDescription.text?.toString()
        var isValid = true
        if (!viewModel.validateAmount(amount)) {
            isValid = false
            binding.inputLayoutAmount.error = getString(R.string.amount_error_text)
        } else {
            binding.inputLayoutAmount.error = null
        }

        if (!viewModel.validateDescription(description)) {
            isValid = false
            binding.inputLayoutDescription.error = getString(R.string.description_error_text)
        } else {
            binding.inputLayoutDescription.error = null
        }

        if (!viewModel.validateDate()) {
            isValid = false
            binding.inputLayoutDate.error = getString(R.string.date_error_text)
        } else {
            binding.inputLayoutDate.error = null
        }

        if (!viewModel.validateMovementType()) {
            isValid = false
            binding.inputLayoutMovement.error = getString(R.string.movement_type_error_text)
        } else {
            binding.inputLayoutMovement.error = null
        }

        if (isValid) {
            lifecycleScope.launch {
                viewModel.createMovement(
                    args.personId.toInt(),
                    amount,
                    description
                )
                updateResumeWidget()
                v.clearFocusAndCloseKeyboard()
                findNavController().popBackStack()
            }
        }
    }

    private fun updateResumeWidget() {
        requireActivity().sendUpdateAppWidgetBroadcast()
    }
}