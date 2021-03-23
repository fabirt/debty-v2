package com.fabirt.debty.ui.people.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fabirt.debty.databinding.ViewItemMovementBinding
import com.fabirt.debty.domain.model.Movement
import com.fabirt.debty.util.toCurrencyString

class MovementViewHolder(
    private val binding: ViewItemMovementBinding
) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun from(parent: ViewGroup): MovementViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ViewItemMovementBinding.inflate(inflater, parent, false)
            return MovementViewHolder(binding)
        }
    }

    fun bind(movement: Movement) {
        binding.tvAmount.text = movement.amount.toCurrencyString()
    }
}