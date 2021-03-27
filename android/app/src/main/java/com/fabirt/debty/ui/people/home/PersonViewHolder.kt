package com.fabirt.debty.ui.people.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.fabirt.debty.R
import com.fabirt.debty.databinding.ViewItemPersonSimpleBinding
import com.fabirt.debty.domain.model.Person
import com.fabirt.debty.ui.common.PersonClickListener
import com.fabirt.debty.util.toDateString

class PersonViewHolder(
    private val binding: ViewItemPersonSimpleBinding,
    private val context: Context
) : RecyclerView.ViewHolder(binding.root) {

    private var clickListener: PersonClickListener? = null

    companion object {
        fun from(parent: ViewGroup, context: Context): PersonViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ViewItemPersonSimpleBinding.inflate(inflater, parent, false)
            return PersonViewHolder(binding, context)
        }
    }

    fun bind(person: Person) {
        binding.tvName.text = person.name
        binding.tvCreatedAt.text =
            context.getString(R.string.created_at, person.createdAt.toDateString())
        if (person.picture != null) {
            binding.image.setImageBitmap(person.picture)
        } else {
            val d =
                ResourcesCompat.getDrawable(context.resources, R.drawable.avatar_placeholder, null)
            binding.image.setImageDrawable(d)
        }

        binding.container.setOnClickListener {
            clickListener?.invoke(person)
        }

    }

    fun setOnPersonClickListener(l: PersonClickListener) {
        clickListener = l
    }
}