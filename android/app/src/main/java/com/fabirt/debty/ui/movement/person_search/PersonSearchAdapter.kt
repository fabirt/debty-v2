package com.fabirt.debty.ui.movement.person_search

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fabirt.debty.domain.model.SelectablePerson
import com.fabirt.debty.ui.common.PersonClickListener
import com.fabirt.debty.ui.people.home.PersonViewHolder

class PersonSearchAdapter(
    private val onNewPersonClickListener: () -> Unit,
    private val onPersonClickListener: PersonClickListener
) : ListAdapter<SelectablePerson, RecyclerView.ViewHolder>(DiffCallback) {
    companion object {
        private const val ITEM_VIEW_TYPE_NEW = 0
        private const val ITEM_VIEW_TYPE_LOCAL = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_NEW -> NewPersonViewHolder.from(parent)
            ITEM_VIEW_TYPE_LOCAL -> PersonViewHolder.from(parent, parent.context)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is NewPersonViewHolder -> {
                holder.bind(onNewPersonClickListener)
            }
            is PersonViewHolder -> {
                val item = getItem(position) as SelectablePerson.Local
                holder.bind(item.person)
                holder.setOnPersonClickListener(onPersonClickListener)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is SelectablePerson.New -> ITEM_VIEW_TYPE_NEW
            is SelectablePerson.Local -> ITEM_VIEW_TYPE_LOCAL
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<SelectablePerson>() {
        override fun areItemsTheSame(
            oldItem: SelectablePerson,
            newItem: SelectablePerson
        ) = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: SelectablePerson,
            newItem: SelectablePerson
        ) = oldItem.id == newItem.id
    }
}