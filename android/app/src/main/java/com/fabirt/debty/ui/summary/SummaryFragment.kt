package com.fabirt.debty.ui.summary

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fabirt.debty.databinding.FragmentSummaryBinding
import com.fabirt.debty.util.toCurrencyString
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SummaryFragment : Fragment() {

    private var _binding: FragmentSummaryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSummaryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvBalanceAmount.text = (2400000).toCurrencyString()
        binding.tvNegativeAmount.text = (3600000).toCurrencyString()
        binding.tvPositiveAmount.text = (1200000).toCurrencyString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}