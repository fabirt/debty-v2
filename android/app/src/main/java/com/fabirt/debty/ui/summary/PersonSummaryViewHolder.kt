package com.fabirt.debty.ui.summary

import android.content.Context
import android.graphics.Bitmap
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PersonSummaryViewHolder(
    private val binding: ViewItemPersonBinding,
    private val context: Context,
) : RecyclerView.ViewHolder(binding.root) {

    private var clickListener: PersonClickListener? = null

    companion object {
        fun from(parent: ViewGroup, context: Context): PersonSummaryViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ViewItemPersonBinding.inflate(inflater, parent, false)
            return PersonSummaryViewHolder(binding, context)
        }
    }

    fun bind(person: Person, defaultAvatar: Bitmap) {
        binding.tvName.text = person.name
        binding.tvAmount.text = if (person.total != null) {
            person.total.toCurrencyString()
        } else context.getString(R.string.no_movements)
        val bitmap = person.picture ?: defaultAvatar
        binding.image.setImageBitmap(bitmap)

        binding.tvIndicator.isVisible = person.total != null
        val indicator = person.indicator
        binding.tvIndicator.setTextColor(context.getColor(indicator.colorId))
        binding.tvIndicator.text = context.getString(indicator.stringId)

        binding.container.setOnClickListener {
            clickListener?.invoke(person)
        }
    }

    fun setOnPersonClickListener(l: PersonClickListener) {
        clickListener = l
    }
}