package com.fabirt.debty.ui.common

import androidx.recyclerview.widget.DiffUtil
import com.fabirt.debty.domain.model.Person

object PersonComparator : DiffUtil.ItemCallback<Person>() {
    override fun areItemsTheSame(oldItem: Person, newItem: Person): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Person, newItem: Person): Boolean {
        return oldItem.id == newItem.id && oldItem.name == newItem.name
    }
}