package com.fabirt.debty.util

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class CurrencyTextWatcher(
    private val editText: EditText
) : TextWatcher {
    private var current = ""

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(s: Editable?) {
        val stringText = s.toString()

        if (stringText != current) {
            editText.removeTextChangedListener(this)
            val cleanString = stringText.removeGroupingSeparator()
            val parsed = cleanString.toDoubleOrNull()
            if (parsed != null) {
                val formatted = parsed.toDecimalString()
                current = formatted
                editText.setText(formatted)
                editText.setSelection(formatted.length)
            }

            editText.addTextChangedListener(this)
        }
    }
}
