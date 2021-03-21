package com.fabirt.debty.ui.summary

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.fabirt.debty.domain.model.Person
import com.fabirt.debty.ui.common.PersonClickListener

class PersonSummaryAdapter(
    private val context: Context,
    private val onPersonClickListener: PersonClickListener
) : ListAdapter<Person, PersonSummaryViewHolder>(PersonSummaryViewHolder.Comparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonSummaryViewHolder {
        return PersonSummaryViewHolder.from(parent, context)
    }

    override fun onBindViewHolder(holder: PersonSummaryViewHolder, position: Int) {
        val person = getItem(position)
        holder.bind(person)
        holder.setOnPersonClickListener(onPersonClickListener)
    }
}