package com.yoox.samplejavaapp.visual;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import com.yoox.net.models.outbound.SearchResultItem;
import com.yoox.samplejavaapp.R;
import com.yoox.samplejavaapp.common.SearchResultItemAdapter;
import com.yoox.samplejavaapp.detail.DetailActivity;

import static com.yoox.samplejavaapp.detail.DetailActivity.ID;

public class VisualActivity extends AppCompatActivity implements SearchResultItemAdapter.OnItemSelectedListener {

    public static final String LOCAL_IMAGE_URI = "LOCAL_IMAGE_URI";
    public static final String REMOTE_IMAGE_URI = "REMOTE_IMAGE_URI";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visual);

        View loader = findViewById(R.id.loader);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        SearchResultItemAdapter adapter = new SearchResultItemAdapter(this);
        recyclerView.setAdapter(adapter);

        VisualViewModel visualViewModel = ViewModelProviders.of(this).get(VisualViewModel.class);
        visualViewModel.getState()
                .observe(this, newData -> {

                    if (newData.isLoading()) {
                        loader.setVisibility(View.VISIBLE);
                    } else {
                        loader.setVisibility(View.INVISIBLE);
                    }


                    adapter.replaceItems(newData.getItems());

                });

        visualViewModel.getError().observe(this, error ->
                Toast.makeText(this, error, Toast.LENGTH_LONG).show());


        String remoteImageUri = getIntent().getStringExtra(REMOTE_IMAGE_URI);
        Uri localImageUri = getIntent().getParcelableExtra(LOCAL_IMAGE_URI);
        visualViewModel.loadItems(remoteImageUri, localImageUri);

    }

    @Override
    public void onItemSelected(SearchResultItem item) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(ID, item.getId());
        startActivity(intent);
    }

}
