package com.yoox.samplekotlinapp.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yoox.net.ItemsBuilder
import com.yoox.net.models.outbound.Item
import com.yoox.samplekotlinapp.common.ScopedViewModel
import kotlinx.coroutines.launch

class DetailViewModel : ScopedViewModel() {
    private val items = ItemsBuilder("IT").build()

    private val itemLiveData = MutableLiveData<Item>()
    private val errorLiveData = MutableLiveData<String>()

    val item: LiveData<Item>
        get() = itemLiveData

    val error: LiveData<String>
        get() = errorLiveData

    internal fun loadItem(id: String) {
        scope.launch {
            try {
                val request = items.get(id)
                val result = request.execute()
                itemLiveData.value = result
            } catch (t: Throwable) {
                errorLiveData.value = t.toString()
            }
        }
    }
}
