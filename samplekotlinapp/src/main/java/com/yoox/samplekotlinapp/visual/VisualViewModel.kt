package com.yoox.samplekotlinapp.visual

import android.app.Application
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yoox.net.ItemsBuilder
import com.yoox.net.Request
import com.yoox.net.models.outbound.Gender
import com.yoox.net.models.outbound.VisualSearch
import com.yoox.samplekotlinapp.common.ImageEncoder
import com.yoox.samplekotlinapp.common.ScopedAndroidViewModel
import kotlinx.coroutines.launch

class VisualViewModel(application: Application) : ScopedAndroidViewModel(application) {


    private val items = ItemsBuilder("IT").build()
    private val women = Gender.Women

    private val stateLiveData = MutableLiveData<VisualState>()
    private val errorLiveData = MutableLiveData<String>()


    val state: LiveData<VisualState>
        get() = stateLiveData

    val error: LiveData<String>
        get() = errorLiveData


    internal fun loadItems(remoteImageUri: String?, localImageUri: Uri?) {

        val request: Request<VisualSearch>? = when {
            !remoteImageUri.isNullOrBlank() -> buildRemoteImageRequest(remoteImageUri)
            localImageUri != null -> buildLocalImageRequest(localImageUri)
            else -> null
        }

        if (request != null) {
            executeRequest(request)
        } else {
            errorLiveData.value = "Invalid parameters"
        }


    }

    private fun buildRemoteImageRequest(remoteImageUri: String): Request<VisualSearch> =
        items.visualSearchByUrl(women, remoteImageUri)

    private fun buildLocalImageRequest(localImageUri: Uri): Request<VisualSearch>? {

        val contentResolver = getApplication<Application>().contentResolver
        val encoded = ImageEncoder.encode(localImageUri, contentResolver)

        return if (encoded == null) {
            null
        } else {
            items.visualSearchByBase64(women, encoded)

        }

    }

    private fun executeRequest(request: Request<VisualSearch>) {
        val loadingState = VisualState.Loading
        stateLiveData.value = loadingState

        scope.launch {
            try {
                val result = request.execute()
                onResult(result)

            } catch (t: Throwable) {
                errorLiveData.value = t.toString()
            }
        }
    }


    private fun onResult(result: VisualSearch) {
        val state = VisualState.Result(result.items)
        this@VisualViewModel.stateLiveData.value = state
    }
}
