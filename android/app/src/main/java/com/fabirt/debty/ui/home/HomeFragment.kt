package com.fabirt.debty.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.fabirt.debty.NavGraphDirections
import com.fabirt.debty.R
import com.fabirt.debty.databinding.FragmentHomeBinding
import com.fabirt.debty.ui.chart.ChartFragment
import com.fabirt.debty.ui.people.PeopleFragment
import com.fabirt.debty.ui.summary.SummaryFragment
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var pagerAdapter: HomePagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val children = listOf(
            SummaryFragment(), PeopleFragment(), ChartFragment()
        )
        pagerAdapter = HomePagerAdapter(this, children)

        binding.fab.setOnClickListener { v ->
            val extras = FragmentNavigatorExtras(v to getString(R.string.button_transition_name))
            val action = NavGraphDirections.actionGlobalCreatePersonFragment()
            findNavController().navigate(action, extras)
        }

        binding.pager.apply {
            adapter = pagerAdapter
            (getChildAt(0) as? RecyclerView)?.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        }

        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.summary)
                1 -> getString(R.string.people)
                2 -> getString(R.string.chart)
                else -> ""
            }
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}



