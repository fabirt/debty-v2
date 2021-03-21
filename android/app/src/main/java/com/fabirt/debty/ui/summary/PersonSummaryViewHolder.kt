package com.fabirt.debty.ui.summary

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.fabirt.debty.R
import com.fabirt.debty.databinding.ViewItemPersonBinding
import com.fabirt.debty.domain.model.Person
import com.fabirt.debty.ui.common.PersonClickListener
import com.fabirt.debty.util.toCurrencyString

class PersonSummaryViewHolder(
    private val binding: ViewItemPersonBinding,
    private val context: Context
) : RecyclerView.ViewHolder(binding.root) {

    private var clickListener: PersonClickListener? = null

    companion object {
        fun from(parent: ViewGroup, context: Context): PersonSummaryViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ViewItemPersonBinding.inflate(inflater, parent, false)
            return PersonSummaryViewHolder(binding, context)
        }
    }

    fun bind(person: Person) {
        binding.tvName.text = person.name
        binding.tvAmount.text = if (person.total != null) {
            person.total.toCurrencyString()
        } else context.getString(R.string.no_movements)
        val placeholder =
            BitmapFactory.decodeResource(context.resources, R.drawable.avatar_placeholder)
        val bitmap = person.picture ?: placeholder
        binding.tvIndicator.isVisible = person.total != null
        val indicator = person.indicator
        binding.tvIndicator.setTextColor(context.getColor(indicator.colorId))
        binding.tvIndicator.text = context.getString(indicator.stringId)
        binding.image.setImageBitmap(bitmap)
        binding.container.setOnClickListener {
            clickListener?.invoke(person)
        }
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