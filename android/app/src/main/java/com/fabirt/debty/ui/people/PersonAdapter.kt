package com.fabirt.debty.ui.people

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.fabirt.debty.R
import com.fabirt.debty.domain.model.Person
import com.fabirt.debty.ui.common.PersonClickListener
import com.fabirt.debty.ui.common.PersonComparator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PersonAdapter(
    private val context: Context,
    private val onPersonClickListener: PersonClickListener
) : ListAdapter<Person, PersonViewHolder>(PersonComparator) {
    private var defaultAvatar: Bitmap? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
        return PersonViewHolder.from(parent, context)
    }

    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        GlobalScope.launch(Dispatchers.Main) {
            val person = getItem(position)
            defaultAvatar = defaultAvatar ?: withContext(Dispatchers.Default) {
                BitmapFactory.decodeResource(context.resources, R.drawable.avatar_placeholder)
            }
            holder.bind(person, defaultAvatar!!)
            holder.setOnPersonClickListener(onPersonClickListener)
        }
    }
}