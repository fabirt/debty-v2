package com.fabirt.debty.ui.common

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class SwipeItemCallback<T> : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
    var adapter: ListAdapter<T, out RecyclerView.ViewHolder>? = null
    var delegate: SwipeToDeleteDelegate<T>? = null

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        adapter?.let { listAdapter ->
            val currentList = listAdapter.currentList
            currentList.getOrNull(viewHolder.bindingAdapterPosition)?.let { item ->
                delegate?.onSwiped(item)
            }
        }
    }
}

interface SwipeToDeleteDelegate<T> {
    /**
     * @param item The swiped item extracted from adapter list.
     */
    fun onSwiped(item: T)
}