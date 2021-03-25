package com.fabirt.debty.ui.common

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.fabirt.debty.R
import com.fabirt.debty.databinding.ViewIconLabelButtonBinding

class IconLabelButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
    defStyleRes: Int = 0
) : ConstraintLayout(context, attrs, defStyle, defStyleRes) {

    private val _binding: ViewIconLabelButtonBinding

    init {
        val inflater = LayoutInflater.from(context)
        _binding = ViewIconLabelButtonBinding.inflate(inflater, this, true)

        attrs?.let {
            context.obtainStyledAttributes(it, R.styleable.IconLabelButton).apply {
                try {
                    _binding.label.text = getString(R.styleable.IconLabelButton_text)
                    _binding.icon.setImageDrawable(getDrawable(R.styleable.IconLabelButton_icon))
                } finally {
                    recycle()
                }
            }
        }
    }
}