package com.yoox.samplekotlinapp.visual

import com.yoox.net.models.outbound.SearchResultItem

sealed class VisualState {
    object Loading : VisualState()
    data class Result(val items: List<SearchResultItem>) : VisualState()
}
