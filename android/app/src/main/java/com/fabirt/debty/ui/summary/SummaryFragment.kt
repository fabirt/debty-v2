package com.fabirt.debty.ui.summary

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fabirt.debty.R
import com.fabirt.debty.databinding.FragmentSummaryBinding
import com.fabirt.debty.domain.model.Person
import com.fabirt.debty.util.toCurrencyString
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SummaryFragment : Fragment() {

    private var _binding: FragmentSummaryBinding? = null
    private val binding get() = _binding!!
    private lateinit var personSummaryAdapter: PersonSummaryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val placeholder = BitmapFactory.decodeResource(resources, R.drawable.avatar_placeholder)
        personSummaryAdapter = PersonSummaryAdapter(placeholder, ::navigateToPersonDetail)
        _binding = FragmentSummaryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvPeople.adapter = personSummaryAdapter
        binding.tvBalanceAmount.text = (2400000).toCurrencyString()
        binding.tvNegativeAmount.text = (3600000).toCurrencyString()
        binding.tvPositiveAmount.text = (1200000).toCurrencyString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun navigateToPersonDetail(person: Person) {
        Log.i("SummaryFragment", person.toString())
    }
}