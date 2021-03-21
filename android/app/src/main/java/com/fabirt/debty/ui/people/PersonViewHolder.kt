package com.fabirt.debty.ui.people

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fabirt.debty.databinding.ViewItemPersonSimpleBinding
import com.fabirt.debty.domain.model.Person
import com.fabirt.debty.ui.common.PersonClickListener

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

    fun bind(person: Person, defaultAvatar: Bitmap) {
        binding.tvName.text = person.name
        val bitmap = person.picture ?: defaultAvatar
        binding.image.setImageBitmap(bitmap)
        binding.container.setOnClickListener {
            clickListener?.invoke(person)
        }

    }

    fun setOnPersonClickListener(l: PersonClickListener) {
        clickListener = l
    }
}