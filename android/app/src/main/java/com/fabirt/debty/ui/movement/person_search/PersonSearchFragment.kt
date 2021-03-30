package com.fabirt.debty.ui.movement.person_search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.fabirt.debty.databinding.FragmentPersonSearchBinding
import com.fabirt.debty.domain.model.Person
import com.fabirt.debty.ui.common.showUnexpectedFailureSnackBar
import com.fabirt.debty.util.applyNavigationBarBottomInset
import com.fabirt.debty.util.applyStatusBarTopInset
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PersonSearchFragment : Fragment() {

    private var _binding: FragmentPersonSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: PersonSearchAdapter
    private val viewModel: PersonSearchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = PersonSearchAdapter(
            onNewPersonClickListener = ::selectNewPerson,
            onPersonClickListener = ::selectPerson
        )
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

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.people
                .catch { showUnexpectedFailureSnackBar() }
                .collect {
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

    private fun selectNewPerson() {
        val action = PersonSearchFragmentDirections.actionPersonSearchToCreatePerson("-1")
        findNavController().navigate(action)
    }

    private fun applyWindowInsets() {
        binding.title.applyStatusBarTopInset()
        binding.rvPeople.applyNavigationBarBottomInset()
    }
}