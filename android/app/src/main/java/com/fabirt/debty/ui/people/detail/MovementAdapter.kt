package com.fabirt.debty.ui.people.detail

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.fabirt.debty.domain.model.Movement
import com.fabirt.debty.ui.common.MovementClickListener
import com.fabirt.debty.ui.common.MovementComparator

class MovementAdapter(
    private val onClickListener: MovementClickListener
) : ListAdapter<Movement, MovementViewHolder>(MovementComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovementViewHolder {
        return MovementViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MovementViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, onClickListener)
    }
}