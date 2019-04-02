package com.yoox.samplekotlinapp.main

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.yoox.net.models.outbound.SearchResultItem
import com.yoox.samplekotlinapp.R
import com.yoox.samplekotlinapp.common.SearchResultItemAdapter
import com.yoox.samplekotlinapp.detail.DetailActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adapter = SearchResultItemAdapter(this::onItemSelected)
        recyclerView.adapter = adapter

        val mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        mainViewModel.state
            .observe(this, Observer { newData ->

                return@Observer when (newData) {
                    MainState.Loading -> {
                        renderLoading()
                    }
                    is MainState.Result -> {
                        renderResult(newData, adapter, mainViewModel)
                    }
                }
            })

        mainViewModel.error.observe(this, Observer { error ->
            Toast.makeText(this, error, Toast.LENGTH_LONG).show()
        })
        mainViewModel.loadItems()
    }

    private fun renderLoading() {
        loader.visibility = View.VISIBLE
    }

    private fun renderResult(
        newData: MainState.Result,
        adapter: SearchResultItemAdapter,
        mainViewModel: MainViewModel
    ) {
        loader.visibility = View.INVISIBLE

        val request = newData.request

        previous_page.isEnabled = newData.isPreviousEnabled
        next_page.isEnabled = newData.isNextEnabled
        filter_color.isEnabled = newData.isFilterColorEnabled
        filter_designer.isEnabled = newData.isFilterDesignerEnabled
        filter_category.isEnabled = newData.isFilterCategoryEnabled
        filter_prices.isEnabled = newData.isFilterPriceEnabled
        adapter.replaceItems(newData.items)

        previous_page.setOnClickListener { mainViewModel.previousPage(request, newData) }
        next_page.setOnClickListener { mainViewModel.nextPage(request, newData) }
        filter_color.setOnClickListener { mainViewModel.colorFilter(request, newData) }
        filter_designer.setOnClickListener { mainViewModel.designerFilter(request, newData) }
        filter_category.setOnClickListener { mainViewModel.categoryFilter(request, newData) }
        filter_prices.setOnClickListener { mainViewModel.priceFilter(request, newData) }
        reset.setOnClickListener { mainViewModel.loadItems() }
    }

    private fun onItemSelected(item: SearchResultItem) {
        DetailActivity.start(this, item.id)
    }
}
