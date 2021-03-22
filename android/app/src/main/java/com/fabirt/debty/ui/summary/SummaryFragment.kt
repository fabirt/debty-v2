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
import androidx.recyclerview.widget.LinearLayoutManager
import com.fabirt.debty.databinding.FragmentSummaryBinding
import com.fabirt.debty.domain.model.Person
import com.fabirt.debty.util.toCurrencyString
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SummaryFragment : Fragment() {

    private var _binding: FragmentSummaryBinding? = null
    private val binding get() = _binding!!
    private lateinit var personSummaryAdapter: PersonSummaryAdapter
    private val viewModel: SummaryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        personSummaryAdapter = PersonSummaryAdapter(requireContext(), ::navigateToPersonDetail)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSummaryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvPeople.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvPeople.adapter = personSummaryAdapter
        binding.tvBalanceAmount.text = (0).toCurrencyString()
        binding.tvNegativeAmount.text = (0).toCurrencyString()
        binding.tvPositiveAmount.text = (0).toCurrencyString()

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.people.collect { data ->
                binding.rvPeople.scheduleLayoutAnimation()
                binding.rvPeople.isVisible = true
                personSummaryAdapter.submitList(data)
                val summaryData = viewModel.calculateSummaryData(data)
                binding.tvBalanceAmount.animateText(summaryData.balance.toCurrencyString(), "$", 1)
                binding.tvNegativeAmount.text = summaryData.negative.toCurrencyString()
                binding.tvPositiveAmount.text = summaryData.positive.toCurrencyString()
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