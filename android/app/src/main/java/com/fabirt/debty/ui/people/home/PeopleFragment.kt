package com.fabirt.debty.ui.people.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.fabirt.debty.NavGraphDirections
import com.fabirt.debty.databinding.FragmentPeopleBinding
import com.fabirt.debty.domain.model.Person
import com.fabirt.debty.ui.common.showUnexpectedFailureSnackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PeopleFragment : Fragment() {

    private var _binding: FragmentPeopleBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PeopleViewModel by viewModels()
    private lateinit var adapter: PersonAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = PersonAdapter(requireContext(), ::navigateToPersonDetail)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPeopleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvPeople.adapter = adapter

        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                if (itemCount == 1) binding.rvPeople.scrollToPosition(0)
            }
        })

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
                adapter.submitList(data)
            }
    }

    private fun navigateToPersonDetail(person: Person) {
        val action = NavGraphDirections.actionGlobalPersonDetail(person.id)
        findNavController().navigate(action)
    }
}