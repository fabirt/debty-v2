package com.fabirt.debty.ui.people.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.fabirt.debty.NavGraphDirections
import com.fabirt.debty.R
import com.fabirt.debty.databinding.FragmentPersonDetailBinding
import com.fabirt.debty.util.applyNavigationBarBottomInset
import com.fabirt.debty.util.applyNavigationBarBottomMargin
import com.fabirt.debty.util.applyStatusBarTopInset
import com.fabirt.debty.util.toCurrencyString
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@AndroidEntryPoint
class PersonDetailFragment : Fragment() {

    private var _binding: FragmentPersonDetailBinding? = null
    private val binding get() = _binding!!
    private val args: PersonDetailFragmentArgs by navArgs()
    private val viewModel: PersonDetailViewModel by viewModels()
    private lateinit var adapter: MovementAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = MovementAdapter()
    }

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
        binding.rvMovements.adapter = adapter

        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                if (itemCount == 1) binding.rvMovements.scrollToPosition(0)
            }
        })

        binding.btnNewMovement.setOnClickListener {
            navigateToNewMovement()
        }

        binding.iconButtonShare.setOnClickListener {

        }

        binding.iconButtonEdit.setOnClickListener {

        }

        binding.iconButtonDelete.setOnClickListener {

        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.requestPerson(args.personId).collect { person ->
                binding.tvName.text = person?.name ?: ""
                if (person?.picture != null) {
                    binding.image.setImageBitmap(person.picture)
                } else {
                    val d =
                        ResourcesCompat.getDrawable(
                            requireContext().resources,
                            R.drawable.avatar_placeholder,
                            null
                        )
                    binding.image.setImageDrawable(d)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.requestMovements(args.personId).collect { data ->
                val isEmpty = data.isEmpty()
                binding.rvMovements.isVisible = !isEmpty
                binding.tvEmpty.isVisible = isEmpty
                adapter.submitList(data)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.requestBalance(args.personId).collect {
                binding.tvTotal.text = it?.absoluteValue.toCurrencyString()
                if (it != null) {
                    binding.tvTotalLabel.text = if (it > 0) {
                        getString(R.string.owe_me)
                    } else getString(R.string.i_owe)
                }
            }
        }
    }

    private fun navigateToNewMovement() {
        val action = NavGraphDirections.actionGlobalCreateMovementFragment(args.personId.toString())
        findNavController().navigate(action)
    }
}