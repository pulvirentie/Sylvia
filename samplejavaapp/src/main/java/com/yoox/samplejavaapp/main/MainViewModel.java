package com.yoox.samplejavaapp.main;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.yoox.net.FilterableRequest;
import com.yoox.net.Items;
import com.yoox.net.ItemsBuilder;
import com.yoox.net.models.outbound.*;
import com.yoox.sylvia.androidcallback.AndroidExecutor;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class MainViewModel extends ViewModel {
    private static final DepartmentType department = DepartmentType.Women;

    private final Items items = new ItemsBuilder("IT").build();

    private final MutableLiveData<MainState> stateLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    public LiveData<MainState> getState() {
        return stateLiveData;
    }

    public LiveData<String> getError() {
        return errorLiveData;
    }

    void loadItems() {
        FilterableRequest request = items.search(department);
        executeRequest(request);
    }

    private void executeRequest(FilterableRequest request) {
        MainState loadingState = new MainState(true, request, Collections.emptyList(), null, false, false, Collections.emptyList(), false, false, false, null, false);
        stateLiveData.setValue(loadingState);
        AndroidExecutor.execute(request,
                result -> onResult(request, result),
                throwable -> MainViewModel.this.errorLiveData.setValue(throwable.toString()));
    }

    void previousPage(FilterableRequest request, MainState mainState) {
        int currentPage = mainState.getStats().getCurrentPageIndex();
        searchAtPage(request, currentPage - 1);
    }

    void nextPage(FilterableRequest request, MainState mainState) {
        int currentPage = mainState.getStats().getCurrentPageIndex();
        searchAtPage(request, currentPage + 1);
    }

    void colorFilter(FilterableRequest request, MainState mainState) {
        List<Refinement> refinements = mainState.getRefinements();
        Filter filter = colorFilter(refinements);
        executeFilterRequest(request, filter);
    }

    void designerFilter(FilterableRequest request, MainState mainState) {
        List<Refinement> refinements = mainState.getRefinements();
        Filter filter = designerFilter(refinements);
        executeFilterRequest(request, filter);
    }

    void categoryFilter(FilterableRequest request, MainState mainState) {
        List<Refinement> refinements = mainState.getRefinements();
        Filter filter = categoryFilter(refinements);
        executeFilterRequest(request, filter);
    }

    void priceFilter(FilterableRequest request, MainState mainState) {
        PriceFilter filter = priceFilter(mainState.getPrices());
        FilterableRequest filterRequest = request.filterBy(filter);
        executeRequest(filterRequest);
    }

    private void onResult(FilterableRequest request, SearchResults searchResults) {
        int currentPage = searchResults.getStats().getCurrentPageIndex();
        boolean previousEnabled = currentPage > 1;
        boolean nextEnabled = currentPage < searchResults.getStats().getPageCount();
        FilterableRequest requestClone = request.clone();
        List<Refinement> refinements = searchResults.getRefinements();
        Prices prices = searchResults.getPrices();
        boolean hasColorFilter = colorFilter(refinements) != null;
        boolean filterDesigner = designerFilter(refinements) != null;
        boolean filterCategoryEnabled = categoryFilter(refinements) != null;
        boolean filterPriceEnabled = priceFilter(prices) != null;
        MainState state = new MainState(false, requestClone, searchResults.getItems(), searchResults.getStats(), previousEnabled, nextEnabled, refinements, hasColorFilter, filterDesigner, filterCategoryEnabled, prices, filterPriceEnabled);
        MainViewModel.this.stateLiveData.setValue(state);
    }

    private void executeFilterRequest(FilterableRequest request, Filter filter) {
        if (filter != null) {
            FilterableRequest filterableRequest = request.filterBy(filter);
            executeRequest(filterableRequest);
        }
    }

    private void searchAtPage(FilterableRequest request, int page) {
        FilterableRequest pageRequest = request.page(page);
        executeRequest(pageRequest);
    }

    @Nullable
    private Filter colorFilter(List<Refinement> refinements) {
        return randomFilter(ModelsKt.colors(refinements));
    }

    @Nullable
    private Filter designerFilter(List<Refinement> refinements) {
        return randomFilter(ModelsKt.designers(refinements));
    }

    @Nullable
    private Filter categoryFilter(List<Refinement> refinements) {
        return randomFilter(ModelsKt.categories(refinements));
    }

    @Nullable
    private PriceFilter priceFilter(Prices prices) {
        int availableMax = prices.getAvailableMax();
        if (prices.getAvailableMin() == 0 && availableMax == 0) {
            return null;
        }

        int halfPrice = availableMax / 2;
        int randomMin = ThreadLocalRandom.current().nextInt(0, halfPrice);
        int randomMax = ThreadLocalRandom.current().nextInt(halfPrice, availableMax);

        return new PriceFilter(randomMin, randomMax);
    }

    private Filter randomFilter(List<Filter> filters) {
        Random rand = ThreadLocalRandom.current();
        return filters.get(rand.nextInt(filters.size()));
    }
}
