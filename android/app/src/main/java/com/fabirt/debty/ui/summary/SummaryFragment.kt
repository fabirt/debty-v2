package com.fabirt.debty.ui.summary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.fabirt.debty.NavGraphDirections
import com.fabirt.debty.databinding.FragmentSummaryBinding
import com.fabirt.debty.domain.model.Person
import com.fabirt.debty.ui.common.showUnexpectedFailureSnackBar
import com.fabirt.debty.util.calculateSummaryData
import com.fabirt.debty.util.toCurrencyString2
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SummaryFragment : Fragment() {

    private var _binding: FragmentSummaryBinding? = null
    private val binding get() = _binding!!
    private lateinit var personSummaryAdapter: PersonSummaryAdapter
    private val viewModel: SummaryViewModel by viewModels()
    private var isRecreatingFragment = false

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

        viewLifecycleOwner.lifecycleScope.launch {
            renderPeople()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private suspend fun renderPeople() {
        viewModel.people
            .catch { showUnexpectedFailureSnackBar() }
            .collect { data ->
                val isEmpty = data.isEmpty()

                binding.rvPeople.isVisible = !isEmpty
                binding.tvEmpty.isVisible = isEmpty

                if (isRecreatingFragment) {
                    binding.rvPeople.itemAnimator = null
                } else {
                    binding.rvPeople.scheduleLayoutAnimation()
                }
                personSummaryAdapter.submitList(data)
                val summaryData = calculateSummaryData(data)
                if (isRecreatingFragment) {
                    binding.tvBalanceAmount.text = summaryData.balance.toCurrencyString2()
                } else {
                    binding.tvBalanceAmount.animateText(
                        summaryData.balance.toCurrencyString2(),
                        "$",
                        1
                    )
                }
                binding.tvNegativeAmount.text = summaryData.negative.toCurrencyString2()
                binding.tvPositiveAmount.text = summaryData.positive.toCurrencyString2()
                isRecreatingFragment = true
            }
    }

    private fun navigateToPersonDetail(person: Person) {
        val action = NavGraphDirections.actionGlobalPersonDetail(person.id)
        findNavController().navigate(action)
    }
}