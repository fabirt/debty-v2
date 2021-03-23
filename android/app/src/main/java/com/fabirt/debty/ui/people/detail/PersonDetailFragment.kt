package com.fabirt.debty.ui.people.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.fabirt.debty.databinding.FragmentPersonDetailBinding
import com.fabirt.debty.util.applyStatusBarTopInset
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PersonDetailFragment : Fragment() {

    private var _binding: FragmentPersonDetailBinding? = null
    private val binding get() = _binding!!
    private val args: PersonDetailFragmentArgs by navArgs()
    private val viewModel: PersonDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPersonDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        applyWindowInsets()

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.requestPerson(args.personId).collect { person ->
                binding.tvName.text = person?.name ?: ""
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.requestMovements(args.personId).collect {

            }
        }
    }

    private fun applyWindowInsets() {
        binding.tvName.applyStatusBarTopInset()
    }
}