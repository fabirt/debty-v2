package com.fabirt.debty.ui.summary

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.fabirt.debty.databinding.ViewItemPersonBinding
import com.fabirt.debty.domain.model.Person
import com.fabirt.debty.ui.common.PersonClickListener
import com.fabirt.debty.util.toCurrencyString

class PersonSummaryViewHolder(
    private val binding: ViewItemPersonBinding,
    private val placeholderBitmap: Bitmap
) : RecyclerView.ViewHolder(binding.root) {

    private var clickListener: PersonClickListener? = null

    companion object {
        fun from(parent: ViewGroup, placeholderBitmap: Bitmap): PersonSummaryViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ViewItemPersonBinding.inflate(inflater, parent, false)
            return PersonSummaryViewHolder(binding, placeholderBitmap)
        }
    }

    fun bind(person: Person) {
        binding.tvName.text = person.name
        binding.tvAmount.text = person.total.toCurrencyString()
        val bitmap = person.picture ?: placeholderBitmap
        binding.image.setImageBitmap(bitmap)
    }

    fun setOnPersonClickListener(l: PersonClickListener) {
        clickListener = l
    }

    object Comparator : DiffUtil.ItemCallback<Person>() {
        override fun areItemsTheSame(oldItem: Person, newItem: Person): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Person, newItem: Person): Boolean {
            return oldItem == newItem
        }
    }
}