package com.yoox.samplerxjavaapp.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Fade;
import androidx.transition.Slide;
import androidx.transition.TransitionManager;
import androidx.transition.TransitionSet;
import androidx.viewpager.widget.ViewPager;
import com.yoox.net.models.outbound.Color;
import com.yoox.net.models.outbound.Image;
import com.yoox.net.models.outbound.Item;
import com.yoox.net.models.outbound.Size;
import com.yoox.samplerxjavaapp.R;
import com.yoox.samplerxjavaapp.common.ImageAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DetailActivity extends AppCompatActivity implements ColorAdapter.OnColorSelectedListener, SizeAdapter.OnSizeSelectedListener {

    private static final String KEY_ITEM_ID = "ITEM_ID";

    public static void start(Context context, String itemID) {
        Intent starter = new Intent(context, DetailActivity.class);
        starter.putExtra(KEY_ITEM_ID, itemID);
        context.startActivity(starter);
    }

    private ViewGroup root;
    private ViewPager viewPager;
    private TextView brand;
    private TextView category;
    private TextView price;
    private TextView composition;
    private RecyclerView colorRecyclerView;
    private RecyclerView sizeRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        root = findViewById(R.id.root);
        viewPager = findViewById(R.id.pager);
        brand = findViewById(R.id.brand);
        category = findViewById(R.id.category);
        price = findViewById(R.id.price);
        composition = findViewById(R.id.composition);
        colorRecyclerView = findViewById(R.id.color_recyclerView);
        sizeRecyclerView = findViewById(R.id.size_recyclerView);

        DetailViewModel detailViewModel = ViewModelProviders.of(this).get(DetailViewModel.class);
        detailViewModel.getItem()
                .observe(this, this::bindUI);

        detailViewModel.getError().observe(this, error ->
                Toast.makeText(this, error, Toast.LENGTH_LONG).show());


        String id = getIntent().getStringExtra(KEY_ITEM_ID);
        detailViewModel.loadItem(id);
    }

    private void bindUI(Item item) {
        TransitionSet transition = new TransitionSet();
        transition.addTransition(new Slide());
        transition.addTransition(new Fade());
        TransitionManager.beginDelayedTransition(root, transition);

        List<String> imagesUrl = Collections.singletonList(item.getPreviewImage());
        List<Size> sizes = Collections.emptyList();

        boolean hasValidColor = !item.getColors().isEmpty();

        if (hasValidColor) {
            Color color = item.getColors().get(0);
            imagesUrl = getImagesUrl(color.getImages());
            sizes = color.getSizeList();
        }

        viewPager.setAdapter(new ImageAdapter(imagesUrl));

        brand.setText(item.getBrand().getName());
        brand.setVisibility(View.VISIBLE);
        category.setText(item.getCategory().getName().getName());
        category.setVisibility(View.VISIBLE);
        price.setText(item.getDiscountedPrice().getRawPrice());
        price.setVisibility(View.VISIBLE);
        composition.setText(item.getComposition());
        composition.setVisibility(View.VISIBLE);

        ColorAdapter colorAdapter = new ColorAdapter(item.getColors(), this);
        colorRecyclerView.setAdapter(colorAdapter);

        SizeAdapter sizeAdapter = new SizeAdapter(sizes, this);
        sizeRecyclerView.setAdapter(sizeAdapter);
    }

    private List<String> getImagesUrl(List<Image> images) {
        ArrayList<String> result = new ArrayList<>(images.size());
        for (Image image : images) {
            result.add(image.getThumbnailUrl());
        }
        return result;
    }

    @Override
    public void onColorSelected(Color item) {
        Toast.makeText(this, item.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSizeSelected(Size item) {
        Toast.makeText(this, item.toString(), Toast.LENGTH_SHORT).show();
    }
}
