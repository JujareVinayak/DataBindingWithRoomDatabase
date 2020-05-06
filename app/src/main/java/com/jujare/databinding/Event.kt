package com.jujare.databinding

import android.util.Log

open class Event<out T>(private val content: T) {

    private val TAG = "com.jujare.databinding.Event"
    var hasBeenHandled = false
        private set // Allow external read but not write

    /**
     * Returns the content and prevents its use again.
     */
    fun getContentIfNotHandled(): T? {
        Log.d(TAG,"getContentIfNotHandled")
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    /**
     * Returns the content, even if it's already been handled.
     */
    fun peekContent(): T = content
}