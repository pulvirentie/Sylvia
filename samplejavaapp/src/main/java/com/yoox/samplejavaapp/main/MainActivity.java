package com.yoox.samplejavaapp.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import com.yoox.net.FilterableRequest;
import com.yoox.net.models.outbound.SearchResultItem;
import com.yoox.samplejavaapp.R;
import com.yoox.samplejavaapp.common.Glide4Engine;
import com.yoox.samplejavaapp.common.SearchResultItemAdapter;
import com.yoox.samplejavaapp.detail.DetailActivity;
import com.yoox.samplejavaapp.visual.VisualActivity;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.util.List;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static com.yoox.samplejavaapp.common.PermissionUtils.askPermission;
import static com.yoox.samplejavaapp.detail.DetailActivity.ID;
import static com.yoox.samplejavaapp.visual.VisualActivity.LOCAL_IMAGE_URI;

public class MainActivity extends AppCompatActivity implements SearchResultItemAdapter.OnItemSelectedListener {

    private static final int READ_REQUEST = 3;
    private static final int WRITE_REQUEST = 2;
    private static final int PHOTO_REQUEST = 4;
    private static final String FILE_PROVIDER = "com.yoox.samplejavaapp.fileprovider";

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
        View photo = findViewById(R.id.photo);
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
                    photo.setOnClickListener(v -> askPermissionsAndRequestPhoto());
                    reset.setOnClickListener(v -> mainViewModel.loadItems());
                });

        mainViewModel.getError().observe(this, error ->
                Toast.makeText(this, error, Toast.LENGTH_LONG).show());
        mainViewModel.loadItems();


    }

    @Override
    public void onItemSelected(SearchResultItem item) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(ID, item.getId());
        startActivity(intent);
    }

    private void askPermissionsAndRequestPhoto() {
        if (askPermission(READ_EXTERNAL_STORAGE, this, READ_REQUEST) && askPermission(WRITE_EXTERNAL_STORAGE, this, WRITE_REQUEST)) {
            requestPhoto();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case WRITE_REQUEST:
                if (grantResults[0] == PERMISSION_GRANTED) {
                    askPermissionsAndRequestPhoto();
                }
                break;

            case READ_REQUEST:
                if (grantResults[0] == PERMISSION_GRANTED) {
                    askPermissionsAndRequestPhoto();
                }
                break;
        }
    }

    private void requestPhoto() {
        Matisse.from(MainActivity.this)
                .choose(MimeType.ofAll())
                .maxSelectable(1)
                .thumbnailScale(0.85f)
                .imageEngine(new Glide4Engine())
                .capture(true)
                .captureStrategy(new CaptureStrategy(true, FILE_PROVIDER))
                .forResult(PHOTO_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PHOTO_REQUEST && resultCode == RESULT_OK) {
            List<Uri> uris = Matisse.obtainResult(data);
            Intent intent = new Intent(this, VisualActivity.class);
            intent.putExtra(LOCAL_IMAGE_URI, uris.get(0));
            startActivity(intent);
        }
    }
}
