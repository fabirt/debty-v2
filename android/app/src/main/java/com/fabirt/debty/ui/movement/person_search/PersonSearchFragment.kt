package com.fabirt.debty.ui.movement.person_search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.fabirt.debty.databinding.FragmentPersonSearchBinding
import com.fabirt.debty.domain.model.Person
import com.fabirt.debty.ui.people.PeopleViewModel
import com.fabirt.debty.ui.people.PersonAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PersonSearchFragment : Fragment() {

    private var _binding: FragmentPersonSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: PersonAdapter
    private val viewModel: PeopleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = PersonAdapter(requireContext(), ::selectPerson)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPersonSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        applyWindowInsets()
        binding.rvPeople.adapter = adapter

        lifecycleScope.launch {
            viewModel.people.collect {
                adapter.submitList(it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun selectPerson(person: Person) {
        val action =
            PersonSearchFragmentDirections.actionPersonSearchToCreateMovement(person.id.toString())
        findNavController().navigate(action)
    }

    private fun applyWindowInsets() {
        binding.title.setOnApplyWindowInsetsListener { v, insets ->
            val statusBarInsets = WindowInsetsCompat.toWindowInsetsCompat(insets)
                .getInsets(WindowInsetsCompat.Type.statusBars())

            v.updatePadding(top = statusBarInsets.top)

            insets
        }

        binding.rvPeople.setOnApplyWindowInsetsListener { v, insets ->
            val navigationBarInsets = WindowInsetsCompat.toWindowInsetsCompat(insets)
                .getInsets(WindowInsetsCompat.Type.navigationBars())

            v.updatePadding(bottom = navigationBarInsets.bottom)

            insets
        }
    }
}