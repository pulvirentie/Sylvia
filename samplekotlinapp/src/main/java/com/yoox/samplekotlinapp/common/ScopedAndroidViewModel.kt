package com.yoox.samplekotlinapp.common

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

open class ScopedAndroidViewModel(application: Application) : AndroidViewModel(application) {
    private val job = Job()
    protected val scope: CoroutineScope = CoroutineScope(job + Dispatchers.Main)

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}