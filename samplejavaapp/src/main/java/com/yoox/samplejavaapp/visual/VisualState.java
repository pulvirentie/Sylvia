package com.yoox.samplejavaapp.visual;

import com.yoox.net.models.outbound.SearchResultItem;

import java.util.List;

class VisualState {

    private final boolean loading;
    private final List<SearchResultItem> items;


    public VisualState(boolean loading, List<SearchResultItem> items) {
        this.loading = loading;
        this.items = items;
    }

    public boolean isLoading() {
        return loading;
    }


    public List<SearchResultItem> getItems() {
        return items;
    }

}
