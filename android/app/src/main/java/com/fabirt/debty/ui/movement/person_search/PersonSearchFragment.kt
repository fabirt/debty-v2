package com.fabirt.debty.ui.movement.person_search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.fabirt.debty.R
import com.fabirt.debty.databinding.FragmentPersonSearchBinding
import com.fabirt.debty.domain.model.FeatureToDiscover
import com.fabirt.debty.domain.model.Person
import com.fabirt.debty.ui.common.showUnexpectedFailureSnackBar
import com.fabirt.debty.ui.featurediscovery.FeatureDiscoveryViewModel
import com.fabirt.debty.util.applyNavigationBarBottomInset
import com.fabirt.debty.util.applyStatusBarTopInset
import com.fabirt.debty.util.showSingleTapTargetView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PersonSearchFragment : Fragment() {

    private var _binding: FragmentPersonSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: PersonSearchAdapter
    private val viewModel: PersonSearchViewModel by viewModels()
    private val featureDiscoveryViewModel: FeatureDiscoveryViewModel by activityViewModels()

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

        binding.includeBackButton.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.people
                .catch { showUnexpectedFailureSnackBar() }
                .collect { selectablePersons ->
                    adapter.submitList(selectablePersons)

                    // Only show feature discovery if there is only 1 selectable person
                    if (selectablePersons.count() > 1) return@collect

                    delay(400)
                    discoverFeatures()
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

    private fun discoverFeatures() {
        lifecycleScope.launch {
            val isFeatureDiscovered =
                featureDiscoveryViewModel.isFeatureDiscovered(FeatureToDiscover.CreatePerson)
            if (!isFeatureDiscovered) {
                val targetView = binding.rvPeople
                    .findViewHolderForAdapterPosition(0)
                    ?.itemView
                    ?.findViewById<View>(R.id.image_card) ?: return@launch

                requireActivity().showSingleTapTargetView(
                    view = targetView,
                    title = getString(R.string.feature_discovery_create_person_title),
                    description = getString(R.string.feature_discovery_create_person_description),
                    cancelable = true,
                    onTargetClick = {
                        featureDiscoveryViewModel.storeFeatureAsDiscovered(FeatureToDiscover.CreatePerson)
                        selectNewPerson()
                    },
                    onTargetCancel = {
                        featureDiscoveryViewModel.storeFeatureAsDiscovered(FeatureToDiscover.CreatePerson)
                    }
                )
            }
        }
    }
}