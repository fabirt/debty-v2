package com.fabirt.debty.ui.home

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class HomePagerAdapter(
    supportFragment: Fragment,
    private val children: List<Fragment>
) : FragmentStateAdapter(supportFragment) {
    override fun getItemCount(): Int = children.count()

    override fun createFragment(position: Int): Fragment {
        return children[position]
    }
}