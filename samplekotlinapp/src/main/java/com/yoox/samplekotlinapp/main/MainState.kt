package com.yoox.samplekotlinapp.main

import com.yoox.net.FilterableRequest
import com.yoox.net.models.outbound.Prices
import com.yoox.net.models.outbound.Refinement
import com.yoox.net.models.outbound.SearchResultItem
import com.yoox.net.models.outbound.SearchStats

sealed class MainState {

    object Loading : MainState()

    data class Result(
        val request: FilterableRequest,
        val items: List<SearchResultItem>,
        val stats: SearchStats,
        val isPreviousEnabled: Boolean,
        val isNextEnabled: Boolean,
        val refinements: List<Refinement>,
        val isFilterColorEnabled: Boolean,
        val isFilterDesignerEnabled: Boolean,
        val isFilterCategoryEnabled: Boolean,
        val prices: Prices,
        val isFilterPriceEnabled: Boolean
    ) : MainState()
}
