package com.fabirt.debty.ui.people.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.fabirt.debty.databinding.ViewItemMovementBinding
import com.fabirt.debty.domain.model.Movement
import com.fabirt.debty.util.toCurrencyString
import com.fabirt.debty.util.toDateString
import java.text.SimpleDateFormat

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
        val amountColor = ContextCompat.getColor(itemView.context, movement.type.color)
        binding.tvDate.text = movement.epochMilli.toDateString(SimpleDateFormat.SHORT)
        binding.tvAmount.text = movement.amount.toCurrencyString()
        binding.tvAmount.setTextColor(amountColor)
        binding.tvDescription.text = movement.description
        binding.tvMovementType.text = itemView.context.getString(movement.type.name)
    }
}