package com.fabirt.debty.ui.movement.person_search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fabirt.debty.databinding.ViewItemNewPersonBinding

class NewPersonViewHolder(
    private val binding: ViewItemNewPersonBinding
) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun from(parent: ViewGroup): NewPersonViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ViewItemNewPersonBinding.inflate(inflater, parent, false)
            return NewPersonViewHolder(binding)
        }
    }

    fun bind(l: () -> Unit) {
        binding.container.setOnClickListener { l() }
    }
}