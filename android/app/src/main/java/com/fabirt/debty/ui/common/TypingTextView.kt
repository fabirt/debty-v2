package com.fabirt.debty.ui.common

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class TypingTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {
    private var mText: CharSequence? = null
    private var mIndex = 0
    private var mDelay: Long = 150 // in ms

    private val mHandler: Handler = Handler(Looper.getMainLooper())

    private val characterAdder: Runnable = object : Runnable {
        override fun run() {
            text = mText!!.subSequence(0, mIndex++)
            if (mIndex <= mText!!.length) {
                mHandler.postDelayed(this, mDelay)
            }
        }
    }

    fun animateText(text: CharSequence, startText: String = "", startIndex: Int = 0) {
        mText = text
        mIndex = startIndex
        setText(startText)
        mHandler.removeCallbacks(characterAdder)
        mHandler.postDelayed(characterAdder, mDelay)
    }

    fun setCharacterDelay(m: Long) {
        mDelay = m
    }
}