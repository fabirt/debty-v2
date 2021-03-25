package com.fabirt.debty.ui.people.home

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.fabirt.debty.domain.model.Person
import com.fabirt.debty.ui.common.PersonClickListener
import com.fabirt.debty.ui.common.PersonComparator

class PersonAdapter(
    private val context: Context,
    private val onPersonClickListener: PersonClickListener
) : ListAdapter<Person, PersonViewHolder>(PersonComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
        return PersonViewHolder.from(parent, context)
    }

    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        val person = getItem(position)
        holder.bind(person)
        holder.setOnPersonClickListener(onPersonClickListener)
    }
}