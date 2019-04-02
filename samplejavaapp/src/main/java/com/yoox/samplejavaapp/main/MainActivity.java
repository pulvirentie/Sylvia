package com.yoox.samplejavaapp.main;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import com.yoox.net.FilterableRequest;
import com.yoox.net.models.outbound.SearchResultItem;
import com.yoox.samplejavaapp.R;
import com.yoox.samplejavaapp.common.SearchResultItemAdapter;
import com.yoox.samplejavaapp.detail.DetailActivity;

public class MainActivity extends AppCompatActivity implements SearchResultItemAdapter.OnItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View loader = findViewById(R.id.loader);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        View previousPage = findViewById(R.id.previous_page);
        View nextPage = findViewById(R.id.next_page);
        View filterColor = findViewById(R.id.filter_color);
        View filterDesigner = findViewById(R.id.filter_designer);
        View filterCategory = findViewById(R.id.filter_category);
        View filterPrices = findViewById(R.id.filter_prices);
        View reset = findViewById(R.id.reset);

        SearchResultItemAdapter adapter = new SearchResultItemAdapter(this);
        recyclerView.setAdapter(adapter);

        MainViewModel mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mainViewModel.getState()
                .observe(this, newData -> {

                    if (newData.isLoading()) {
                        loader.setVisibility(View.VISIBLE);
                    } else {
                        loader.setVisibility(View.INVISIBLE);
                    }

                    FilterableRequest request = newData.getRequest();

                    previousPage.setEnabled(newData.isPreviousEnabled());
                    nextPage.setEnabled(newData.isNextEnabled());
                    filterColor.setEnabled(newData.isFilterColorEnabled());
                    filterDesigner.setEnabled(newData.isFilterDesignerEnabled());
                    filterCategory.setEnabled(newData.isFilterCategoryEnabled());
                    filterPrices.setEnabled(newData.isFilterPriceEnabled());
                    adapter.replaceItems(newData.getItems());

                    previousPage.setOnClickListener(v -> mainViewModel.previousPage(request, newData));
                    nextPage.setOnClickListener(v -> mainViewModel.nextPage(request, newData));
                    filterColor.setOnClickListener(v -> mainViewModel.colorFilter(request, newData));
                    filterDesigner.setOnClickListener(v -> mainViewModel.designerFilter(request, newData));
                    filterCategory.setOnClickListener(v -> mainViewModel.categoryFilter(request, newData));
                    filterPrices.setOnClickListener(v -> mainViewModel.priceFilter(request, newData));
                    reset.setOnClickListener(v -> mainViewModel.loadItems());
                });

        mainViewModel.getError().observe(this, error ->
                Toast.makeText(this, error, Toast.LENGTH_LONG).show());
        mainViewModel.loadItems();


    }

    @Override
    public void onItemSelected(SearchResultItem item) {
        DetailActivity.start(this, item.getId());
    }
}
