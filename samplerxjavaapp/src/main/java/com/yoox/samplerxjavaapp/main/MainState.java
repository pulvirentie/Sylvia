package com.yoox.samplerxjavaapp.main;

import com.yoox.net.FilterableRequest;
import com.yoox.net.models.outbound.Prices;
import com.yoox.net.models.outbound.Refinement;
import com.yoox.net.models.outbound.SearchResultItem;
import com.yoox.net.models.outbound.SearchStats;

import java.util.List;

class MainState {

    private final boolean loading;
    private final FilterableRequest request;
    private final List<SearchResultItem> items;
    private final SearchStats stats;
    private final boolean previousEnabled;
    private final boolean nextEnabled;
    private final List<Refinement> refinements;
    private final boolean filterColorEnabled;
    private final boolean filterDesignerEnabled;
    private final boolean filterCategoryEnabled;
    private final Prices prices;
    private final boolean filterPriceEnabled;


    public MainState(boolean loading, FilterableRequest request, List<SearchResultItem> items, SearchStats stats, boolean previousEnabled, boolean nextEnabled, List<Refinement> refinements, boolean filterColorEnabled, boolean filterDesignerEnabled, boolean filterCategoryEnabled, Prices prices, boolean filterPriceEnabled) {
        this.loading = loading;
        this.request = request;
        this.items = items;
        this.stats = stats;
        this.previousEnabled = previousEnabled;
        this.nextEnabled = nextEnabled;
        this.refinements = refinements;
        this.filterColorEnabled = filterColorEnabled;
        this.filterDesignerEnabled = filterDesignerEnabled;
        this.filterCategoryEnabled = filterCategoryEnabled;
        this.prices = prices;
        this.filterPriceEnabled = filterPriceEnabled;
    }

    public boolean isLoading() {
        return loading;
    }

    public FilterableRequest getRequest() {
        return request;
    }

    public List<SearchResultItem> getItems() {
        return items;
    }

    public SearchStats getStats() {
        return stats;
    }

    public boolean isPreviousEnabled() {
        return previousEnabled;
    }

    public boolean isNextEnabled() {
        return nextEnabled;
    }

    public List<Refinement> getRefinements() {
        return refinements;
    }

    public boolean isFilterColorEnabled() {
        return filterColorEnabled;
    }

    public boolean isFilterDesignerEnabled() {
        return filterDesignerEnabled;
    }

    public boolean isFilterCategoryEnabled() {
        return filterCategoryEnabled;
    }

    public Prices getPrices() {
        return prices;
    }

    public boolean isFilterPriceEnabled() {
        return filterPriceEnabled;
    }
}
