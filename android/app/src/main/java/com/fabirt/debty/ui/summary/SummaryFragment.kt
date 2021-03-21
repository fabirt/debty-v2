package com.fabirt.debty.ui.summary

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.fabirt.debty.databinding.FragmentSummaryBinding
import com.fabirt.debty.domain.model.Person
import com.fabirt.debty.util.toCurrencyString
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@AndroidEntryPoint
class SummaryFragment : Fragment() {

    private var _binding: FragmentSummaryBinding? = null
    private val binding get() = _binding!!
    private lateinit var personSummaryAdapter: PersonSummaryAdapter
    private val viewModel: SummaryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        personSummaryAdapter = PersonSummaryAdapter(requireContext(), ::navigateToPersonDetail)
        _binding = FragmentSummaryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvPeople.adapter = personSummaryAdapter
        binding.tvBalanceAmount.text = (0).toCurrencyString()
        binding.tvNegativeAmount.text = (0).toCurrencyString()
        binding.tvPositiveAmount.text = (0).toCurrencyString()

        viewModel.people.observe(viewLifecycleOwner) { data ->
            binding.rvPeople.scheduleLayoutAnimation()
            binding.rvPeople.isVisible = true
            lifecycleScope.launch {
                personSummaryAdapter.submitList(data)
                var balance = 0.0
                var positive = 0.0
                var negative = 0.0
                data.forEach { person ->
                    val total = person.total
                    if (total != null) {
                        balance += total

                        if (total > 0) {
                            positive += total
                        } else if (total < 0) {
                            negative += total.absoluteValue
                        }
                    }
                }

                binding.tvBalanceAmount.text = balance.toCurrencyString()
                binding.tvNegativeAmount.text = negative.toCurrencyString()
                binding.tvPositiveAmount.text = positive.toCurrencyString()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun navigateToPersonDetail(person: Person) {
        Log.i("SummaryFragment", person.toString())
    }
}