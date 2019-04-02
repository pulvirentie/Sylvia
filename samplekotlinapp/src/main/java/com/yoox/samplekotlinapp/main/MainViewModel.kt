package com.yoox.samplekotlinapp.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yoox.net.FilterableRequest
import com.yoox.net.ItemsBuilder
import com.yoox.net.models.outbound.DepartmentType
import com.yoox.net.models.outbound.Filter
import com.yoox.net.models.outbound.PriceFilter
import com.yoox.net.models.outbound.Prices
import com.yoox.net.models.outbound.Refinement
import com.yoox.net.models.outbound.SearchResults
import com.yoox.net.models.outbound.categories
import com.yoox.net.models.outbound.colors
import com.yoox.net.models.outbound.designers
import com.yoox.samplekotlinapp.common.ScopedViewModel
import kotlinx.coroutines.launch
import java.util.concurrent.ThreadLocalRandom

class MainViewModel : ScopedViewModel() {
    private val items = ItemsBuilder("IT").build()

    private val stateLiveData = MutableLiveData<MainState>()
    private val errorLiveData = MutableLiveData<String>()

    val state: LiveData<MainState>
        get() = stateLiveData

    val error: LiveData<String>
        get() = errorLiveData

    internal fun loadItems() {
        val request = items.search(department)
        executeRequest(request)
    }

    private fun executeRequest(request: FilterableRequest) {
        val loadingState = MainState.Loading
        stateLiveData.value = loadingState

        scope.launch {
            try {
                val result = request.execute()
                onResult(request, result)
            } catch (t: Throwable) {
                errorLiveData.value = t.toString()
            }
        }
    }

    internal fun previousPage(request: FilterableRequest, mainState: MainState.Result) {
        val currentPage = mainState.stats.currentPageIndex
        searchAtPage(request, currentPage - 1)
    }

    internal fun nextPage(request: FilterableRequest, mainState: MainState.Result) {
        val currentPage = mainState.stats.currentPageIndex
        searchAtPage(request, currentPage + 1)
    }

    internal fun colorFilter(request: FilterableRequest, mainState: MainState.Result) {
        val refinements = mainState.refinements
        val filter = colorFilter(refinements)
        executeFilterRequest(request, filter)
    }

    internal fun designerFilter(request: FilterableRequest, mainState: MainState.Result) {
        val refinements = mainState.refinements
        val filter = designerFilter(refinements)
        executeFilterRequest(request, filter)
    }

    internal fun categoryFilter(request: FilterableRequest, mainState: MainState.Result) {
        val refinements = mainState.refinements
        val filter = categoryFilter(refinements)
        executeFilterRequest(request, filter)
    }

    internal fun priceFilter(request: FilterableRequest, mainState: MainState.Result) {
        val filter = priceFilter(mainState.prices)

        filter?.let {
            val filterRequest = request.filterBy(it)
            executeRequest(filterRequest)
        }
    }

    private fun onResult(request: FilterableRequest, searchResults: SearchResults) {
        val currentPage = searchResults.stats.currentPageIndex
        val previousEnabled = currentPage > 1
        val nextEnabled = currentPage < searchResults.stats.pageCount
        val requestClone = request.clone()
        val refinements = searchResults.refinements
        val prices = searchResults.prices
        val hasColorFilter = colorFilter(refinements) != null
        val filterDesigner = designerFilter(refinements) != null
        val filterCategoryEnabled = categoryFilter(refinements) != null
        val filterPriceEnabled = priceFilter(prices) != null
        val state = MainState.Result(
            requestClone,
            searchResults.items,
            searchResults.stats,
            previousEnabled,
            nextEnabled,
            refinements,
            hasColorFilter,
            filterDesigner,
            filterCategoryEnabled,
            prices,
            filterPriceEnabled
        )
        this@MainViewModel.stateLiveData.value = state
    }

    private fun executeFilterRequest(request: FilterableRequest, filter: Filter?) {
        if (filter != null) {
            val filterableRequest = request.filterBy(filter)
            executeRequest(filterableRequest)
        }
    }

    private fun searchAtPage(request: FilterableRequest, page: Int) {
        val pageRequest = request.page(page)
        executeRequest(pageRequest)
    }

    private fun colorFilter(refinements: List<Refinement>): Filter? {
        return randomFilter(refinements.colors())
    }

    private fun designerFilter(refinements: List<Refinement>): Filter? {
        return randomFilter(refinements.designers())
    }

    private fun categoryFilter(refinements: List<Refinement>): Filter? {
        return randomFilter(refinements.categories())
    }

    private fun priceFilter(prices: Prices): PriceFilter? {
        val availableMax = prices.availableMax
        if (prices.availableMin == 0 && availableMax == 0) {
            return null
        }

        val halfPrice = availableMax / 2
        val randomMin = ThreadLocalRandom.current().nextInt(0, halfPrice)
        val randomMax = ThreadLocalRandom.current().nextInt(halfPrice, availableMax)

        return PriceFilter(randomMin, randomMax)
    }

    private fun randomFilter(filters: List<Filter>): Filter {
        val rand = ThreadLocalRandom.current()
        return filters[rand.nextInt(filters.size)]
    }

    companion object {
        private val department = DepartmentType.Women
    }
}
