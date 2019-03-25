@file:JvmName("AndroidExecutor")

package com.yoox.sylvia.androidcallback

import android.os.Handler
import android.os.Looper
import com.yoox.net.Request
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

interface Callback<T> {

    fun onComplete(result: T)

    fun onError(t: Throwable)
}

interface Consumer<T> {

    fun accept(value: T)
}

private val main: Handler = Handler(Looper.getMainLooper())

fun <T> Request<T>.execute(callback: Callback<T>) {

    val onComplete: (T) -> Unit = callback::onComplete
    val onError: (Throwable) -> Unit = callback::onError

    internalExecute(onComplete, onError)
}

fun <T> Request<T>.execute(
    onComplete: Consumer<T>,
    onError: Consumer<Throwable>
) {

    internalExecute(onComplete::accept, onError::accept)
}

private fun <T> Request<T>.internalExecute(
    onComplete: (T) -> Unit,
    onError: (Throwable) -> Unit
) {
    GlobalScope.launch(Dispatchers.IO) {
        try {
            val result: T = execute()
            main.post { onComplete(result) }
        } catch (e: Exception) {
            main.post { onError(e) }
        }
    }
}