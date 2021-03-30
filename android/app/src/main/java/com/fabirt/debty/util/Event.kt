package com.fabirt.debty.util

open class Event<out T>(private val data: T) {
    var hasBeenHandled: Boolean = false
        private set

    fun getContent(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            data
        }
    }

    fun peekContent() = data
}