package com.yoox.samplekotlinapp.common

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

open class ScopedViewModel : ViewModel() {
    private val job = Job()
    protected val scope: CoroutineScope = CoroutineScope(job + Dispatchers.Main)

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}